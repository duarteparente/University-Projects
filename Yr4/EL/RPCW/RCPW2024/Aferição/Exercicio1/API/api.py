from flask import Flask, request
import requests

app = Flask(__name__)

## GraphDB Endpoint
graphdb_endpoint = "http://localhost:7200/repositories/Alunos"


@app.route('/api/alunos')
def alunos():

    sparql_query = '''
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
select ?idAluno ?nome ?curso where { 
    ?idAluno rdf:type :Aluno ;
           :curso ?curso ;
           :nome ?nome .
} order by (?nome)
'''

    curso = request.args.get('curso')
    if curso:
        sparql_query = sparql_query.replace(':curso ?curso', f':curso "{curso}"')
    
    groupBy = request.args.get('groupBy')
    if groupBy:
        sparql_query = '''
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
select ?curso (count(?aluno) as ?numAlunos) where { 
    ?aluno rdf:type :Aluno ;
           :curso ?curso ;
} group by ?curso
order by (?curso)
''' 
        if groupBy == 'projeto':
            sparql_query = sparql_query.replace('curso', 'projeto')
        elif groupBy == 'recurso':
            sparql_query = '''
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
select ?idAluno ?nome ?curso ?exame where { 
    ?idAluno rdf:type :Aluno ;
           :curso ?curso ;
           :nome ?nome ;
    	   :temExame ?exame .
    ?exame rdf:type :Exame ;
    	   :epocaExame "recurso" .
           
} order by (?nome)
'''

    resposta = requests.get(graphdb_endpoint, params={ 'query': sparql_query }, headers={ 'Accept': 'application/sparql-results+json' })
    if resposta.status_code == 200:
        return resposta.json()['results']['bindings']
    else:
        return []


@app.route('/api/alunos/tpc')
def tpcs():
    sparql_query = '''
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
select ?idAluno ?nome ?curso (count(?tpc) as ?numTPC) where { 
    ?idAluno rdf:type :Aluno ;
           :curso ?curso ;
           :nome ?nome ;
           :temTPC ?tpc .
    ?tpc rdf:type :TPC .
} group by ?idAluno ?nome ?curso 
order by (?nome)
'''
    resposta = requests.get(graphdb_endpoint, params={ 'query': sparql_query }, headers={ 'Accept': 'application/sparql-results+json' })
    if resposta.status_code == 200:
        return resposta.json()['results']['bindings']
    else:
        return []

@app.route('/api/alunos/avaliados')
def avaliados():
    sparql_query = '''
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

select ?idAluno ?nome ?curso ?notaFinal where { 
    ?idAluno rdf:type :Aluno ;
           :curso ?curso ;
           :nome ?nome ;
           :projeto ?projeto ;
           :temTPC ?tpc .
    ?tpc rdf:type :TPC ;
            :notaTPC ?notaTPC .
    
    {
        select ?idAluno (max(?notaExame) as ?maxNotaExame) (sum(?notaTPC) as ?sumNotaTPC) where {
            ?idAluno :temExame ?exame .
            ?exame rdf:type :Exame ;
            	 :notaExame ?notaExame .
        } group by ?idAluno
    }
    
    bind(if(?projeto < 10 || ?maxNotaExame < 10, "R",
             if((?sumNotaTPC + (?projeto * 0.4) + (?maxNotaExame * 0.4)) < 10, "R",
                ?sumNotaTPC + (?projeto * 0.4) + (?maxNotaExame * 0.4))) as ?notaFinal)
} group by ?idAluno ?nome ?curso ?notaFinal
'''
    resposta = requests.get(graphdb_endpoint, params={ 'query': sparql_query }, headers={ 'Accept': 'application/sparql-results+json' })
    if resposta.status_code == 200:
        return resposta.json()['results']['bindings']
    else:
        return []



@app.route('/api/alunos/<idAluno>')
def aluno(idAluno):
    sparql_query = f'''
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
select ?nome ?curso ?projeto ?exame ?tpc where {{ 
    ?aluno rdf:type :Aluno ;
           :id "{idAluno}" ;
           :curso ?curso ;
           :nome ?nome ;
    	   :projeto ?projeto ;
           :temExame ?exame ;
           :temTPC ?tpc .
    ?exame rdf:type :Exame .
    ?tpc rdf:type :TPC .
}}
'''
    resposta = requests.get(graphdb_endpoint, params={ 'query': sparql_query }, headers={ 'Accept': 'application/sparql-results+json' })
    if resposta.status_code == 200:
        return resposta.json()['results']['bindings']
    else:
        return []




if __name__== "__main__":
    app.run(debug=True)