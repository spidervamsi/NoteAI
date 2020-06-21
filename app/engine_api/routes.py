from flask import json, jsonify, request
from . import engine_api_blueprint
from nltk import sent_tokenize, word_tokenize
import nltk
nltk.download('punkt')
import spacy
import pytextrank
# import pickle
import en_core_web_sm
from summarizer import Summarizer

nlp = en_core_web_sm.load()
filename = '../model/bertModel.sav'
# loaded_model = pickle.load(open(filename, 'rb'))
model = Summarizer()

tr = pytextrank.TextRank()
nlp.add_pipe(tr.PipelineComponent, name='textrank', last=True)


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

@engine_api_blueprint.route("/summarize", methods=['POST'])
def summarize_post():
    req = request.get_json()
    text = req['text']
    doc = nlp(text)
    words = []
    for p in doc._.phrases:
        print(str(p.rank) + " " + p.text)
        words.append(p.text)
    res = {'spacy':'','bert':''}
    res['spacy'] = words
    # res['bert'] = loaded_model(text, 0.2)
    return jsonify(res)