var express = require('express');
var router = express.Router();
var Tasks = require('../controllers/tasks')

/* =============   GET   ============== */

/* GET home page. */
router.get('/', function(req, res, next) {
  var data = new Date().toISOString().substring(0, 16)
  Tasks.list()
  .then(tasks => {
    res.render('index', { t: tasks, d: data });
  })
  .catch(erro => {
    res.render('error', {error: erro, message: "Erro na obtenção da lista de tarefas"})
  })
});

/* Edit Task */
router.get('/tasks/edit/:idTask', function(req, res, next){
  var data = new Date().toISOString().substring(0, 16)
  Tasks.getTask(req.params.idTask)
  .then(task => {
    res.render('editTask', { t: task, d: data });
  })
  .catch(erro => {
    res.render('error', {error: erro, message: "Erro na alteração do registo da tarefa"})
  })
})


/* Task Done. */
router.get('/tasks/done/:idTask', function(req, res, next){
  Tasks.getTask(req.params.idTask)
  .then(task => {
    task.done = 1
    Tasks.updateTask(task)
    .then(() =>{
      res.redirect('/')
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração do registo da tarefa"})
    })
  })
  .catch(erro => {
    res.render('error', {error: erro, message: "Erro na obtenção do registo da tarefa"})
  })
})

/* Undo Task. */
router.get('/tasks/undo/:idTask', function(req, res, next){
  Tasks.getTask(req.params.idTask)
  .then(task => {
    task.done = 0
    Tasks.updateTask(task)
    .then(() =>{
      res.redirect('/')
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração do registo da tarefa"})
    })
  })
  .catch(erro => {
    res.render('error', {error: erro, message: "Erro na obtenção do registo da tarefa"})
  })
})

/* Delete Task. */
router.get('/tasks/delete/:idTask', function(req, res, next){
  Tasks.deleteTask(req.params.idTask)
  .then(() => {
    res.redirect('/')
  })
  .catch(erro => {
    res.render('error', {error: erro, message: "Erro a apagar o registo da tarefa"})
  })
})


/* =============   POST   ============== */
router.post('/tasks/register', (req, res) => {
  var task = req.body
  task.done = 0
  Tasks.addTask(task)
    .then(() => {
      res.redirect('/')
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro no armazenamento do registo da tarefa"})
    })
})

router.post('/tasks/edit/:idTask', (req, res) => {
  var task = req.body
  task.done = 0
  Tasks.updateTask(task)
    .then(() => {
      res.redirect('/')
    })
    .catch(erro => {
      res.render('error', {error: erro, message: "Erro na alteração do registo da tarefa"})
    })

})

module.exports = router;
