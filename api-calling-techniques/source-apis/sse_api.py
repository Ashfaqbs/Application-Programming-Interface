from flask import Flask, jsonify, Response
import time
import json

import random

app = Flask(__name__)

# Simulate some data changes every 5 seconds
def generate_data():
    while True:
        time.sleep(5)  # Simulate data change every 5 seconds
        yield f"data: {json.dumps({'timestamp': time.time(), 'value': random.randint(0, 100)})}\n\n"

@app.route('/sse')
def sse():
    return Response(generate_data(), content_type='text/event-stream')

if __name__ == '__main__':
    app.run(port=5000)
