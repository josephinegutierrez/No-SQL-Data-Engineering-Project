'use strict'

var mongoose = require('mongoose');
var IdentifierSchema = require('./IdentifierSchema.js');

var EventsSchema = new mongoose.Schema({
  id: {type: String, required: true},
  end_date: {type: String, required: true},
  classification: {type: String, required: true},
  name: {type: String, required: true},
  type: String,
  start_date: {type: String, required: true},
  organization_id: String,
  identifiers: IdentifierSchema.schema
}, {collection: 'Events'});

module.exports = mongoose.model('Events', EventsSchema);
