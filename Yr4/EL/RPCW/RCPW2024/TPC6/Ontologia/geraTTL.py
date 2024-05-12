import requests
import json

sparql_endpoint = "http://dbpedia.org/sparql"
cinema_json = {
    'Films': {},
    'People': {}
}

for counter in range(5):
    offset = counter * 10000

    sparql_query = f"""
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    PREFIX dbo: <http://dbpedia.org/ontology/>

    select distinct ?film ?title ?abstract ?release ?genre ?duration ?country ?director ?director_name ?director_birthDate  ?producer ?producer_name ?producer_birthDate ?actor ?actor_name ?actor_birthDate ?writer ?writer_name ?writer_birthDate ?composer ?composer_name ?composer_birthDate where {{
         ?film rdf:type <http://dbpedia.org/ontology/Film> .

         ?film rdfs:label ?title .
         optional {{?film dbo:abstract ?abstract .}}
         optional {{ ?film dbo:releaseDate ?release .}}
         optional {{ ?film dbo:genre ?genre .}}
         optional {{ ?film dbo:runtime ?duration .}}
         optional {{ ?film dbo:country ?country .}}

         optional {{ ?film dbo:director ?director . 
              optional {{ ?director foaf:name ?director_name . }}
              optional {{ ?director dbo:birthDate ?director_birthDate . }}
         }}
         optional {{ ?film dbo:producer ?producer . 
              optional {{ ?producer foaf:name ?producer_name . }}
              optional {{ ?producer dbo:birthDate ?producer_birthDate . }}     
         }}
         optional {{ ?film dbo:starring ?actor .
              optional {{ ?actor foaf:name ?actor_name . }}
              optional {{ ?actor dbo:birthDate ?actor_birthDate . }}
         }}
         optional {{ ?film dbo:writer ?writer . 
              optional {{ ?writer foaf:name ?writer_name . }}
              optional {{ ?writer dbo:birthDate ?writer_birthDate . }}     
         }}
         optional {{ ?film dbo:musicComposer ?composer . 
              optional {{ ?composer foaf:name ?composer_name . }}
              optional {{ ?composer dbo:birthDate ?composer_birthDate . }}
         }}     

         filter(lang(?title)='en') .
         filter(lang(?abstract)='en') .
    }} limit 10000 offset {offset}
    """

    headers = {
            "Accept": "application/sparql-results+json"
        }

    params = {
        "query": sparql_query,
        "format": "json"
    }

    response = requests.get(sparql_endpoint, params=params, headers=headers)
    if response.status_code == 200:
        results = response.json()

        for result in results["results"]["bindings"]:

            uri = result["film"]["value"]

            if uri not in cinema_json["Films"]:
                cinema_json["Films"][uri] = {
                    "title": result["title"]["value"],
                    "directors": set(),
                    "producers": set(),
                    "cast": set(),
                    "writers": set(),
                    "composers": set()
                }
            if "abstract" in result:
                cinema_json["Films"][uri]["abstract"] = result["abstract"]["value"]
            if "genre" in result:
                cinema_json["Films"][uri]["genre"] = result["genre"]["value"]
            if "release" in result:
                cinema_json["Films"][uri]["release"] = result["release"]["value"]
            if "duration" in result:
                cinema_json["Films"][uri]["duration"] = result["duration"]["value"]
            if "country" in result:
                cinema_json["Films"][uri]["country"] = result["country"]["value"]

            if "director" in result:
                director = {}
                uri_dir = result["director"]["value"]
                cinema_json["Films"][uri]["directors"].add(uri_dir)

                if "director_name" in result:
                    director["name"] = result["director_name"]["value"]
                if "director_birthDate" in result:
                    director["birthDate"] = result["director_birthDate"]["value"]
                if uri_dir not in cinema_json["People"]:
                    cinema_json["People"][uri_dir] = director

            if "producer" in result:
                producer = {}
                uri_prod = result["producer"]["value"]
                cinema_json["Films"][uri]["producers"].add(uri_prod)

                if "producer_name" in result:
                    producer["name"] = result["producer_name"]["value"]
                if "producer_birthDate" in result:
                    producer["birthDate"] = result["producer_birthDate"]["value"]
                if uri_prod not in cinema_json["People"]:
                    cinema_json["People"][uri_prod] = producer

            if "actor" in result:
                actor = {}
                uri_actor = result["actor"]["value"]
                cinema_json["Films"][uri]["cast"].add(uri_actor)

                if "actor_name" in result:
                    actor["name"] = result["actor_name"]["value"]
                if "actor_birthDate" in result:
                    actor["birthDate"] = result["actor_birthDate"]["value"]
                if uri_actor not in cinema_json["People"]:
                    cinema_json["People"][uri_actor] = actor

            if "writer" in result:
                writer = {}
                uri_writer = result["writer"]["value"]
                cinema_json["Films"][uri]["writers"].add(uri_writer)

                if "writer_name" in result:
                    writer["name"] = result["writer_name"]["value"]
                if "writer_birthDate" in result:
                    writer["birthDate"] = result["writer_birthDate"]["value"]
                if uri_writer not in cinema_json["People"]:
                    cinema_json["People"][uri_writer] = writer

            if "composer" in result:
                composer = {}
                uri_comp = result["composer"]["value"]
                cinema_json["Films"][uri]["composers"].add(uri_comp)

                if "composer_name" in result:
                    composer["name"] = result["composer_name"]["value"]
                if "composer_birthDate" in result:
                    composer["birthDate"] = result["composer_birthDate"]["value"]
                if uri_comp not in cinema_json["People"]:
                    cinema_json["People"][uri_comp] = composer

    else:   
        print("Error:", response.status_code)
        print(response.text)


ttl = ""


for individual, individual_data in cinema_json["People"].items():
    name = individual.rsplit("/", 1)[-1].replace(".","").replace("(","").replace(")","").replace("'","").replace("´","").replace(",","").replace("&","").replace("–","_")

    ttl += f"""
###  http://rpcw.di.uminho.pt/2024/cinema#{name}
:{name} rdf:type owl:NamedIndividual ;
                  :birthDate "{individual_data.get("birthDate","")}" ;
                  :name "{individual_data.get("name","").replace('"',"'")}" .
"""
    
for film, film_data in cinema_json["Films"].items():
    label = film.rsplit("/", 1)[-1].replace("(","").replace(")","")
    
    ttl += f"""
###  http://rpcw.di.uminho.pt/2024/cinema#{label}
<http://rpcw.di.uminho.pt/2024/cinema#{label}> rdf:type owl:NamedIndividual ,
                                                                     :Film ;"""

    if "cast" in film_data and len(film_data["cast"]) > 0:
        ttl += '''      
                                                                     :hasActor '''
        for actor in film_data["cast"]:
            name = actor.rsplit("/", 1)[-1].replace("'","").replace('(', '_').replace(')','_').replace(',', '_').replace('.', '').replace("__","_").replace("-","")
            ttl += f''':{name} ,
                                                                               '''
        ttl = ttl[:-81] + ';'


    if "composers" in film_data and len(film_data["composers"]) > 0:
        ttl += '''      
                                                                     :hasComposer '''
        for composer in film_data["composers"]:
            name = composer.rsplit("/", 1)[-1].replace("'","").replace('(', '_').replace(')','_').replace(',', '_').replace('.', '').replace("__","_").replace("-","")
            ttl += f''':{name} ,
                                                                                  '''
        ttl = ttl[:-84] + ';'

    if "country" in film_data and len(film_data["country"]) > 0:                                                        
        ttl += '''      
                                                                     :hasCountry '''
        ttl += f''':{film_data["country"].rsplit("/", 1)[-1]} ,
                                                                                 '''
        ttl = ttl[:-84] + ';'

    if "directors" in film_data and len(film_data["directors"]) > 0:
        ttl += '''      
                                                                     :hasDirector '''
        for director in film_data["directors"]:
            name = director.rsplit("/", 1)[-1].replace("'","").replace('(', '_').replace(')','_').replace(',', '_').replace('.', '').replace("__","_").replace("-","")
            ttl += f''':{name} ,
                                                                                  '''
        ttl = ttl[:-84] + ';'

    if "producers" in film_data and len(film_data["producers"]) > 0:
        ttl += '''      
                                                                     :hasProducer '''
        for producer in film_data["producers"]:
            name = producer.rsplit("/", 1)[-1].replace("'","").replace('(', '_').replace(')','_').replace(',', '_').replace('.', '').replace("__","_").replace("-","")
            ttl += f''':{name} ,
                                                                                  '''
        ttl = ttl[:-84] + ';'

    if "writers" in film_data and len(film_data["writers"]) > 0:
        ttl += '''      
                                                                     :hasWriter '''
        for writer in film_data["writers"]:
            name = writer.rsplit("/", 1)[-1].replace("'","").replace('(', '_').replace(')','_').replace(',', '_').replace('.', '').replace("__","_").replace("-","")
            ttl += f''':{name} ,
                                                                                '''
        ttl = ttl[:-82] + ';'

    if "genre" in film_data and len(film_data["genre"]) > 0:                                                      
        ttl += '''      
                                                                     :genre '''
        ttl += f''':{film_data["genre"].rsplit("/", 1)[-1].replace("'","").replace('(', '_').replace(')','_')} ,
                                                                            '''
        ttl = ttl[:-79] + ';'

    if "release" in film_data and len(film_data["release"]) > 0:                                                     
        ttl += '''      
                                                                     :release '''
        ttl += f''':{film_data["release"]} ,
                                                                              '''
        ttl = ttl[:-80] + ';'

    if "abstract" in film_data and len(film_data["abstract"]) > 0:                                                    
        ttl += '''      
                                                                     :abstract '''
        ttl += f'''"{film_data["abstract"].replace('"',"'")}" ,
                                                                               '''
        ttl = ttl[:-81] + ';'

    if "duration" in film_data and len(film_data["duration"]) > 0:                                                      
        ttl += '''      
                                                                     :duration '''
        ttl += f''':{film_data["duration"].replace("-","")} ,
                                                                               '''
        ttl = ttl[:-81] + ';'
                                                     
    ttl += '''      
                                                                     :title '''
    ttl += f'''"{film_data["title"]}" ,
                                                                            '''
    ttl = ttl[:-79] + '.\n'

                                        
with open("cinema_pg53791.ttl", "a") as out_file:
    out_file.write(ttl)