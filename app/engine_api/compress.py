import spacy
import pytextrank
# import en_core_web_lg
from flask import json, jsonify, request

from .preProcess import PreProcess


class Compress:

    def __init__(self):
        print("Hello compress")
        try:
            self.nlp = spacy.load("en_core_web_sm")
            tr = pytextrank.TextRank()
            self.nlp.add_pipe(tr.PipelineComponent, name='textrank', last=True)
        except Exception as e:
            print("on create "+e)


    def getCheck(self,text):
        return text

    def compress(self,text):
        print("hey bro Compress "+text)
        text = PreProcess().process(text)
        doc = self.nlp(text)
        res = {'spacy': '', 'bert': ''}
        res['spacy'] = []
        print("No of words " + str(len(text.split())))

        len_words = len(text.split())
        if len_words<10:
            print("case1 ")
            res['spacy'].append(self.removeStopWords_1(doc))
        elif len_words<50:
            print("case2 ")
            res['spacy'].append(self.removeStopWords_1(doc))
            res['spacy'].append(self.nounChunks_2(doc))
        elif len_words<80:
            print("case3 ")
            res['spacy'].append(self.removeStopWords_1(doc))
            res['spacy'].append(self.nounChunks_2(doc))
            result = self.textRank_3(doc,1)
            for i in range(len(result)):
                res['spacy'].append(result[i])


        elif len_words<100:
            print("case4 ")
            res['spacy'].append(self.removeStopWords_1(doc))
            res['spacy'].append(self.nounChunks_2(doc))
            result = self.textRank_3(doc,2)
            for i in range(len(result)):
                res['spacy'].append(result[i])
        else:
            print("case5 ")
            res['spacy'].append(self.removeStopWords_1(doc))
            res['spacy'].append(self.nounChunks_2(doc))
            result = self.textRank_3(doc,3)
            for i in range(len(result)):
                res['spacy'].append(result[i])


        # res['spacy'].append(self.textRank_3(doc))
        # res['spacy'].append(self.nounChunks_2(doc))
        # res['spacy'].append(self.removeStopWords_1(doc))
        res['bert'] = ''

        return jsonify(res)

    def removeStopWords_1(self,doc):
        words =[]
        try:
            for token in doc:
                if not token.is_stop:
                    # print(token.text)
                    words.append(token.text)
        except Exception as e:
            print(e)
        return words

    def nounChunks_2(self,doc):
        words =[]
        try:
            for chunk in doc.noun_chunks:
                # print(chunk.root.text)
                words.append(chunk.root.text)
        except Exception as e:
            print(e)
        return words

    def textRank_3(self,doc,level):
        result = []
        words =[]
        try:
            for p in doc._.phrases:
                words.append(p.text)

            count = pow(2,level)
            i=1
            while(i<count) :
                print("the count "+str(i))
                result.append(words[:int(len(words)/i)])
                i = i*2
        except Exception as e:
            print(e)
        return result