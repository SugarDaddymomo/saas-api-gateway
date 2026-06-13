from flask import Flask, request, jsonify

app = Flask(__name__)

@app.get("/orders/<id>")
def get_order(id):
    return jsonify({
        "orderId": id,
        "status": request.args.get("status"),
        "page": request.args.get("page")
    })

@app.get("/orders/error")
def error():
    return {"message": "not found"}, 404

@app.route("/orders", methods=["POST"])
def create_order():
    return jsonify({
        "received": request.get_json(silent=True)
    })

@app.get("/headers")
def headers():
    return jsonify({
        "authorization": request.headers.get("Authorization"),
        "correlationId": request.headers.get("X-Correlation-Id")
    })

app.run(port=9000)