'use strict'

var mongoose = require('mongoose');

var DirectorSchema = new mongoose.Schema({
  _id: {type: String, required: true},
  _type: {type: , required: true},
  actor_movies: {type: [String], ref: "Movie", default: undefined},
  directed_movies: {type: [String], ref: "Movie", default: undefined},
  name: {type: String, required: true}
}, { versionKey: false, collection: 'director'});


module.exports = mongoose.model('Director', DirectorSchema);
