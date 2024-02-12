const mongoose = require('mongoose')

var plantSchema = new mongoose.Schema({
    _id: Number,
    NumeroDeRegisto: Number,
    CodigoDeRua: Number,
    Rua: String,
    Local: String,
    Freguesia: String,
    Especie: String,
    NomeCientifico: String,
    Origem: String,
    DataDePlantacao: String,
    Estado: String,
    Caldeira: String,
    Tutor: String,
    Implantacao: String,
    Gestor: String,
    DataDeActualizacao: String,
    NumeroDeIntervencoes: Number
});

module.exports = mongoose.model('planta', plantSchema)