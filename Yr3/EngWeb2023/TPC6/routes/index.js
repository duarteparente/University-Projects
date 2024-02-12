var express = require('express');
var router = express.Router();
var Pessoa = require('../controllers/person')

/* GET home page. */
router.get('/pessoas', function(req, res) {
  Pessoa.list()
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(520).json({erro: erro, mensagem: "Não consegui obter a lista de pessoas"}))
});

router.get('/pessoas/:id', function(req, res) {
  Pessoa.getExame(req.params.id)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(521).json({erro: erro, mensagem: "Não consegui obter o registo da pessoa"}))
});

router.post('/pessoas', function(req, res) {
  Pessoa.addPessoa(req.body)
    .then(dados => res.status(201).json(dados))
    .catch(erro => res.status(522).json({erro: erro, mensagem: "Não consegui inserir o registo da pessoa"}))
});

router.put('/pessoas/:id', (req,res) => {
  Pessoa.updatePessoa(req.body)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(523).json({erro: erro, mensagem: "Não consegui alterar o registo da pessoa"}))
});

router.delete('/pessoas/:id', (req,res) => {
  Pessoa.deletePessoa(req.params.id)
    .then(dados => res.status(200).json(dados))
    .catch(erro => res.status(524).json({erro: erro, mensagem: "Não consegui eliminar o registo da pessoa"}))

})
  

module.exports = router;
