from datetime import datetime
from flask import Flask, render_template, url_for
import requests

app = Flask(__name__)

data_iso = datetime.now().strftime("%Y-%m-%dT%H:%M:%S")

## GraphDB Endpoint
graphdb_endpoint = "http://localhost:7200/repositories/tabelaPeriodica"

@app.route('/')
def index():
    return render_template('index.html', data = {"data": data_iso})

@app.route('/elementos')
def elementos():
    sparql_query = '''
PREFIX : <http://www.daml.org/2003/01/periodictable/PeriodicTable#>

SELECT ?nome ?simb ?num ?grupo ?gname WHERE{
    ?s a :Element ;
        :name ?nome ;
        :symbol ?simb ;
        :atomicNumber ?num ;
        :group ?grupo .
    optional{ ?grupo :name ?gname . }
}
ORDER BY (?num)
'''

    resposta = requests.get(graphdb_endpoint, 
                            params={'query': sparql_query }, 
                            headers={'Accept': 'application/sparql-results+json'}
                )
    if resposta.status_code == 200:
        dados = resposta.json()['results']['bindings']
        return render_template('elementos.html', data = {"data": data_iso, "dados": dados})
    else:
        return render_template('empty.html', data = {"data": data_iso})
    

@app.route('/elementos/<elemento>')
def elemento(elemento):
    sparql_query = f'''
PREFIX : <http://www.daml.org/2003/01/periodictable/PeriodicTable#>

SELECT ?group ?simb ?num ?aw ?b ?cr ?c ?col ?period ?ss ?gname WHERE {{
    ?s a :Element ;
       :name "{elemento}" ;
       :group ?group ;
       :symbol ?simb ;
       :atomicNumber ?num .
    optional {{ ?s :atomicWeight ?aw . }}
    optional {{ ?s :block ?b . }}
    optional {{ ?s :casRegistryID ?cr . }}
    optional {{ ?s :classification ?c . }}
    optional {{ ?s :color ?col . }}
    optional {{ ?s :period ?period . }}
    optional {{ ?s :standardState ?ss .}}
    optional {{ ?group :name ?gname .}}
}}
'''
    resposta = requests.get(graphdb_endpoint, params={ 'query': sparql_query }, headers={ 'Accept': 'application/sparql-results+json' })
    if resposta.status_code == 200:
        dados = resposta.json()['results']['bindings']
        return render_template('elemento.html', data = {"data": data_iso, "dados": dados[0], "nome": elemento})
    else:
        return render_template('empty.html', data = { 'data': data_iso })
    

@app.route('/grupos')
def grupos():
    sparql_query = '''
PREFIX : <http://www.daml.org/2003/01/periodictable/PeriodicTable#>

SELECT ?grupo (COUNT(?el) as ?numElem) WHERE { 
    ?grupo a :Group .
    ?el :group ?grupo .
} GROUP BY (?grupo)
ORDER BY (?grupo)
'''
    resposta = requests.get(graphdb_endpoint, 
                            params={'query': sparql_query }, 
                            headers={'Accept': 'application/sparql-results+json'}
                )
    if resposta.status_code == 200:
        dados = resposta.json()['results']['bindings']
        return render_template('grupos.html', data = {"data": data_iso, "dados": dados})
    else:
        return render_template('empty.html', data = {"data": data_iso})



@app.route('/grupos/<grupo>')
def grupo(grupo):
    sparql_query = f'''
PREFIX : <http://www.daml.org/2003/01/periodictable/PeriodicTable#>

SELECT ?group ?nome ?num ?elnome WHERE {{
  ?group a :Group .
    optional {{ ?group :name ?nome . }}
    optional {{ ?group :number ?num . }}
    ?group :element ?elem .
    ?elem :name ?elnome .
    FILTER(str(?group) = "http://www.daml.org/2003/01/periodictable/PeriodicTable#{grupo}")
}}
'''
    resposta = requests.get(graphdb_endpoint, params={ 'query': sparql_query }, headers={ 'Accept': 'application/sparql-results+json' })
    if resposta.status_code == 200:
        dados = resposta.json()['results']['bindings']
        return render_template('grupo.html', data = {"data": data_iso, "dados": dados})
    else:
        return render_template('empty.html', data = { 'data': data_iso})


if __name__== "__main__":
    app.run(debug=True)