import sys

import numpy as np
import nltk
from nltk.stem.porter import PorterStemmer
import json

nltk.download('punkt')
stemmer = PorterStemmer()

settings = []
global dataBack
dataBack = None
with open("config.json", "r") as f:
    dataBack = json.load(f)

for abschnitt in dataBack['settings']:
    settings.append(abschnitt)

try:
    mySetting = settings[0]
except IndexError:
    print("There was a Error by Parsing the Config.json!")
    sys.exit()

print("full_word_language: " + mySetting["full_word_language"])


def tokenize(sentence):
    """
    split sentence into array of words/tokens
    a token can be a word or punctuation character, or number
    """
    return nltk.word_tokenize(sentence, language=mySetting["full_word_language"])


def stem(word):
    """
    stemming = find the root form of the word
    examples:
    words = ["organize", "organizes", "organizing"]
    words = [stem(w) for w in words]
    -> ["organ", "organ", "organ"]
    """
    return stemmer.stem(word.lower())


def bag_of_words(tokenized_sentence, words):
    """
    return bag of words array:
    1 for each known word that exists in the sentence, 0 otherwise
    example:
    sentence = ["hello", "how", "are", "you"]
    words = ["hi", "hello", "I", "you", "bye", "thank", "cool"]
    bog   = [  0 ,    1 ,    0 ,   1 ,    0 ,    0 ,      0]
    """

    sentence_words = [stem(word) for word in tokenized_sentence]

    bag = np.zeros(len(words), dtype=np.float32)
    for idx, w in enumerate(words):
        if w in sentence_words: 
            bag[idx] = 1

    return bag
