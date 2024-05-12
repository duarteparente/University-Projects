#### Lista de cidades, ordenada alfabeticamente pelo nome

```
PREFIX : <http://rpcw.di.uminho.pt/2024/mapa/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

select ?nome where { 
    ?cidade rdf:type :Cidade ;
         :nome ?nome.
} order by (?nome)
```


#### Distribuição das cidades por distrito: lista de distritos ordenada alfabeticamente em que para cada um se indica quantas cidades tem

```
PREFIX : <http://rpcw.di.uminho.pt/2024/mapa/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

select ?distrito (count(?cidade) as ?numCidades) where { 
    ?cidade rdf:type :Cidade ;
         :nome ?nome ;
         :distrito ?distrito .
} group by ?distrito 
order by (?distrito)
```


#### Que cidades têm ligações diretas com Braga? (Considera Braga como origem mas também como destino)

```
PREFIX : <http://rpcw.di.uminho.pt/2024/mapa/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

select ?cidade where {
    {
        ?origem :distrito "Braga" .
        ?ligacao :origem ?origem ;
                  :destino ?destino .
        ?destino rdf:type :Cidade ;
                 :nome ?cidade .
    } union {
        ?destino :distrito "Braga" .
        ?ligacao :origem ?origem ;
                  :destino ?destino .
        ?origem rdf:type :Cidade ;
                :nome ?cidade .
    }
} order by (?cidade)

```


#### Partindo de Braga, que cidades se conseguem visitar? (Apresenta uma lista de cidades ordenada alfabeticamente)

```
PREFIX : <http://rpcw.di.uminho.pt/2024/mapa/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

select ?cidade where {
    ?distrito :distrito "Braga" .
    ?ligacao :origem ?distrito ;
  	      :destino ?destino .
    ?destino rdf:type :Cidade ;
    	  :nome ?cidade .
} order by (?cidade)

```

#### Através duma query CONSTRUCT cria uma ligação direta entre Braga e todas as cidades que se conseguem visitar a partir dela

```


```

#### Através duma query CONSTRUCT cria uma ligação direta entre cada uma das cidades e todas as cidades que se conseguem visitar a partir dela.

```


```