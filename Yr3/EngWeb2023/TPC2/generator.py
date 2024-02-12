import json

def ordCidade(c):
    return c['nome']

def ordLigacoes(ligacao):
    return int(ligacao['distância'])

f = open("dataset/mapa.json")
mapa = json.load(f)
cidades = mapa['cidades']
cidades.sort(key=ordCidade)
ligacoes = mapa['ligações']
ligacoes.sort(key=ordLigacoes)

map_id_name = {}
for c in cidades:
    map_id_name[c['id']] = c['nome']

def find_key_by_value(d, value):
    for k, v in d.items():
        if value == v:
            return k 


distritos = {}
for c in cidades:
    key = c['distrito']
    if key not in distritos.keys():
        distritos[key] = []
    distritos[key].append(c['nome'])


conn = {}
for l in ligacoes:
    key = l['origem']
    if key not in conn.keys():
        conn[key] = {}
    conn[key][l['destino']] = l['distância']

## Generating index.html
pagHTML = """
<!DOCTYPE html>
<html>
    <head>
        <title> Index </title>
        <meta charset="utf-8"/>
    </head>
    <body>
        <h1 style="text-align: center;"> Índice </h1>
        <div style="margin-left: 30px ; font-size:large">
        
"""

for d in sorted(distritos):
    pagHTML += f"""
                    <h2> {d} </h2>
                    <ul>
    """
    for c in distritos[d]:
        pagHTML += f"""       
                        <li> <a href="{find_key_by_value(map_id_name,c)}">{c}</a></li>
        """
    pagHTML += """
                    </ul><br>
    """

pagHTML += """
        </div>
    </body>
</html>
"""

index = open("pages/index.html", "w")
index.write(pagHTML)
index.close()

## Generating city.html

for c in cidades:
    cx = open(f"pages/{c['id']}.html", "w")
    pagHTML = f"""
<!DOCTYPE html>
    <html>
        <head>
            <title> {c['nome']} </title>
            <meta charset="utf-8"/>
        </head>
        <body>
            <h1 style="text-align: center;"> {c['nome']} </h1>
            <div style="border: 2px solid black">
                <div style="margin-left: 30px; margin-right: 30px; font-size:large;">
                    <p><b>Distrito:</b> {c['distrito']}</p>
                    <p><b>População:</b> {c['população']}</p>
                    <p><b>Descrição:</b> {c['descrição']}</p>
"""
    if c['id'] in conn.keys():
        pagHTML+= "                    <h4>Ligações (Destino: Distância (KM)):</h4>\n                        <ul>\n"
        dest = conn.get(c['id'])
        for item in dest.items():
            pagHTML+= f"""                      <li><a href="{item[0]}">{map_id_name.get(item[0])}</a>: {item[1]:.2f}</li>\n"""

    pagHTML += """
                    <br><adress>[<a href="/">Voltar ao Índice</a>]</adress>                  
                </div>
            </div>
"""
    cx.write(pagHTML)
    cx.close()


