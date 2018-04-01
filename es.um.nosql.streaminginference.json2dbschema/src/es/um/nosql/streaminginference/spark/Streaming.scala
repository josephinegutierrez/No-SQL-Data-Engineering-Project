package es.um.nosql.streaminginference.spark

import java.io.File

import scala.collection.immutable.HashMap

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.State
import org.apache.spark.streaming.StateSpec
import org.apache.spark.streaming.StreamingContext

import es.um.nosql.streaminginference.NoSQLSchema.NoSQLSchema
import es.um.nosql.streaminginference.spark.input.CustomDSFactory
import es.um.nosql.streaminginference.spark.utils.CrossReferenceMatcher
import es.um.nosql.streaminginference.spark.utils.EcoreHelper
import es.um.nosql.streaminginference.spark.utils.IO
import java.nio.file.Paths
import org.apache.spark.rdd.RDD

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path


object Streaming
{  
  val CheckpointDir = "checkpoint"

  val updateExistingSchema = (schemaName: String, schema: Option[NoSQLSchema], state: State[NoSQLSchema]) =>
  {    
    val matcher:CrossReferenceMatcher = new CrossReferenceMatcher()
    // Match references between state and schema before merging
    // to avoid the creation of unnecesary versions
    matcher.setCrossReferences(state.getOption(), schema)
    val acc = EcoreHelper.merge(state.getOption(), schema)
    if (acc.isDefined)
      state.update(acc.get)
    (schemaName, acc)
  }
  
  def printHelp() 
  {
    println("Usage:")
    println("\t --mode mongo --database [databasename] --host [hostname] --port [portnumber] --output [outputDir] --load [xmi file] --user [username] --password [password]")
    println("\t --mode couch --host [hostname] --port [portnumber] --output [outputDir] --load [xmi file] --user [username] --password [password]")
    println("\t --mode file --input [inputDir] --output [outputDir] --load [xmi file]")
    println("-------")
    println("--mode: mongo|file -> Sets how spark will read input streams")
    println("\t mongo -> Read data through mongo change streams")
    println("\t mongo -> Read data through couchdb change notifications")
    println("\t file -> Read data through hdfs files")
    println("-------")
    println("--load: (optional) -> Specifies comma separated list of xmi models to load as initial state [file name must match database name]")
    println("-------")
    println("--database: (mongo only) sets database to watch")
    println("--host: (mongo,couchdb only) sets host to connect")
    println("--input: (file only) sets input directory to watch")
    println("--output: sets output directory to write results")
    println("--port: (mongo,couchdb only) sets port to connect") 
    println("--user: (mongo,couchdb only, optional) sets database username") 
    println("--password: (mongo,couchdb only, optional) sets database password") 
  }
  
  def parseOptions(args: Array[String]): HashMap[String, String] = 
  {  
    var options:HashMap[String, String] = HashMap()
    args
      .zipWithIndex
      .foreach 
      { 
        case (arg, index) => 
        {
          if (arg.startsWith("--") && args.size > index + 1)
            options += arg.substring(2) -> args(index+1) 
        }
      }
      options
  }
  
    
  def createContext(args: Array[String])(): StreamingContext =
  {
    val conf = new SparkConf().setMaster("local[*]").setAppName("StreamingInference")
    val ssc = new StreamingContext(conf, Seconds(15))
    val options = parseOptions(args)
    val outputDir = options("output")
    val inputDS = CustomDSFactory.create(ssc, options)
    
    var initialRDD:RDD[(String, NoSQLSchema)] = ssc.sparkContext.emptyRDD
    if (options.contains("load")) 
    {
      // Set xmi file as initial state
      val path = options("load")
      val schema = IO.fromXMIFile(path)
      val name = Paths.get(path).getFileName.toString.split("\\.")(0)      
      initialRDD = ssc.sparkContext.parallelize(List((name, schema))) 
    }
    
    val stateSpec = StateSpec.function(updateExistingSchema)
                             .initialState(initialRDD)
    
    inputDS
      .map 
      { 
        case (schemaName, content) => 
              (schemaName, IO.fromJSONString(schemaName, content)) 
      }
      .mapWithState(stateSpec)
      // Output XMI schema
      .foreachRDD(rdd => 
        rdd
          .collect()
          .map 
          {
            case (schemaName, Some(schema)) => 
              IO.toXMI(schema, outputDir + "/" + schemaName + ".xmi")
            case (_, None) => 
          }
      )

    ssc.checkpoint(CheckpointDir)
    ssc
  }
  
  def clean(directories: String*) = 
  {
    
    def recursiveClean(file: File): Unit = 
    {
      if (file.isDirectory) 
        file.listFiles().foreach(recursiveClean(_))
      file.delete
    }
    
    directories.foreach(directory => 
    {
      val outputPath:Path = new Path(directory);
      val conf: Configuration = new Configuration
		  val fs: FileSystem = FileSystem.get(conf)
		  try { fs.delete(outputPath, true) } catch { case _ : Throwable => { } }
//      val folder:File = new File(directory)
//      if (folder.isDirectory)
//        folder.listFiles.foreach(recursiveClean(_))
    })
  }
  
  def main(args: Array[String]) = 
  {
    try 
    {
      clean(CheckpointDir)
      val context = StreamingContext.getOrCreate(CheckpointDir, createContext(args))
      context.start()             // Start the computation
      context.awaitTermination()  // Wait for the computation to terminate
    
    } 
    catch 
    {
      case e: Exception => 
      {
        printHelp
        e.printStackTrace
      }
    }
  }
  
}