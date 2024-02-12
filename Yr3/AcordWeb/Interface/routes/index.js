var express = require('express');
var router = express.Router();
var axios = require('axios')
var multer = require('multer')
var fs = require('fs')
var user, admin
var logged = false
var dict = {
  Processo: "Processo",
  url: "url",
  tribunal: "tribunal",
  Descritores: "Descritores",
  Relator: "Relator",
  Privacidade: "Privacidade",
  Votacao: "Votação",
  NConvencional: "Número Convencional",
  Data: "Data",
  DataAcordao: "Data do Acórdão",
  Acordao: "Acordão",
  NDocumento: "Número do Documento",
  Especie: "Espécie",
  Requerente: "Requerente",
  Requerido: "Requerido",
  NormasApreciadas: "Normas Apreciadas",
  NormasJulgadasInconst: "Normas Julgadas Inconstitucionais",
  AreaTematica1: "Área Temática 1",
  AreaTematica2: "Área Temática 2",
  Decisao: "Decisão",
  Sumario: "Sumário",
  TextoIntegral: "Texto Integral",
  VolumeAcordaosTC: "Volume dos Acordãos do Tribunal Constitucional",
  Constituicao: "Constituição",
  LegislacaoNacional: "Legislação Nacional",
  NDiarioRepublica: "Número do Diário da República",
  SerieDiarioRepublica: "Série do Diário da República",
  DataDiarioRepublica: "Data do Diário da República",
  PaginaDiarioRepublica: "Página do Diário da República",
  NBoletimMJ: "Nº do Boletim do Ministério da Justiça",
  PaginaBoletimMJ: "Página do Boletim do Ministério da Justiça",
  JurisprudenciaConstitucional: "Jurisprudência Constitucional",
  NormasSuscitadas: "Normas Suscitadas",
  DeclaracaoVoto: "Declaração de Voto",
  VotoVencido: "Voto Vencido",
  IndicacoesEventuais: "Indicações Eventuais",
  NormasDeclaradasInconst: "Normas Declaradas Inconstitucionais",
  PaginaVolume: "Página do Volume",
  OutrasPublicacoes: "Outras Publicações",
  LegislacaoComunitaria: "Legislação Comunitária",
  ReferenciaPareceres: "Referência a Pareceres",
  ReferenciasInternacionais: "Referências Internacionais",
  OutraJurisprudencia: "Outra Jurisprudência",
  JurisprudenciaInternacional: "Jurisprudência Internacional",
  JurisprudenciaEstrangeira: "Jurisprudência Estrangeira",
  LegislacaoEstrangeira: "Legislação Estrangeira",
  Tribunal: "Tribunal",
  Recorrente: "Recorrente",
  Recorrido1: "Recorrido 1",
  Recorrido2: "Recorrido 2",
  DataEntrada: "Data de Entrada",
  MeioProcessual: "Meio Processual",
  Objecto: "Objecto",
  ReferenciaDoutrina: "Referência a Doutrina",
  JurisprudenciaNacional: "Jurisprudência Nacional",
  NVolume: "Número do Volume",
  RecusaAplicacao: "Recusa Aplicação",
  AnoPublicacao: "Ano de Publicação",
  Apendice: "Apêndice",
  DataApendice: "Data do Apêndice",
  PPagPublicacaoAcordao: "1ª Página de Publicação do Acordão",
  ReferenciaPublicacao1: "Referência de Publicação 1",
  Tribunal1instancia: "Tribunal de 1ª Instância",
  JuizoSeccao: "Juízo ou Secção",
  TipoAcao: "Tipo de Ação",
  TipoContrato: "Tipo de Contrato",
  Autor: "Autor",
  Reu: "Réu",
  DataDecisao: "Data da Decisão",
  TextoClausulasAbusivas: "Texto das Cláusulas Abusivas",
  Recursos: "Recursos",
  Pagina: "Página",
  ReferenciaPublicacao2: "Referência de Publicação 2",
  DecisaoTextoIntegral: "Decisão Texto Integral",
  Apenso: "Apenso",
  ReferenciaPublicacao: "Referência de Publicação",
  DataDecisaoSumaria: "Data da Decisão Sumária",
  NUnicoProcesso: "Número Único do Processo",
  TribunalRecurso: "Tribunal Recurso",
  Doutrina: "Doutrina",
  AreaTematica: "Área Temática",
  DataDecisaoSingular: "Data da Decisão Singular",
  DataReclamacao: "Data da Reclamação",
  ReferenciaProcesso: "Referência do Processo",
  ProcessoTribunalRecurso: "Processo no Tribunal Recurso",
  Recurso: "Recurso",
  Seccao: "Secção",
  Contencioso: "Contencioso",
  NProcessoTAF: "Número do Processo/TAF",
  SubSeccao: "Sub-Secção",
  Magistrado: "Magistrado",
  Observacoes: "Observações",
  DisponivelJTCA: "Disponível na JTCA",
  PecaProcessual: "Peça Processual",
  Tema: "Tema",
  ParecerMinisterioPublico: "Parecer do Ministério Publico",
  TribunalRecorrido: "Tribunal Recorrido",
  ProcessoTribunalRecorrido: "Processo no Tribunal Recorrido",
  TextoParcial: "Texto Parcial",
  Estrangeiro1: "Estrangeiro 1",
  Reclamacoes: "Reclamações",
  DataDecRecorrida: "Data Dec. Recorrida"
}

var tribs = {
  atco1: "Acórdãos do Tribunal Constitucional",
  jcon: "Acórdãos do Tribunal dos Conflitos",
  jdgpj: "Cláusulas Abusivas julgadas pelos Tribunais",
  jsta: "Acórdãos do Supremo Tribunal Administrativo",
  jstj: "Acórdãos do Supremo Tribunal de Justiça",
  jtca: "Acórdãos do Tribunal Central Administrativo Sul",
  jtcampca: "Pareceres do MP do Tribunal Central Administrativo Sul - Contencioso Administrativo",
  jtcampct: "Pareceres do MP do Tribunal Central Administrativo Sul - Contencioso Tributário",
  jtcn: "Acórdãos do Tribunal Central Administrativo Norte",
  jtrc: "Acórdãos do Tribunal da Relação de Coimbra",
  jtre: "Acórdãos do Tribunal da Relação de Évora",
  jtrg: "Acórdãos do Tribunal da Relação de Guimarães",
  jtrl: "Acórdãos do Tribunal da Relação de Lisboa",
  jtrp: "Acórdãos do Tribunal da Relação do Porto"
}


var upload = multer({dest: 'uploads'})


router.get('/', function(req, res){
  if(logged){
    (user.level == 'admin') ? admin = true : admin = false
    res.render('main-page', {log: true, adm: admin, username: user.username, image: user.image})
  }
  else{
    res.render('main-page', {log: false})
  }
})


router.get('/tribunal', function(req, res, next) {
  axios.get('http://localhost:22231/acordaos' + req.url)
    .then(resposta => {
      if (resposta.data.length == 0){
        res.render('not-found');
      }
      else {
        var tribunal = tribs[req.query.tribunal]
        if(logged){
          (user.level == 'admin') ? admin = true : admin = false
          res.render('tribunal-page', {log: true, adm: admin, username: user.username, lvl: user.level, ft: tribunal, acs: resposta.data ,image: user.image})
        }
        else{
          res.render('tribunal-page', {log: false, ft: tribunal, acs: resposta.data })
        }
      }
    })
    .catch(error => {
      res.render('error', {err: error, message: 'ERROR'})
    })
});


router.get('/processo', function(req, res, next) {
  var d = new Date().toUTCString().substring(0,26)
  axios.get('http://localhost:22231/acordaos?processo=' + req.query.proc)
    .then(resposta => {
      if (resposta.data == null){
        res.render('not-found');
      }
      else {
        if(logged){
          var token = req.cookies[user.username];
          (user.level == 'admin') ? admin = true : admin = false
          axios.get("http://localhost:22230/users/" + user.username + "/perfil?tkn=" + token)
            .then(response => {
              res.render('acordao-page', {date: d, log: true, adm: admin, username: user.username, favs: response.data.dados.favs, lvl: user.level, d: dict, a: resposta.data, image: user.image})
            })
        }
        else{
          res.render('acordao-page', {log: false, d: dict, a: resposta.data })
        }
      }
    })
    .catch(error => {
      res.render('error', {err: error, message: 'ERROR'})
    })
});


router.get('/perfil', function(req, res, next) {
  if(logged){
    var token = req.cookies[user.username];
    (user.level == 'admin') ? admin = true : admin = false
    axios.get('http://localhost:22230/users/' + user.username + '/perfil?tkn=' + token)
      .then(response => {
        (user.level == 'admin') ? admin = true : admin = false
        res.render('profile', {profile: response.data.dados, log: true,  adm: admin, image: response.data.dados.image, username: user.username, mine: true})
      })
      .catch(e => {
        res.render('error', {err: error, message: 'ERROR'})
      })
  }
  else{
    res.redirect('/login');
  }
});



router.get('/:usr/perfil', function(req, res, next) {
  if(logged){
    if(req.params.usr == user.username){
      res.redirect('/perfil')
    }
    else{
      var token = req.cookies[user.username];
      axios.get('http://localhost:22230/users/' + req.params.usr + '/perfil?tkn=' + token)
        .then(response => {
          (response.data.dados.level == 'admin') ? admin = true : admin = false
          res.render('profile', {profile: response.data.dados, log: true,  adm: admin, image: user.image, username: user.username, mine: false})
        })
        .catch(e => {
          res.render('error', {err: error, message: 'ERROR'} )
        })

      }
  }
  else{
    res.redirect('/login');
  }
});


router.get('/:usr/favoritos', function(req, res, next) {
  if(logged){
    var token = req.cookies[user.username];
    (user.level == 'admin') ? admin = true : admin = false
    axios.get("http://localhost:22230/users/" + req.params.usr + '/favoritos?tkn=' + token)
      .then(response => {
        res.render('favs', {log: true, adm: admin, username: user.username, favs: response.data.dados, image: user.image})
      })
      .catch(e => {
        res.render('error', {err: error, message: 'ERROR'})
      })
  }
  else{
    res.redirect('/login');
  }
});


router.get('/:usr/adicionados', function(req, res, next) {
  if(logged){
    var token = req.cookies[user.username];
    (user.level == 'admin') ? admin = true : admin = false
    axios.get("http://localhost:22230/users/" + req.params.usr + '/adicionados?tkn=' + token)
      .then(response => {
        res.render('meusAcordaos', {edit: false, log: true, adm: admin, username: user.username, meus: response.data.dados, image: user.image})
      })
      .catch(e => {
        res.render('error', {err: error, message: 'ERROR'})
      })
  }
  else{
    res.redirect('/login');
  }
});


router.get('/:usr/editados', function(req, res, next) {
  if(logged){
    var token = req.cookies[user.username];
    (user.level == 'admin') ? admin = true : admin = false
    axios.get("http://localhost:22230/users/" + req.params.usr + '/editados?tkn=' + token)
      .then(response => {
        res.render('meusAcordaos', {edit: true, log: true, adm: admin, username: user.username, meus: response.data.dados, image: user.image})
      })
      .catch(e => {
        res.render('error', {err: error, message: 'ERROR'})
      })
  }
  else{
    res.redirect('/login');
  }
});


router.post('/editarFoto', upload.single('profilePicture'), function(req, res, next) {
  if(logged){
    var token = req.cookies[user.username];
    let oldPath = __dirname + '/../' + req.file.path
    let newPath = __dirname + '/../public/profile/' + req.file.originalname
  
    fs.rename(oldPath, newPath, error => {
      if(error) throw error
    })

    axios.post("http://localhost:22230/users/" + user.username + '/editarFoto', {tkn: token, photo: req.file.originalname})
      .then(() => {
        user.image = req.file.originalname
        res.redirect('/perfil')
      })
      .catch(e => {
        res.render('error', {err: error, message: 'ERROR'})
      })
  }
  else{
    res.redirect('/login');
  }
});


router.get('/editarPerfil', function(req, res, next) {
  if(logged){
    var token = req.cookies[user.username];
    (user.level == 'admin') ? admin = true : admin = false
    axios.get("http://localhost:22230/users/" + user.username + "/perfil?tkn=" + token)
      .then(response => {
        (user.level == 'admin') ? admin = true : admin = false
        res.render('editProfile', {profile: response.data.dados, adm: admin, image: response.data.dados.image})
      })
      .catch(e => {
        res.render('error', {err: error, message: 'ERROR'})
      })
  }
  else{
    res.redirect('/login');
  }
});


router.post('/editarPerfil', function(req, res, next) {
  if(logged){
    var token = req.cookies[user.username];
    (user.level == 'admin') ? admin = true : admin = false
    axios.put("http://localhost:22230/users/" + user.username + '/editarPerfil', {tkn: token, dados: req.body})
      .then(response => {
        user = response.data.user
        res.redirect('/perfil')
      })
      .catch(e => {
        res.render('error', {err: error, message: 'ERROR'})
      })
  }
  else{
    res.redirect('/login');
  }
});


router.get('/adicionar', function(req, res, next) {
  var d = new Date().toUTCString().substring(0,26)
  if(logged){
    (user.level == 'admin') ? admin = true : admin = false
    if (admin == true){
      res.render('addAc-page', {date: d, log: true, adm: admin, username: user.username, lvl: user.level, d: dict, msg: 'OK', image: user.image})
    }
    else {
      res.redirect('/');
    }
  }
  else{
    res.redirect('/login');
  }
});


router.get('/processo/elimina/:Processo', function(req, res, next) {
      if(logged){
        var token = req.cookies[user.username];
        axios.post('http://localhost:22230/users/' + user.username + '/elimina', {tkn: token, Processo: req.params.Processo, Descricao: ""})
        .then(() => {
          res.redirect('/processo?proc=' + req.params.Processo)
        })
        .catch(error => {
          res.render('error', {err: error, message: 'ERROR'})
        })
      }
      
      else {
        res.redirect('/login');
      }
});


router.post('/processo/editar', function(req, res, next) {
      if(logged){
        var token = req.cookies[user.username];
        (user.level == 'admin') ? admin = true : admin = false
        if (admin == true) {
          axios.put('http://localhost:22231/acordaos/editar/' + req.query.id + '?usr=' + user.username + '&tkn=' + token + '&proc=' + req.query.Processo, req.body)
            .then(() => {
              res.redirect('/processo?proc=' + req.query.Processo)
            })
            .catch(error => {
              res.render('error', {err: error, message: 'ERROR'})
            })
        }
        else {
          res.redirect('/');
        }      
      }
      else{
        res.redirect('/login');
      }
});


router.post('/processo/eliminar', function(req, res, next) {
      if(logged){
        var token = req.cookies[user.username];
        (user.level == 'admin') ? admin = true : admin = false
        if (admin == true) {
          axios.post('http://localhost:22230/users/' + user.username + '/elimina', {tkn: token, Processo: req.query.Processo, Descricao: ""})
            .then(() => {
              axios.delete('http://localhost:22231/acordaos/' + req.query.id)
                .then(() => {
                  res.redirect('/');
                })
                .catch(error => {
                  res.render('error', {err: error, message: 'ERROR'})
                }) 
            })
            .catch(error => {
              res.render('error', {err: error, message: 'ERROR'})
          })
        }
        else {
          res.redirect('/');
        }
      }
      
      else {
        res.redirect('/login');
      }
});


router.post('/adicionar', function(req, res, next) {
      if(logged){
        var token = req.cookies[user.username];
        (user.level == 'admin') ? admin = true : admin = false
        if (admin == true) {
          const filtered = Object.fromEntries(Object.entries(req.body).filter(([key, value]) => value !== ''));
          if ("Descritores" in filtered)
            filtered["Descritores"] = filtered["Descritores"].split(",")
          axios.post('http://localhost:22231/acordaos?usr=' + user.username + "&tkn=" + token, filtered)
            .then(resposta => {
              if (Object.keys(resposta.data).length == 0)
                res.render('addAc-page', {log: true, adm: admin, username: user.username, lvl: user.level, d: dict, msg: 'NO', image: user.image})
              else res.redirect('/processo?proc=' + resposta.data.Processo)
            })
            .catch(error => {
              res.render('error', {err: error, message: 'ERROR'})
            })
        }
        else {
          res.redirect('/');
        }      
      }
      else{
        res.redirect('/login');
      }
});


router.post('/processo/fav/:Processo', function(req, res, next) {
      if(logged){
        var token = req.cookies[user.username];
        axios.post('http://localhost:22230/users/' + user.username + '/fav', {tkn: token, Processo: req.params.Processo, Descricao: req.body.Descricao})
        .then(() => {
          res.redirect("/processo?proc=" + req.params.Processo);
        })
        .catch(error => {
          res.render('error', {err: error, message: 'ERROR'})
        })
      }
      
      else {
        res.redirect('/login');
      }
});


router.get('/eliminarConta', function(req, res, next) {
  if(logged){
    var token = req.cookies[user.username];
    
    axios.delete("http://localhost:22230/users/" + user.username + '/eliminarConta?tkn=' + token)
    .then(() => {
      logged = false
      res.redirect('/')
    })
    .catch(e => {
      res.render('error', {err: error, message: 'ERROR'})
    })
  }
  
  else{
    res.redirect('/login');
  }
});


// LOGIN
router.get('/login', function(req, res){
  res.render('loginForm', {message: 'OK'})
})


router.post('/login', function(req, res){
  axios.post('http://localhost:22230/users/' + req.body.username + '/login', req.body)
    .then(response => {
      logged = true
      res.cookie(req.body.username, response.data.token)
      user = response.data.user
      res.redirect('/');
    })
    .catch(e =>{
      logged = false
      res.render('loginForm', {message: "Credenciais inválidas. Tente outra vez!"})
    })
})


// REGISTER
router.get('/registo', function(req, res){
  res.render('registerForm', {message: 'OK'})
})


router.post('/registo', function(req, res){
  axios.post('http://localhost:22230/users/registo', req.body)
    .then(response => {
      logged = true
      res.cookie(req.body.username, response.data.token)
      user = response.data.user
      res.redirect('/');
    })
    .catch(e =>{
      logged = false
      res.render('registerForm', {message: 'Username já existente!'})
    })
})


// LOGOUT
router.get('/logout', function(req, res){
  var token = req.cookies[user.username];
  axios.put('http://localhost:22230/users/' + user.username + '/logout', { tkn: token })
  .then(() => {
    logged = false
    res.clearCookie('token');
    res.redirect('/');
  })
  .catch(e =>{
    res.render('error', {err: error, message: 'ERROR'})
  })
})


module.exports = router;