var mongoose = require('mongoose');
mongoose.Promise = require('bluebird');
mongoose.connect('mongodb://127.0.0.1/test3',
{
  useMongoClient: true
}, function(err)
{
  if (err)
    console.log(err);
  else
    console.log('Connected to 127.0.0.1/test3');
});
mongoose.set('debug', true);
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error: '));

var Persons = require('./app/models/PersonsSchema');
var CustomDate1 = require('./app/models/CustomDate1Schema');
var CustomDate2 = require('./app/models/CustomDate2Schema');
var PersonalData = require('./app/models/PersonalDataSchema');
var CustomDate3 = require('./app/models/CustomDate3Schema');

db.once('open', function()
{
  Persons.find({}, '-_id', function(err, result)
  {
    if (err)
      return console.error(err);

    console.log("Checking consistency of the \"Persons\" table");
    var errorNumber = 0;

    result.forEach(function(persons)
    {
      var validation = persons.validateSync();
      if (typeof validation !== "undefined")
      {
        console.log(validation);
        errorNumber++;
      }
    });

    if (errorNumber)
      console.log("\"Persons\" table: " + errorNumber + " errors found");
    else
      console.log("\"Persons\" table: No errors found!");
  });

});
