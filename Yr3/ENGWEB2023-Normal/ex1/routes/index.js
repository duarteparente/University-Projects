var express = require('express');
var router = express.Router();
var Plant = require('../controllers/planta')


router.get('/plantas', function(req, res) {
  // GET /plantas?especie=EEEE
  if (req.query.especie != undefined){
    Plant.getPlantEspecie(req.query.especie)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(521).json({erro: erro, mensagem: "Erro"}))
  }
  // GET /plantas?implant=AAA
  else if (req.query.implant != undefined){
    Plant.getPlantImplants(req.query.implant)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(521).json({erro: erro, mensagem: "Erro"}))
  }
  // GET /plantas
  else{
    Plant.list()
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(520).json({erro: erro, mensagem: "Erro"}))
  }
})

// GET /plantas/freguesias
router.get('/plantas/freguesias', function(req, res){
  Plant.getFreguesias()
  .then(dados => res.status(200).json(dados))
  .catch(erro => res.status(521).json({erro: erro, mensagem: "Erro"}))
})

// GET /plantas/especies
router.get('/plantas/especies', function(req, res){
  Plant.getSpecies()
  .then(dados => res.status(200).json(dados))
  .catch(erro => res.status(521).json({erro: erro, mensagem: "Erro"}))
})

// GET /plantas/:id
router.get('/plantas/:id', function(req, res){
  Plant.getPlant(req.params.id)
  .then(dados => res.status(200).json(dados))
  .catch(erro => res.status(521).json({erro: erro, mensagem: "Erro"}))
})

// POST /plantas: acrescenta um registo novo à BD
router.post('/plantas', function(req, res) {
  Plant.addPlant(req.body)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(520).json({erro: erro, mensagem: "Não consegui adicionar a planta!"}))
});


// DELETE /plantas/:id: elimina da BD o registo com o identificador id
router.delete('/plantas/:id', function(req, res) {
  Plant.deletePlant(req.params.id)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(520).json({erro: erro, mensagem: "Não consegui apagar a planta!"}))
});

module.exports = router;