var express = require('express');
var router = express.Router();
var jwt = require('jsonwebtoken')
var passport = require('passport')
var userModel = require('../models/user')
var auth = require('../auth/auth')

var User = require('../controllers/user')

router.get('/:usr/perfil', auth.verificaAcesso, function(req, res){
  var user = req.params.usr
  User.getUser(user)
    .then(dados => res.status(200).jsonp({dados}))
    .catch(e => res.status(500).jsonp({error: e}))
})

router.get('/:usr/favoritos', auth.verificaAcesso, function(req, res){
  var user = req.params.usr
  User.getUser(user)
    .then(dados => res.status(200).jsonp({dados: dados.favs}))
    .catch(e => res.status(500).jsonp({error: e}))
})

router.get('/:usr/adicionados', auth.verificaAcesso, function(req, res){
  var user = req.params.usr
  User.getUser(user)
    .then(dados => res.status(200).jsonp({dados: dados.added}))
    .catch(e => res.status(500).jsonp({error: e}))
})

router.get('/:usr/editados', auth.verificaAcesso, function(req, res){
  var user = req.params.usr
  User.getUser(user)
    .then(dados => res.status(200).jsonp({dados: dados.edited}))
    .catch(e => res.status(500).jsonp({error: e}))
})

router.post('/registo', function(req, res) {
  var d = new Date().toUTCString().substring(0,26)
  userModel.register(
    new userModel({
      username: req.body.username,
      name: req.body.name, 
      email: req.body.email,
      idade: req.body.idade,
      profissao: req.body.profissao,
      level: req.body.level,
      active: true, 
      dateCreated: d,
      lastAccess: d,
      localidade: req.body.localidade,
      image: "justice.jpeg"
    }), 
    req.body.password, 
    function(err, user) {
      if (err){
        res.status(500).jsonp({error: "Erro no registo: " + err})
      } else {
          passport.authenticate("local")(req,res,function(){
            jwt.sign(
              { username: req.user.username, level: req.user.level, sub: 'login'}, 
              "BasesJuridicas",
              {expiresIn: 3600},
              function(e, token) {
                if(e) res.status(500).jsonp({error: "Erro na geração do token: " + e}) 
                else {
                  res.status(201).jsonp({token: token, user:{username: req.user.username, level: req.user.level, image: req.user.image}})
                }
              });
  
          })
      }     
  })
})

router.post('/:usr/editarRegisto', auth.verificaAcesso, function(req, res) {
  User.addEdit(req.params.usr, req.body.proc)
    .then(dados => res.status(201).jsonp({dados: dados}))
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na edição de registo"})
    })
})


router.post('/:usr/adicionarRegisto', auth.verificaAcesso, function(req, res) {
  User.addRegisto(req.params.usr, req.body.proc)
    .then(dados => res.status(201).jsonp({dados: dados}))
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na adição de registo"})
    })
})

router.post('/:usr/fav', auth.verificaAcesso, function(req, res) {
  User.addFav(req.params.usr, req.body)
    .then(dados => res.status(201).jsonp({dados: dados}))
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração de favoritos do utilizador"})
    })
})

router.post('/:usr/elimina', auth.verificaAcesso, function(req, res) {
  User.elimina(req.params.usr, req.body)
    .then(dados => res.status(201).jsonp({dados: dados}))
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração de favoritos do utilizador"})
    })
})

router.post('/:usr/editarFoto', auth.verificaAcesso, function(req, res) {
  User.updateFoto(req.params.usr, req.body.photo)
    .then(dados => res.status(201).jsonp({dados: dados}))
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração da foto de perfil do utilizador"})
    })
})


// POST /users/:usr/login  -  Efetua o login do utilizador
router.post('/:usr/login', passport.authenticate('local'), function(req, res){
  var d = new Date().toUTCString().substring(0,26)
  jwt.sign(
    { username: req.params.usr, level: req.user.level, sub: 'login'}, 
    "BasesJuridicas",
    {expiresIn: 3600},
    function(e, token) {
      if(e) res.status(500).jsonp({error: "Erro na geração do token: " + e}) 
      else {    
        User.userLogin(req.params.usr, d)
        .then(() => {
          res.status(201).jsonp({token: token, user:{username: req.user.username, level: req.user.level, image: req.user.image}})
        })
        .catch(erro => {
          res.render('error', {error: erro, message: "Erro na alteração do utilizador"})
        })
      }
  });
})

router.put('/:id', auth.verificaAcesso, function(req, res) {
  User.updateUser(req.params.id, req.body)
    .then(dados => {
      res.jsonp(dados)
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração do utilizador"})
    })
})


// PUT /users/:usr/logout  -  Efetua o logout do utilizador
router.put('/:usr/logout', auth.verificaAcesso, function(req, res) {
  User.userLogout(req.params.usr)
    .then(dados => {
      res.jsonp(dados)
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração do utilizador"})
    })
})

router.put('/:usr/editarPerfil', auth.verificaAcesso, function(req, res) {
  User.updateUser(req.params.usr, req.body.dados)
    .then(() => {
      User.getUser(req.params.usr)
      .then(dados => {
        res.status(201).jsonp({user:{username: dados.username, level: dados.level, image: dados.image}})
      })
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração do utilizador"})
    })
})

router.put('/:id/ativar', auth.verificaAcesso, function(req, res) {
  User.updateUserStatus(req.params.id, true)
    .then(dados => {
      res.jsonp(dados)
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração do utilizador"})
    })
})

router.put('/:id/password', auth.verificaAcesso, function(req, res) {
  User.updateUserPassword(req.params.id, req.body)
    .then(dados => {
      res.jsonp(dados)
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração do utilizador"})
    })
})

router.delete('/:usr/eliminarConta', auth.verificaAcesso, function(req, res) {
  User.deleteUser(req.params.usr)
    .then(dados => {
      res.jsonp(dados)
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na remoção do utilizador"})
    })
})

module.exports = router;
