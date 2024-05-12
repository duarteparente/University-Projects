#### Quantos filmes existem no repositório?

```
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>

select (count(?aluno) as ?nalunos) where {
  ?aluno a :Aluno .
}
```

#### Quantos alunos frequentam o curso "LCC"? (inteiro)

```
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>

select (count(?aluno) as ?nalunos) where {
  ?aluno a :Aluno ;
         :curso "LCC" .
}
```

#### Que alunos tiveram nota positiva no exame de época normal? (lista ordenada alfabeticamente por nome com: idAluno, nome, curso, nota do exame);

```
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
select ?idAluno ?nome ?curso ?nota where { 
    ?aluno rdf:type :Aluno ;
           :id ?idAluno ;
           :nome ?nome ;
           :curso ?curso ;
           :temExame ?exame .
    ?exame rdf:type :Exame ;
            :epocaExame "normal" ;
           :notaExame ?nota .
    filter (?nota >= 9.5)
} order by (?nome)

```

#### Qual a distribuição dos alunos pelas notas do projeto? (lista com: nota e número de alunos que obtiveram essa nota)

```
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
select ?notaProjeto (COUNT(?aluno) AS ?numAlunos) where { 
    ?aluno rdf:type :Aluno ;
           :projeto ?notaProjeto .

} groupby (?notaProjeto)
  order by (?numAlunos)
```

#### Quais os alunos mais trabalhadores durante o semestre? (lista ordenada por ordem decrescente do total: idAluno, nome, curso, total = somatório dos resultados dos TPC)

```
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
select ?idAluno ?nome ?curso (sum(?nota) as ?total) where { 
    ?idAluno rdf:type :Aluno ;
		   :nome ?nome ;
           :curso ?curso ;
           :temTPC ?tpc .
    ?tpc rdf:type :TPC ;
		   :notaTPC ?nota .
		 
} groupby ?idAluno ?nome ?curso
  order by desc (?total)
```


#### Qual a distribuição dos alunos pelos vários cursos? (lista de cursos, ordenada alfabeticamente por curso, com: curso, número de alunos nesse curso)

```
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX : <http://rpcw.di.uminho.pt/2024/alunos/>
select ?nomeCurso (count(?aluno) as ?numAlunos) where { 
    ?aluno rdf:type :Aluno ;
           :curso ?nomeCurso.
} groupby ?nomeCurso
  order by (?nomeCurso)
```