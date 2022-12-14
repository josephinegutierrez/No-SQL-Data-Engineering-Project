'use strict'

var mongoose = require('mongoose');

var Media = new mongoose.Schema({
  name: {type: String, required: true},
  type: {type: String, required: true},
  url: {type: String, required: true}
}, { versionKey: false, _id : false});


module.exports = Media;
