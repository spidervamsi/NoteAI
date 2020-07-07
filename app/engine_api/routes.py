from flask import json, jsonify, request
from . import engine_api_blueprint
from nltk import sent_tokenize, word_tokenize
import nltk
nltk.download('punkt')
import spacy
import pytextrank
# import pickle
import en_core_web_sm
# from summarizer import Summarizer

nlp = en_core_web_sm.load()
filename = '../model/bertModel.sav'
# loaded_model = pickle.load(open(filename, 'rb'))
# model = Summarizer()

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
    print("reached here")
    req = request.get_json()
    text = req['text']
    doc = nlp(text)
    words1 = []
    words2 = []
    words3 = []
    res = {'spacy': '', 'bert': ''}
    res['spacy'] = {'layer1': '', 'layer2': '', 'layer3': ''}
    try :
        for token in doc:
            if not token.is_stop:
                print(token.text)
                words1.append(token.text)

        for chunk in doc.noun_chunks:
            print(chunk.root.text)
            words2.append(chunk.root.text)

        for p in doc._.phrases:
            print(str(p.rank) + " " + p.text)
            words3.append(p.text)

        res['spacy']['layer3'] = words3
        res['spacy']['layer2'] = words2
        res['spacy']['layer1'] = words1
        res['bert'] = ''

    except Exception as e:
        print(e)

    return jsonify(res)