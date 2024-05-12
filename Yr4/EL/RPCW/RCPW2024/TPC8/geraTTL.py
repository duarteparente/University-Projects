import xml.etree.ElementTree as ET

tree = ET.parse('royal.xml')

root = tree.getroot()

sex = {}
sex_to_op = {
    'M': ':temPai',
    'F': ':temMae'
}

for person in root.findall('person'):
    sex[person.find('id').text] = person.find('sex').text if person.find('sex') != None else None


ttl = ""


for pessoa in root.findall('person'):
    ttl += f"""
###  http://rpcw.di.uminho.pt/2024/familia#{pessoa.find("id").text}
:{pessoa.find("id").text} rdf:type owl:NamedIndividual ,
                       :Pessoa ;"""
    
    for parent in pessoa.findall('parent'):
        ref = parent.get('ref')
        ttl += f'\n              {sex_to_op[sex[ref]]} :{ref} ;'

    if pessoa.find("name") != None:
        ttl += f'\n              :nome "{pessoa.find("name").text}" ;'
    if pessoa.find("titl") != None:
        ttl += f'\n              :titulo "{pessoa.find("titl").text}" ;'
    if pessoa.find("sex") != None:
        ttl += f'\n              :sexo "{pessoa.find("sex").text}" ;'
    if pessoa.find("birthdate") != None:
        ttl += f'\n              :dataNascimento "{pessoa.find("birthdate").text}" ;'
    if pessoa.find("birthplace") != None:
        ttl += f'\n              :localNascimento "{pessoa.find("birthplace").text}" ;'
    if pessoa.find("christeningdate") != None:
        ttl += f'\n              :dataBatismo "{pessoa.find("christeningdate").text}" ;'
    if pessoa.find("christeningplace") != None:
        ttl += f'\n              :localBatismo "{pessoa.find("christeningplace").text}" ;'
    if pessoa.find("deathdate") != None:
        ttl += f'\n              :dataMorte "{pessoa.find("deathdate").text}" ;'
    if pessoa.find("deathplace") != None:
        ttl += f'\n              :localMorte "{pessoa.find("deathplace").text}" ;'
    if pessoa.find("burialdate") != None:
        ttl += f'\n              :dataEnterro "{pessoa.find("burialdate").text}" ;'
    if pessoa.find("burialplace") != None:
        ttl += f'\n              :localEnterro "{pessoa.find("burialplace").text}" ;'
    if pessoa.find("familyasspouse") != None:
        ttl += "\n              :familiaConjuge "
        for fspouse in pessoa.findall("familyasspouse"):
                ttl += f'"{fspouse.text}" ,\n                              '
           
        ttl = ttl[:-33] + " ;"
        
    if pessoa.find("familyaschild") != None:
        ttl += f'\n              :familiaFilho "{pessoa.find("familyaschild").text}" ;'
    if pessoa.find("refn") != None:
        ttl += f'\n              :refn "{pessoa.find("familyaschild").text}"^^xsd:int ;'

    if pessoa.find("spouse") != None:
        ttl += "\n              :conjuge "
        for spouse in pessoa.findall("spouse"):
                ttl += f'"{spouse.text}" ,\n                       '
           
        ttl = ttl[:-26] + " ;"

    if pessoa.find("child") != None:
        ttl += "\n              :filho "
        for child in pessoa.findall("child"):
                ttl += f'"{child.text}" ,\n                     '
           
        ttl = ttl[:-23] + " ;"
    ttl += "\n"

ttl += "\n\n"


for familia in root.findall('family'):
    ttl += f'''
###  http://rpcw.di.uminho.pt/2024/familia#{familia.find("id").text}
:{familia.find("id").text} rdf:type owl:NamedIndividual ,
                       :Familia ;'''
     
    if familia.find('wife') != None:
        ttl += f'\n              :esposa "{familia.find("wife").text}" ;'


    if familia.find('husb') != None:
        ttl += f'\n              :esposo "{familia.find("husb").text}" ;'
    
    casamento = familia.find('marr')
    if casamento != None and casamento.find('date') != None:
        ttl += f'\n              :dataCasamento "{casamento.find("date").text}" ;'
    
    if casamento != None and casamento.find('place') != None:
        ttl += f'\n              :localCasamento "{casamento.find("place").text}" ;'
    
    if familia.find('div') != None:
        ttl += f'\n              :divorcio "{familia.find("div").text}" ;'
    
    if pessoa.find("chil") != None:
        ttl += "\n              :crianca "
        for child in pessoa.findall("chil"):
                ttl += f'"{child.text}" ,\n                       '
           
        ttl = ttl[:-25] + " ;"
    

    ttl += "\n"


ttl += "\n\n"
    
    
print(ttl)
