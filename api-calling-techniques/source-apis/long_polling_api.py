from flask import Flask, jsonify
import time
import threading
import random

app = Flask(__name__)

# Simulate some global data that changes
latest_value = {"timestamp": time.time(), "value": random.randint(0, 100)}

# Background thread to update the value every 10 seconds
def update_data():
    global latest_value
    while True:
        time.sleep(10)
        latest_value = {"timestamp": time.time(), "value": random.randint(0, 100)}

threading.Thread(target=update_data, daemon=True).start()

@app.route('/long-poll')
def long_poll():
    start_time = time.time()
    initial_value = latest_value["value"]

    # Wait for up to 15 seconds for a value change
    while time.time() - start_time < 15:
        if latest_value["value"] != initial_value:
            break
        time.sleep(1)

    return jsonify(latest_value)

if __name__ == '__main__':
    app.run(port=5000)
