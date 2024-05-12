## TPC2

Classes:
- Cidade, Ligação

Object Properties:
- Origem, Destino

Data Properties:
- Cidade: nome, descrição, distrito, população
- Ligação: distância

### Especificação das queries:

#### Quais as cidades de um determinado distrito?

```
PREFIX : <http://rpcw.di.uminho.pt/2024/mapa/>

select distinct ?nome where { 
    ?cidade :distrito "Viana do Castelo".
    ?cidade :nome ?nome.
}
```

#### Distribuição de cidades por distrito?

```
PREFIX : <http://rpcw.di.uminho.pt/2024/mapa/>

select ?distrito (COUNT(?cidade) AS ?numCidades) where {
  ?cidade :distrito ?distrito
}
GROUP BY (?distrito)
ORDER BY DESC (?numCidades)
```

#### Quantas cidades se podem atingir a partir do Porto?

```
PREFIX : <http://rpcw.di.uminho.pt/2024/mapa/>

select (COUNT(?destino) AS ?numDestinos) where {
    ?distrito :distrito "Porto" .
    ?ligacao :origem ?distrito .
  	?ligacao :destino ?destino .
}
```

#### Quais as cidades com população acima de um determinado valor?

```
PREFIX : <http://rpcw.di.uminho.pt/2024/mapa/>

select ?cidade ?populacao where {
    ?c :populacao ?populacao .
    ?c :nome ?cidade .
    filter (?populacao > 205585) .
}

order by desc (?populacao)
```