import json

pessoas = json.load(open("dataset.json"))

for i, p in enumerate(pessoas['pessoas']):
    p['id'] = f"p{i}"

modified = json.dumps(pessoas, indent=2, ensure_ascii=False)
with open("dataset.json", "w", encoding='utf-8') as outfile:
    outfile.write(modified)