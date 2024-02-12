var express = require('express');
var router = express.Router();
var axios = require('axios')
var Acordao = require('../controllers/acordao')


router.get('/acordaos', function(req, res) {
  if (req.query.processo != undefined){
    Acordao.getAcordaoProcesso(req.query.processo)
      .then(dados => res.status(200).json(dados))
      .catch(erro => res.status(520).json({erro: erro, mensagem: "Não consegui obter o acórdão!"}))
  }

  else {
    Acordao.count()
      .then(dados => res.status(200).json(dados))
      .catch(erro => res.status(520).json({erro: erro, mensagem: "Não consegui obter a contagem de acórdãos!"}))
  }
});


router.get('/acordaos/tribunal', function(req, res) {
  Acordao.getSearchAcordaosTribunal(req.query.tribunal, decodeURI(req.query.Descritor), req.query.DataAcordao)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(520).json({erro: erro, mensagem: "Não consegui obter a lista de acórdãos pertencentes a essa instituição!"}))
});


router.put('/acordaos/editar/:id', function(req, res) {
  var username = req.query.usr
  var token = req.query.tkn
  axios.post('http://localhost:22230/users/' + username + '/editarRegisto', {proc: req.query.proc, tkn: token})
  .then(() => {
    Acordao.updateAcordao(req.params.id, req.body)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(520).json({erro: erro, mensagem: "Não consegui atualizar o acórdão pretendido!"}))
  })
  .catch(error => {
    res.render('error', {err: error, message: 'ERROR'})
  })

});


router.post('/acordaos', function(req,res) {
  var username = req.query.usr
  var token = req.query.tkn
  axios.post('http://localhost:22230/users/' + username + '/adicionarRegisto', {proc: req.body.Processo, tkn: token})
  .then(() => {
    Acordao.createAcordao(req.body)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(520).json({erro: erro, mensagem: "Não consegui adicionar um novo acórdão!"}))
    
  })
  .catch(error => {
    res.render('error', {err: error, message: 'ERROR'})
  })
})


router.delete('/acordaos/:id', function(req,res) {
  Acordao.deleteAcordao(req.params.id)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(520).json({erro: erro, mensagem: "Não consegui eliminar o acórdão pretendido!"}))
})


module.exports = router;