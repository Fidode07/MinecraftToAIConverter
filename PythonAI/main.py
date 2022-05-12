# MADE BY Fido_de07 (Fido_de02), ChatBot Conzept is from NeuralNet!
# My Discord is: Fido_de02#9227, write me if you have any Questions (Like: Why is ur English so bad?).
# Before i forgot/forget (i dont know). THIS IS THE FILE YOU HAVE TO START TO RUN THE AI!

import json
from random import randrange
import socket
import torch
from ChatBot.model import NeuralNet
from ChatBot.nltk_utils import bag_of_words, tokenize

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

if torch.cuda.is_available():
    print("WE USE GRAPHICS CARD, CUZ CUDA IS AVAILABLE!")
else:
    print("Let's try to run this Shit on our CPU ...")


with open('ChatBot/intents.json', 'r', encoding='utf-8') as json_data:
    intents = json.load(json_data)


FILE = "ChatBot/model.pth"
data = torch.load(FILE)

input_size = data["input_size"]
hidden_size = data["hidden_size"]
output_size = data["output_size"]
all_words = data['all_words']
tags = data['tags']
model_state = data["model_state"]

model = NeuralNet(input_size, hidden_size, output_size).to(device)
model.load_state_dict(model_state)
model.eval()


s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(('', 7777))
s.listen(1)


while True:
    print("On Receive ...")
    # Warte auf eine Verbindung zum Server
    conn, (remotehost, remoteport) = s.accept()
    print('Verbunden mit %s:%s' % (remotehost, remoteport))

    data = conn.recv(1024)
    msg = data.decode()
    msg = msg.replace("\x00\x10", "")
    print("Sended: %s" % msg)
    print(f"Encoded: {data}")
    if msg is None or msg == "" or msg == " ":
        print("HAVE TO RETURN!")
        pass

    print("after the check!")
    conn.send(b"RETURN")
    conn.close()
    msg = msg.replace("'", "")
    msg = msg.replace("\x00", "")
    msg = msg.replace("\x13", "")
    msg = msg.replace("\x00\x13", "")

    sentence = tokenize(msg.lower())
    X = bag_of_words(sentence, all_words)
    X = X.reshape(1, X.shape[0])
    X = torch.from_numpy(X).to(device)

    output = model(X)
    _, predicted = torch.max(output, dim=1)

    tag = tags[predicted.item()]

    probs = torch.softmax(output, dim=1)
    prob = probs[0][predicted.item()]

    print("Genauigkeit: " + str(prob.item()))

    if prob.item() > 0.96:
        for intent in intents['intents']:
            if tag == intent["tag"]:
                if intent['tag'] == "greeting":
                    print("Er hat uns begrüßt!")
                elif intent['tag'] == "bye":
                    print("Er hat sich verabschieded!")

                if intent['tag'] == "stopword":
                    response = "NONERESP"
                else:
                    try:
                        response = intent['responses'][randrange(len(intent['responses']))]
                    except Exception as e:
                        print(e)
                        print("Send Error Message ...")
                        response = "§cSorry, there was a Error!"

                print("REPONSE: " + response)
                host = "localhost"
                # Ein INet Streaming (TCP/IP) Socket erzeugen
                s2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                # Zum Server verbinden
                s2.connect((host, 4839))
                # Unsere Nachricht senden
                s2.send(str(response).encode())
                # Verbindung trennen
                s2.close()
    else:
        print("WE NOT SURE AND ITS NOT A QUESTION!")
        response = "Tut mir leid, ich bin leider noch nicht weit genug entwickelt, um das zu verstehen. " \
                   "Allerdings werde ich versuchen, das, was du gesagt hast, irgendwann verstehen zu können. " \
                   "Bitte vergiss dabei aber nicht, dass ich zurzeit nur dazu da bin, um mit dir zu handeln! "
        host = "localhost"
        # Ein INet Streaming (TCP/IP) Socket erzeugen
        s2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        # Zum Server verbinden
        s2.connect((host, 4839))
        # Unsere Nachricht senden
        s2.send(str(response).encode())
        # Verbindung trennen
        s2.close()
