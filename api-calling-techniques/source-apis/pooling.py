from flask import Flask, jsonify
import time
import random

app = Flask(__name__)

# Simulate changing data
@app.route("/data")
def get_data():
    current_timestamp = int(time.time())
    return jsonify({
        "timestamp": current_timestamp,
        "value": random.randint(1, 100)
    })

if __name__ == "__main__":
    app.run(port=5000, debug=True)
