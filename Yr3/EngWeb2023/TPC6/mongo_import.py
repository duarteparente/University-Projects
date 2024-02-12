import json
import requests

file = open('pessoas.json')
pessoas = json.load(file)['pessoas']

url = 'http://localhost:7777/pessoas'

for p in pessoas:
    del p['id']
    if 'descrição' in p:
        p['descricao'] = p['descrição']
        del p['descrição']
    if 'BI' in p:
        p['_id'] = p['BI']
    else:
        p['_id'] = p['CC']
    res = requests.post(url,json=p)
