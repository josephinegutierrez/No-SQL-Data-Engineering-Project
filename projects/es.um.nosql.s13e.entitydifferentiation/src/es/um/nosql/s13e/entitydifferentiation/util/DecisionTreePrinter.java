package es.um.nosql.s13e.entitydifferentiation.util;

import java.io.File;

import es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreeForEntity;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreeNode;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTreePackage;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.DecisionTrees;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.IntermediateNode;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.LeafNode;
import es.um.nosql.s13e.entitydifferentiation.DecisionTree.PropertySpec2;
import es.um.nosql.s13e.NoSQLSchema.EntityType;
import es.um.nosql.s13e.util.ModelLoader;

public class DecisionTreePrinter
{
  private static final String TAB = "  ";
  private static final String ENDL = System.lineSeparator();

  public static void main(String[] args)
  {
    DecisionTreePrinter printer = new DecisionTreePrinter();
    String INPUT_FOLDER = "../es.um.nosql.models/";
    String[] input_models = new String[] {"everypolitician_sweden", "facebook", "harvard", "links","mongomovies", "opensanctions",
        "proteins", "publications", "stackoverflow", "urban", "webclicks", "mongosongs"};

    for (String input_model : input_models)
    {
      String inputFile = INPUT_FOLDER + input_model + "/" + input_model + "_Tree.xmi";
      System.out.println(printer.printPretty(inputFile));
    }
  }

  public String printPretty(String inputFile)
  {
    ModelLoader loader = new ModelLoader(DecisionTreePackage.eINSTANCE);
    DecisionTrees decTrees = loader.load(new File(inputFile), DecisionTrees.class);

    return printPretty(decTrees);
  }

  public String printPretty(DecisionTrees decTrees)
  {
    if (decTrees == null)
      return null;

    StringBuilder result = new StringBuilder();

    result.append("DecisionTrees name: " + decTrees.getName() + ENDL);

    for (DecisionTreeForEntity decTreeEntity : decTrees.getTrees())
      result.append(printPretty(decTreeEntity, TAB) + ENDL);

    return result.toString();
  }

  public String printPretty(DecisionTreeForEntity decTreeEntity)
  {
    return printPretty(decTreeEntity, "");
  }

  private String printPretty(DecisionTreeForEntity decTreeEntity, String defTabs)
  {
    if (decTreeEntity == null)
      return null;

    StringBuilder result = new StringBuilder();

    result.append(defTabs + "Entity: " + decTreeEntity.getEntity().getName() + ENDL);
    result.append(printPretty(decTreeEntity.getRoot(), defTabs + TAB));

    return result.toString();
  }

  public String printPretty(DecisionTreeNode dtNode)
  {
    return printPretty(dtNode, "");
  }

  private String printPretty(DecisionTreeNode dtNode, String defTabs)
  {
    if (dtNode == null)
      return null;

    StringBuilder result = new StringBuilder();

    if (dtNode instanceof IntermediateNode)
      result.append(printPretty((IntermediateNode) dtNode, defTabs));
    else if (dtNode instanceof LeafNode)
      result.append(printPretty((LeafNode) dtNode, defTabs));

    if (dtNode.getYesBranch() != null)
      result.append(defTabs + "YES branch - " + printBranch(dtNode.getYesBranch(), defTabs + TAB));

    if (dtNode.getNoBranch() != null)
      result.append(defTabs + "NO branch - " + printBranch(dtNode.getNoBranch(), defTabs + TAB));

    return result.toString();
  }

  private String printBranch(DecisionTreeNode dtNode, String defTabs)
  {
    if (dtNode == null)
      return null;

    StringBuilder result = new StringBuilder();

    if (dtNode instanceof IntermediateNode)
      result.append(printPretty((IntermediateNode) dtNode));
    else if (dtNode instanceof LeafNode)
      result.append(printPretty((LeafNode) dtNode));

    if (dtNode.getYesBranch() != null)
      result.append(defTabs + "YES - " + printBranch(dtNode.getYesBranch(), defTabs + TAB));

    if (dtNode.getNoBranch() != null)
      result.append(defTabs + "NO  - " + printBranch(dtNode.getNoBranch(), defTabs + TAB));

    return result.toString();
  }

  public String printPretty(IntermediateNode iNode)
  {
    return printPretty(iNode, "");
  }

  private String printPretty(IntermediateNode iNode, String defTabs)
  {
    if (iNode == null)
      return null;

    StringBuilder result = new StringBuilder();

    result.append(defTabs + "IntermediateNode: " + prettyPrint(iNode.getCheckedProperty()) + ENDL);

    return result.toString();
  }

  public String printPretty(LeafNode lNode, String defTabs)
  {
    if (lNode == null)
      return null;

    StringBuilder result = new StringBuilder();

    result.append(defTabs + "LeafNode: " 
      + ((EntityType)lNode.getIdentifiedVariation().getContainer()).getName() 
      + "_" + lNode.getIdentifiedVariation().getVariationId() + ENDL);

    return result.toString();
  }

  public String prettyPrint(PropertySpec2 pSpec2)
  {
    if (pSpec2 == null)
      return null;

    StringBuilder result = new StringBuilder();

    result.append(pSpec2.getProperty().getName() + (pSpec2.isNeedsTypeCheck() ? " (needsTypeCheck)" : ""));

    return result.toString();
  }
}
