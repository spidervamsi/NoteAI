import spacy
import pytextrank
import en_core_web_sm
from flask import json, jsonify, request

from .preProcess import PreProcess


class Compress:

    def __init__(self):
        print("Hello compress")
        self.nlp = en_core_web_sm.load()
        tr = pytextrank.TextRank()
        self.nlp.add_pipe(tr.PipelineComponent, name='textrank', last=True)

    def compress(self,text):
        print("hey bro Compress "+text)
        text = PreProcess().process(text)
        doc = self.nlp(text)
        words1 = []
        words2 = []
        words3 = []
        res = {'spacy': '', 'bert': ''}
        res['spacy'] = []
        print("No of words " + str(len(text.split())))

        len_words = len(text.split())
        if len_words<10:
            print("less than 10")
        elif len_words<30:
            print("less than 30")
        elif len_words<50:
            print("less than 50")
        elif len_words<100:
            print("less than 100")
        else:
            print("above 100")

        try :
            for token in doc:
                if not token.is_stop:
                    # print(token.text)
                    words1.append(token.text)

            for chunk in doc.noun_chunks:
                # print(chunk.root.text)
                words2.append(chunk.root.text)

            for p in doc._.phrases:
                # print(str(p.rank) + " " + p.text)
                words3.append(p.text)

            res['spacy'].append(words3)
            res['spacy'].append(words2)
            res['spacy'].append(words1)
            res['bert'] = ''

        except Exception as e:
            print(e)

        return jsonify(res)