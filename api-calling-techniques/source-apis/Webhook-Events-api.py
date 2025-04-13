# webhook_sender.py
from flask import Flask
import requests
import time
import random

app = Flask(__name__)

WEBHOOK_URL = "http://localhost:8080/webhook"

@app.route('/trigger', methods=['GET'])
def trigger_webhook():
    data = {
        "timestamp": time.time(),
        "value": random.randint(1, 100)
    }
    try:
        res = requests.post(WEBHOOK_URL, json=data)
        return f"Webhook sent, status: {res.status_code}"
    except Exception as e:
        return f"Error sending webhook: {str(e)}"

if __name__ == '__main__':
    app.run(port=5000)
