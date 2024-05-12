from PIL import Image
import graphviz, io, json, sys


with open(f'../data/graph/{sys.argv[1]}.txt') as dot_file:
    data = dot_file.read()

graph = graphviz.Source(data)
imagem = Image.open(io.BytesIO(graph.pipe(format='png')))
imagem.save(f'../output/graph/{sys.argv[1]}.png')