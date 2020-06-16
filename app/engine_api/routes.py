from flask import json, jsonify, request
from . import engine_api_blueprint


@engine_api_blueprint.route("/get", methods=['GET'])
def swagger_api_docs_yml():

    return "heybro"