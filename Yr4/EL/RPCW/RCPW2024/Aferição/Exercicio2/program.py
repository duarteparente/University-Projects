from flask import Flask, request
import requests

app = Flask(__name__)

## GraphDB Endpoint
graphdb_endpoint = "http://localhost:7200/repositories/Mapa"




if __name__== "__main__":
    app.run(debug=True)