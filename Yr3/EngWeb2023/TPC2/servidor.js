var http = require('http')
var url = require('url')
var fs = require('fs')

var myServer = http.createServer(function(req, res){
    console.log(req.method + " " + req.url + " " + (new Date().toISOString().substring(0,16)))

    var pedido = url.parse(req.url, true).pathname
    if (pedido.length == 1) pedido = "/index" 
    
    fs.readFile('pages/' + pedido.substring(1) + '.html', function(err, data){
        res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'});
        if(err){
            res.write("Erro: leitura do ficheiro :: " + err)
        }
        else{
            res.write(data)
        }
        res.end()
    })

})
myServer.listen(7777);

console.log("Servidor à escuta na porta 7777....")