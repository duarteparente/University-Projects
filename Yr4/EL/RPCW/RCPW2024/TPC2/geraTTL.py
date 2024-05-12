import json

f = open("db.json")
bd = json.load(f)
f.close()

ttl = '''@prefix : <http://rpcw.di.uminho.pt/2024/musica/> .
@prefix owl: <http://www.w3.org/2002/07/owl#> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix xml: <http://www.w3.org/XML/1998/namespace> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
@base <http://rpcw.di.uminho.pt/2024/musica/> .

<http://rpcw.di.uminho.pt/2024/musica> rdf:type owl:Ontology .

#################################################################
#    Object Properties
#################################################################


###  http://rpcw.di.uminho.pt/2024/musica#ensina
:ensina rdf:type owl:ObjectProperty ;
               rdfs:domain :Curso ;
               rdfs:range :Instrumento .


###  http://rpcw.di.uminho.pt/2024/musica#estuda
:estuda rdf:type owl:ObjectProperty ;
        rdfs:domain :Aluno ;
        rdfs:range :Curso .


#################################################################
#    Data properties
#################################################################

###  http://rpcw.di.uminho.pt/2024/musica#anoCurso
:anoCurso rdf:type owl:DatatypeProperty ;
          rdfs:domain :Aluno ;
          rdfs:range xsd:int .


###  http://rpcw.di.uminho.pt/2024/musica#dataNascimento
:dataNascimento rdf:type owl:DatatypeProperty ;
          rdfs:domain :Aluno ;
          rdfs:range xsd:string .


###  http://rpcw.di.uminho.pt/2024/musica#designacao
:designacao rdf:type owl:DatatypeProperty ;
            rdfs:domain :Curso ;
            rdfs:range xsd:string .


###  http://rpcw.di.uminho.pt/2024/musica#duracao
:duracao rdf:type owl:DatatypeProperty ;
         rdfs:domain :Curso ;
         rdfs:range xsd:int .


###  http://rpcw.di.uminho.pt/2024/musica#nomeAluno
:nomeAluno rdf:type owl:DatatypeProperty ;
           rdfs:domain :Aluno ;
           rdfs:range xsd:string .


###  http://rpcw.di.uminho.pt/2024/musica#nomeInstrumento
:nomeInstrumento rdf:type owl:DatatypeProperty ;
                 rdfs:domain :Instrumento ;
                 rdfs:range xsd:string .

         
#################################################################
#    Classes
#################################################################

###  http://rpcw.di.uminho.pt/2024/musica#Aluno
:Aluno rdf:type owl:Class .


###  http://rpcw.di.uminho.pt/2024/musica#Curso
:Curso rdf:type owl:Class .


###  http://rpcw.di.uminho.pt/2024/musica#Instrumento
:Instrumento rdf:type owl:Class .

#################################################################
#    Individuals
#################################################################

'''

for aluno in bd["alunos"]:
    ttl += f'''###  http://rpcw.di.uminho.pt/2024/musica#{aluno["id"]}
:{aluno["id"]} rdf:type owl:NamedIndividual ,
                 :Aluno ;
        :estuda :{aluno["curso"]} ;
        :anoCurso "{aluno["anoCurso"]}"^^xsd:int ;
        :dataNascimento "{aluno["dataNasc"]}" ;
        :nomeAluno "{aluno["nome"]}" .

        
'''
    
for curso in bd["cursos"]:
    ttl+=f'''###  http://rpcw.di.uminho.pt/2024/musica#{curso["id"]}
:{curso["id"]} rdf:type owl:NamedIndividual ,
              :Curso ;
     :ensina :{curso["instrumento"]["id"]} ;
     :designacao "{curso["designacao"]}" ;
     :duracao {curso["duracao"]} .


'''
    
for instrumento in bd["instrumentos"]:
    ttl += f'''###  http://rpcw.di.uminho.pt/2024/musica#{instrumento["id"]}
:{instrumento["id"]} rdf:type owl:NamedIndividual ,
              :Instrumento ;
     :nomeInstrumento "{instrumento["#text"]}" .

'''


f = open("musica.ttl", "w")
f.write(ttl)
f.close()