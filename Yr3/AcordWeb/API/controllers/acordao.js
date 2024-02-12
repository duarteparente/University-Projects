var Acordao = require('../models/acordao')


module.exports.count = () => {
    return Acordao
        .count()
        .then(resposta => {
            return resposta
        })
        .catch(erro => {
            return erro
        })
}


module.exports.getAcordaoProcesso = processo => {
    return Acordao.findOne({Processo: processo})
        .then(resposta => {
            return resposta
        })
        .catch(erro => {
            return erro
        })
}


module.exports.getAcordaosTribunal = tribunal => {
    return Acordao.find({tribunal: tribunal})
        .then(resposta => {
            return resposta
        })
        .catch(erro => {
            return erro
        })
}


module.exports.getSearchAcordaosTribunal = (tribunal, Descritor, DataAcordao) => {
    if (Descritor == '' && DataAcordao == ''){
        return Acordao.find({tribunal: tribunal}, {_id: 0, Processo: 1, url: 1})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
    }

    else if (Descritor == ''){
        return Acordao.find({tribunal: tribunal, DataAcordao: { $exists: true, $eq: DataAcordao } }, {_id: 0, Processo: 1, url: 1})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
    }

    else if (DataAcordao == ''){
        const regex = new RegExp(Descritor, 'i');
        return Acordao.find({tribunal: tribunal, Descritores: { $in: [ regex ]}}, {_id: 0, Processo: 1, url: 1})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
    }

    else{
        const regex = new RegExp(Descritor, 'i');
        return Acordao.find({tribunal: tribunal, Descritores: { $in: [ regex ] }, DataAcordao: { $exists: true, $eq: DataAcordao }}, {_id: 0, Processo: 1, url: 1})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
    }
}


module.exports.updateAcordao = (id, a) => {
    return Acordao.updateOne({ _id: id }, a)
        .then(() => {
            return Acordao.findOne({_id: id})
        })
        .catch(erro => {
            return erro
        })
}


module.exports.createAcordao = a => {
    return Acordao.findOne({ Processo: a.Processo })
        .then(proc => {
            if (proc) {
                return {}
            } 
            else {
                return Acordao.create(a)
            }
        })
        .catch(erro => {
            return erro
        })
}


module.exports.deleteAcordao = id => {
    return Acordao.deleteOne({_id:id})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}