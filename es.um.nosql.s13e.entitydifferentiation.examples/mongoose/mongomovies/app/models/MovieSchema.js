'use strict'

var mongoose = require('mongoose');
var RatingSchema = require('./RatingSchema.js');
var PrizeSchema = require('./PrizeSchema.js');
var CriticismSchema = require('./CriticismSchema.js');
var UnionType = require('./util/UnionType.js');

var MovieSchema = new mongoose.Schema({
  _id: {type: String, required: true},
  criticisms: {type: [CriticismSchema.schema], default: undefined},
  director_id: {type: String, ref: "Director", required: true},
  genre: String,
  genres: {type: [String], default: undefined},
  prizes: {type: [PrizeSchema.schema], default: undefined},
  rating: RatingSchema.schema,
  running_time: Number,
  title: {type: String, required: true},
  writers: {type: [String], default: undefined},
  year: {type: Number, required: true}
}, { versionKey: false, collection: 'movie'});


module.exports = mongoose.model('Movie', MovieSchema);