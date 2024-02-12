import json

def ordCidade(c):
    return c['nome']

def ordLigacoes(ligacao):
    return int(ligacao['distância'])

f = open("mapa.json")
mapa = json.load(f)
cidades = mapa['cidades']
cidades.sort(key=ordCidade)
ligacoes = mapa['ligações']
ligacoes.sort(key=ordLigacoes)

map_id_name = {}
for c in cidades:
    map_id_name[c['id']] = c['nome']


aux = {}
for l in ligacoes:
    key = l['origem']
    if key not in aux.keys():
        aux[key] = {}
    aux[key][l['destino']] = l['distância']
        

pagHTML = """
<!DOCTYPE html>
<html>
    <head>
        <title> Mapa Virtual </title>
        <meta charset="utf-8"/>
    </head>

    <body>
        <h1> Mapa Virtual </h1>
        <table>
            <tr>
                <!--coluna indice-->
                <td valign="top">
                    <a name="indice"/>
                    <h3>Índice</h3>
                    <ul>
"""

for c in cidades:
    pagHTML += f"""
                        <li> <a href=#{c['id']}>{c['nome']}</a> </li>    
    """

pagHTML += """
                    </ul>
                </td>
                <!--coluna conteudo-->
                <td width = 70%>

"""

for c in cidades:
    pagHTML += f"""
                    <div style="border: 2px solid black">
                        <a name = "{c['id']}"></a>
                        <h3 align="center">{c['nome']}</h3>
                        <p><b>Distrido:</b> {c['distrito']}</p>
                        <p><b>População:</b> {c['população']}</p>
                        <p><b>Descrição:</b> {c['descrição']}</p>
"""
    if c['id'] in aux.keys():
        pagHTML+= "                        <h4>Ligações (Destino: Distância (KM)):</h4>\n                        <ul>\n"
        dest = aux.get(c['id'])
        for item in dest.items():
            pagHTML+= f"""                          <li><a href='#{item[0]}'>{map_id_name.get(item[0])}</a>: {item[1]:.2f}</li>\n"""
    pagHTML += """
                        <br><adress>[<a href="#indice">Voltar ao índice</a>]</adress> 
                    </div>
    """


pagHTML += """
                </td>
            </tr>
        </table>
    </body>
</html>
"""

print(pagHTML)