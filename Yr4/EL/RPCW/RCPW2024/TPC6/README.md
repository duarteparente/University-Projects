## TPC6

### Especificação das queries:

#### Quantos filmes existem no repositório?

```
PREFIX : <http://rpcw.di.uminho.pt/2024/cinema/>

select (count(?film) as ?nfilmes) where {
  ?film a :Film .
}
```

#### Qual a distribuição de filmes por ano de lançamento?

```
PREFIX : <http://rpcw.di.uminho.pt/2024/cinema/>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>

select ?year (count(?film) as ?count) where {
	?film a :Film ;
          :releaseDate ?date .
    bind(str(year(xsd:date(?date))) as ?year)
} group by ?year order by ?year
```

#### Qual a distribuição de filmes por género?

```
PREFIX : <http://rpcw.di.uminho.pt/2024/cinema/>

select ?genre (count(?film) as ?count) where {
	?film a :Film ;
          :hasGenre ?genre .
} group by ?genre order by ?count
```

#### Em que filmes participu o ator "Burt Reynolds"?

```
PREFIX : <http://rpcw.di.uminho.pt/2024/cinema/>

select ?film where {
	?film a :Film ;
          :hasActor ?actor .
	?actor :name "Burt Reynolds" .   
}
```

#### Produz uma lista de realizadores com o seu nome e os filmes que realizou.

```
PREFIX : <http://rpcw.di.uminho.pt/2024/cinema/>

select ?director ?film where {
    ?film a :Film ;
          :hasDirector ?director .
} order by (?director)
```


#### Qual o título dos livros que aparecem associados aos filmes?

```
------
```