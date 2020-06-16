from flask import json, jsonify, request
from . import engine_api_blueprint


@engine_api_blueprint.route("/get", methods=['GET'])
def simple_get():
    str = {'name':''}
    str['name'] = "velivela vamsi krishna"
    return jsonify(str)

@engine_api_blueprint.route("/post", methods=['POST'])
def simple_post():
    print(request.get_json())
    str = {'name':''}
    str['name'] = "velivela vamsi krishna"
    return jsonify(str)