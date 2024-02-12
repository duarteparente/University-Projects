var Plant = require('../models/planta')


// GET /plantas: devolve uma lista com todos os registos;
module.exports.list = () => {
    return Plant
            .find()
            .sort({Freguesia: -1})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}


// GET /plantas/:id: devolve o registo com identificador id;
module.exports.getPlant = id => {
    return Plant.findOne({_id:id})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}


// GET /plantas?especie=EEEE: devolve a lista dos registos correspondentes à espécie EEEE;
module.exports.getPlantEspecie = esp => {
    return Plant.find({Especie: esp})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}

// GET /plantas?implant=AAA: devolve a lista dos registos com implantação AAA;
module.exports.getPlantImplants = AAA => {
    return Plant.find({Implantacao: AAA})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}

// GET /plantas/freguesias: devolve a lista de freguesias ordenada alfabeticamente e sem repetições;
module.exports.getFreguesias = () => {
    return Plant.distinct('Freguesia')
            .sort()
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}

// GET /plantas/especies: devolve a lista das espécies vegetais ordenada alfabeticamente e sem repetições;
module.exports.getSpecies = () => {
    return Plant.distinct('Especie')
            .sort()
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}


// POST /plantas: acrescenta um registo novo à BD;
module.exports.addPlant = p => {
    return Plant.create(p)
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}

// DELETE /plantas/:id: elimina da BD o registo com o identificador id.
module.exports.deletePlant = id => {
    return Plant.deleteOne({_id: id})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}