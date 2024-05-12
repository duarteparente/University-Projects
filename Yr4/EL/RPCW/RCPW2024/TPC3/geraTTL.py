import json

f = open("mapa-virtual.json")
mapa = json.load(f)
f.close()

ttl = '''
#################################################################
#    Individuals
#################################################################

'''


for cidade in mapa["cidades"]:
    ttl += f'''###  http://rpcw.di.uminho.pt/2024/mapa#{cidade["id"]}
:{cidade["id"]} rdf:type owl:NamedIndividual ,
                 :Cidade ;
        :nome "{cidade["nome"]}" ;
        :descricao "{cidade["descrição"]}" ;
        :distrito "{cidade["distrito"]}" ;
        :populacao "{cidade["população"]}"^^xsd:int .

'''

for ligacao in mapa["ligacoes"]:
    ttl += f'''###  http://rpcw.di.uminho.pt/2024/mapa#{ligacao["id"]}
:{ligacao["id"]} rdf:type owl:NamedIndividual ,
                 :Ligacao ;
        :origem :{ligacao["origem"]} ;
        :destino :{ligacao["destino"]} ;
        :distancia "{ligacao["distância"]}"^^xsd:float .

'''

print(ttl)