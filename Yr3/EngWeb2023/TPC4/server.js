var http = require('http')
var axios = require('axios')
var templates = require('./templates')
var static = require('./static.js')
const { parse } = require('querystring');

// Aux functions
function collectRequestBodyData(request, callback) {
    if(request.headers['content-type'] === 'application/x-www-form-urlencoded') {
        let body = '';
        request.on('data', chunk => {
            body += chunk.toString();
        });
        request.on('end', () => {
            callback(parse(body));
        });
    }
    else {
        callback(null);
    }
}

var server = http.createServer(function(req, res){
    // Logger: what was requested and when it was requested
    var date = new Date().toISOString().substring(0, 16)
    console.log(req.method + " " + req.url + " " + date)

    // Handling request
    if(static.staticResource(req)){
        static.serveStaticResource(req, res)
    }
    else{
        switch(req.method){
            // GET / ------------------------------------------------
            case "GET":
                if((req.url == "/") || (req.url == "/tasks")){
                    axios.get("http://localhost:3000/tasks?_sort=due_date")
                        .then(response => {
                            res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                            res.end(templates.genMainPage(response.data,date))
                        })
                        .catch(function(erro){
                            res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                            res.end("<p>Erro: " + erro)

                        })
                }
                // GET /tasks/done/id ----------------------------------------
                if(/\/tasks\/done\/[0-9]+$/i.test(req.url)){
                    var id = req.url.split("/")[3]
                    axios.get('http://localhost:3000/tasks/' + id)
                        .then(response => {
                            var task = response.data
                            task.done = 1
                            axios.put("http://localhost:3000/tasks/" + id, task)
                                .then(() => {
                                    axios.get("http://localhost:3000/tasks?_sort=due_date")
                                        .then(r => {
                                            res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                                            res.end(templates.genMainPage(r.data,date))
                                        })
                                        .catch(function(erro){
                                            res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                                            res.end("<p>Erro: " + erro)
                                        })
                                })
                        })
                }
                // GET /tasks/undo/id ----------------------------------------
                if(/\/tasks\/undo\/[0-9]+$/i.test(req.url)){
                    var id = req.url.split("/")[3]
                    axios.get('http://localhost:3000/tasks/' + id)
                        .then(response => {
                            var task = response.data
                            task.done = 0
                            axios.put("http://localhost:3000/tasks/" + id, task)
                                .then(() => {
                                    axios.get("http://localhost:3000/tasks?_sort=due_date")
                                        .then(r => {
                                            res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                                            res.end(templates.genMainPage(r.data,date))
                                        })
                                        .catch(function(erro){
                                            res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                                            res.end("<p>Erro: " + erro)
                                        })
                                })
                        })
                }
                // GET /alunos/edit/id --------------------------------------------------------------------
                else if(/\/tasks\/edit\/[0-9]+$/i.test(req.url)){
                    var id = req.url.split("/")[3]
                    axios.get('http://localhost:3000/tasks/' + id)
                        .then(response => {
                            res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                            res.end(templates.genEditPage(response.data, date))
                        })
                        .catch(erro => {
                            console.log("Erro: " + erro)
                        })
                }
                // GET /tasks/delete/id ----------------------------------------
                if(/\/tasks\/delete\/[0-9]+$/i.test(req.url)){
                    var id = req.url.split("/")[3]
                    axios.delete('http://localhost:3000/tasks/' + id)
                        .then(resp => {
                            console.log("Delete " + id + " :: " + resp.status)
                            axios.get("http://localhost:3000/tasks?_sort=due_date")
                                .then(r => {
                                    res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                                    res.end(templates.genMainPage(r.data,date))
                                })
                                .catch(function(erro){
                                    res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                                    res.end("<p>Erro: " + erro)
                                })
                        })
                        .catch(erro => {
                            console.log("Erro: " + erro)
                        })
                }

                break
            case "POST":
                if (/\/tasks\/edit\/[0-9]+$/i.test(req.url)){
                    var id = req.url.split("/")[3]
                    collectRequestBodyData(req, result => {
                        if(result){
                            console.dir(result)
                            result['done'] = 0
                            axios.put("http://localhost:3000/tasks/" + id, result)
                                .then(resp => {
                                    console.log(resp.data);
                                    axios.get("http://localhost:3000/tasks?_sort=due_date")
                                        .then(response => {
                                            res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                                            res.end(templates.genMainPage(response.data,date))
                                        })
                                    
                                })
                                .catch(error => {
                                    console.log('Erro: ' + error); 
                                })
                        }
                        else{
                            res.writeHead(201, {'Content-Type': 'text/html;charset=utf-8'})
                            res.end("<p>Unable to collect data from body...</p>")
                        }
                    })

                }
                else{
                    collectRequestBodyData(req, result => {
                        if(result){
                            console.dir(result)
                            result['done'] = 0
                            axios.post('http://localhost:3000/tasks', result)
                                .then(resp => {
                                    console.log(resp.data);
                                    axios.get("http://localhost:3000/tasks?_sort=due_date")
                                        .then(response => {
                                            res.writeHead(200, {'Content-Type': 'text/html;charset=utf-8'})
                                            res.end(templates.genMainPage(response.data,date))
                                        })
                                    
                                })
                                .catch(error => {
                                    console.log('Erro: ' + error); 
                                })
                        }
                        else{
                            res.writeHead(201, {'Content-Type': 'text/html;charset=utf-8'})
                            res.end("<p>Unable to collect data from body...</p>")
                        }
                    })
                }
                break
        }
    }

})


server.listen(7777, ()=>{
    console.log("Servidor à escuta na porta 7777...")
})