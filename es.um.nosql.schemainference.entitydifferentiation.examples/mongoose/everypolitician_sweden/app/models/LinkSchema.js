'use strict'

var mongoose = require('mongoose');

var LinkSchema = new mongoose.Schema({
  note: {type: String, required: true},
  url: {type: String, required: true}
}, { versionKey: false, _id : false});


module.exports = mongoose.model('Link', LinkSchema);
