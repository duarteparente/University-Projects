var http = require('http')
var axios = require('axios')
var mypages = require('./mypages')
var fs = require('fs')

http.createServer(function(req, res){
    var d = new Date().toISOString().substring(0,16)
    console.log(req.method + " " + req.url + " " + d)

    if(req.url == '/pessoas'){
        axios.get('http://localhost:3000/pessoas')
            .then(function(resp){
                var pessoas = resp.data
                res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'})
                res.write(mypages.genListPersonPage(pessoas, d, 0, ""))
                res.end()
            })
            .catch(erro =>  {
                console.log("Erro: " + erro)
                res.writeHead(404, {'Content-Type': 'text/html; charset=utf-8'})
                res.end('<p> Erro na obtenção de dados ' + erro + '</p>')
            })
    }
    else if(req.url == '/pessoasOrdenadas'){
        axios.get('http://localhost:3000/pessoas?_sort=nome')
            .then(function(resp){
                var pessoas = resp.data
                res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'})
                res.write(mypages.genListPersonPage(pessoas, d, 0, ""))
                res.end()
            })
            .catch(erro =>  {
                console.log("Erro: " + erro)
                res.writeHead(404, {'Content-Type': 'text/html; charset=utf-8'})
                res.end('<p> Erro na obtenção de dados ' + erro + '</p>')
            })
    }
    else if(req.url.match(/w3\.css$/)){
        fs.readFile("w3.css", function(err,data){
            if(err){
                res.writeHead(404, {'Content-Type': 'text/html; charset=utf-8'})
                res.end("<p>Erro: leitura do ficheiro :: " + err + "</p>")
            }
            else{
                res.writeHead(200, {'Content-Type': 'text/css'})
                res.end(data)
            }
        })

    }
    else if(req.url.match(/\/pessoas\/p\d+/)){
        axios.get('http://localhost:3000/pessoas/' + req.url.substring(9))
            .then(function(resp){
                var pessoa = resp.data
                res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'})
                res.write(mypages.genPersonPage(pessoa, d))
                res.end()
            })
            .catch(erro =>  {
                console.log("Erro: " + erro)
                res.writeHead(404, {'Content-Type': 'text/html; charset=utf-8'})
                res.end('<p> Erro na obtenção de dados ' + erro + '</p>')
            })

    }
    else if (req.url == '/'){
        res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'})
        res.end(mypages.genMainPage(d))

    }
    else if (req.url.match(/\/dist/)){
        var flag = 0;
        if (req.url.substring(5) == "Sex") flag = 1
        else if (req.url.substring(5) == "Sports") flag = 2
        axios.get('http://localhost:3000/pessoas')
            .then(function(resp){
                var pessoas = resp.data
                res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'})
                res.end(mypages.genDistPage(pessoas, d, flag))
            })
            .catch(erro =>  {
                console.log("Erro: " + erro)
                res.writeHead(404, {'Content-Type': 'text/html; charset=utf-8'})
                res.end('<p> Erro na obtenção de dados ' + erro + '</p>')
            })
    }
    else if(req.url.match(/\/pessoas/)){
        var flag = 0
        var desporto = ""
        if (req.url.substring(9,10) == 'd') { desporto = decodeURIComponent(req.url.substring(19)); req.url = "  "; flag = 1; }
        axios.get('http://localhost:3000/pessoas' + req.url.substring(8))
            .then(function(resp){
                var pessoas = resp.data
                res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'})
                
                res.end(mypages.genListPersonPage(pessoas, d, flag, desporto))
            })
            .catch(erro =>  {
                console.log("Erro: " + erro)
                res.writeHead(404, {'Content-Type': 'text/html; charset=utf-8'})
                res.end('<p> Erro na obtenção de dados ' + erro + '</p>')
            })
    }
    else{
        res.writeHead(404, {'Content-Type': 'text/html; charset=utf-8'})
        res.end('<p> Operação não suportada ' + req.url + '</p>')
    }


}).listen(7777);

console.log("Servidor à escuta na porta 7777....")