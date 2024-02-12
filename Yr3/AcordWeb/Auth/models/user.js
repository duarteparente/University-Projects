const mongoose = require('mongoose')
var passportLocalMongoose = require('passport-local-mongoose');

var favSchema = new mongoose.Schema({
  Processo: String,
  Descricao: String
})

var userSchema = new mongoose.Schema({
    username: String,
    name: String,
    email: String,
    idade: Number,
    profissao: String,
    level: String,
    active: Boolean,
    dateCreated: String,
    lastAccess: String,
    localidade: String,
    image: String,
    favs: [favSchema],
    added: [String],
    edited: [String]
  });

userSchema.plugin(passportLocalMongoose);

module.exports = mongoose.model('user', userSchema)