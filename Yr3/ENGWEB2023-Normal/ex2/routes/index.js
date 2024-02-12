var express = require('express');
var router = express.Router();
var axios = require('axios');

/* GET home page. */
router.get('/', function(req, res, next) {
  axios.get('http://localhost:15030/plantas')
  .then(response => {
    res.render('main', {plantas: response.data});
  })
  .catch(err => {
    res.render('error', {error: err})
  })
});

router.get('/especies/:id', function(req, res, next) {
  axios.get('http://localhost:15030/plantas?especie=' + req.params.id)
  .then(response => {
    res.render('especie', {plantas: response.data});
  })
  .catch(err => {
    res.render('error', {error: err})
  })
});

router.get('/:id', function(req, res, next) {
  axios.get('http://localhost:15030/plantas/' + req.params.id)
  .then(response => {
    res.render('planta', {c: response.data});
  })
  .catch(err => {
    res.render('error', {error: err})
  })
});

module.exports = router;
