// HTML templates generator

function start(title){
    return `
<!DOCTYPE html>
    <html>
        <head>
            <link rel="stylesheet" type="text/css" href="w3.css"/>
            <meta charset="UTF-8"/>
            <title>${title}</title>
        </head>
        <body>

            <div class="w3-card-4">
                <header class="w3-container w3-blue-grey">
                    <h1>${title}</h1>
                </header>
                <br>
                <div class="w3-container">
                    <table class='w3-table-all'>
`   
}


exports.genListPersonPage = function(lista, data, flag, desporto){
    var pagHTML = start("Lista de Pessoas") + `
                        <tr>
                            <th>Id</th>
                            <th>Nome</th>
                            <th>Idade</th>
                            <th>Sexo</th>
                            <th>Cidade</th>
                        </tr>
    `       
    for (let i=0; i<lista.length; i++){
        if (flag == 0 || (flag == 1 && (lista[i].desportos.includes(desporto)))){
            pagHTML += `
                        <tr>
                            <td>${lista[i].id}</td>
                            <td> <a href="/pessoas/${lista[i].id}">${lista[i].nome}</a> </td>  
                            <td>${lista[i].idade}</td>
                            <td>${lista[i].sexo}</td>
                            <td>${lista[i].morada.cidade}</td>
                        </tr>
            `
        }
        
    }
                    
    pagHTML += `
                    </table>
                </div>
                <hr>
                <br><adress style="margin-left: 40px">[<a href="/">Página Inicial</a>]</adress><br>

                <footer class="w3-container w3-blue-grey">
                    <h5 align="center">Generated for TPC3 in EngWeb2023 ${data}</h5>
                </footer>
            </div> 
            
        </body>
    </html>
    `
    return pagHTML
}


exports.genPersonPage = function(pessoa, data){
    var pagHTML = start(pessoa.nome) + `
                        <tr>
                                <th>Id</th>
                                <th>Nome</th>
                                <th>Idade</th>
                                <th>Sexo</th>
                                <th>Cidade</th>
                        </tr>
                        <tr>
                                <td>${pessoa.id}</td>
                                <td>${pessoa.nome}</td>
                                <td>${pessoa.idade}</td>
                                <td>${pessoa.sexo}</td>
                                <td>${pessoa.morada.cidade}</td>
                        </tr>
                    </table>
                </div>
                <br>
                <hr>
                <br><adress style="margin-left: 40px">[<a href="/">Página Inicial</a>]</adress>
                <br><adress style="margin-left: 40px">[<a href="/pessoas">Lista de Pessoas</a>]</adress>
                <br><adress style="margin-left: 40px">[<a href="/pessoasOrdenadas">Lista de Pessoas Ordenada</a>]</adress><br>


                <footer class="w3-container w3-blue-grey w3-bottom">
                    <h5 align="center">Generated for TPC3 in EngWeb2023 ${data}</h5>
                </footer>
            </div>
        </body>
    </html>
    `   
    return pagHTML    
}


exports.genMainPage = function(data){
    var pagHTML = `
    <!DOCTYPE html>
    <html>
        <head>
            <style>
                .container {
                    font-size: 22px;
                    align: center;
                    border: 3px solid black;
                    display: flex;
                    justify-content: center;
                    margin-top: 25px;
                    margin-left: 500px;
                    margin-right: 500px;

                }
            </style>

            <link rel="stylesheet" type="text/css" href="w3.css"/>
            <meta charset="UTF-8"/>
            <title>About People</title>
        </head>
        <body>
            <h1 align="center"> About People </h1>
            <div class="container">
                <ol>
                    <li><a href="/pessoas">Lista de Pessoas</a></li>
                    <li><a href="/pessoasOrdenadas">Lista de Pessoas Ordenada</a></li>
                    <li><a href="/distSex">Distribuição por Sexo</a></li>
                    <li><a href="/distSports">Distribuição por Desporto</a></li>
                    <li><a href="/distJobs">Top 10 Profissões</a></li>
                </ol>
            </div>
            
            <footer class="w3-container w3-blue-grey w3-bottom">
                    <h5 align="center">Generated for TPC3 in EngWeb2023 ${data}</h5>
            </footer>
        </body>
    </html>        
    `
    return pagHTML;
}


exports.genDistPage = function(lista,data,flag){
    var res = {}
    var pagHTML = ""
    var d = ""
    if (flag == 0){
        d = "profissao"
        var res = {}
        for(let i=0; i<lista.length; i++){
            if (!(lista[i].profissao in res)){
                res[lista[i].profissao] = 0
            }   
        res[lista[i].profissao]++
        }
        pagHTML += start("Top 10 Profissões") + `
        <tr>
            <th>Profissão</th>
        `
        var items = Object.keys(res).map(function(key) {
            return [key, res[key]];
        });
        items.sort(function(first, second) {
          return second[1] - first[1];
        });
        ten = items.slice(0,10)
        res = {}
        for( i in ten){
            res[ten[i][0]] = ten[i][1]
        }
        
        
    } else if(flag==1){
        d = "sexo"
        res['masculino'] = 0
        res['feminino'] = 0
        res['outro'] = 0
        for(let i=0; i<lista.length; i++){
            res[lista[i].sexo]++
        }
        pagHTML += start("Distribuição por Sexo") + `
                        <tr>
                            <th>Sexo</th>
        `

    }
    else{
        d = "desportos"
        for(let i=0; i<lista.length; i++){
            for(let j=0; j<lista[i].desportos.length; j++){
                if(!(lista[i].desportos[j] in res)){
                    res[lista[i].desportos[j]] = 0
                }
                res[lista[i].desportos[j]]++
            }
        }
        pagHTML += start("Distribuição por Desporto") + `
                        <tr>
                            <th>Desporto</th>
            `
    }
    
    pagHTML += `
                            <th>Frequência</th>
                        </tr>
    `
    for (let key in res){
        pagHTML += `
                    <tr>
                        <td>${key}</td>
                        <td> <a href="pessoas?${d}=${key}">${res[key]}</td>
                    </tr>
        `
    }
    pagHTML += `
                </table>
                <hr>
                <adress style="margin-left: 40px">[<a href="/">Página Inicial</a>]</adress><br>
                
            </div>

            <footer class="w3-container w3-blue-grey">
                <h5 align="center">Generated for TPC3 in EngWeb2023 ${data}</h5>
            </footer>
        </body>
    </html>
    `
    return pagHTML

}