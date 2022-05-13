import numpy as np
import json
import torch
import torch.nn as nn
from torch.utils.data import Dataset, DataLoader

from nltk_utils import bag_of_words, tokenize, stem
from model import NeuralNet


def train_a_model(lang: str):
    try:
        with open('Models/'+lang+".json", 'r', encoding='utf-8') as f:
            intents = json.load(f)

        all_words = []
        tags = []
        xy = []

        for intent in intents['intents']:
            tag = intent['tag']

            tags.append(tag)
            for pattern in intent['patterns']:
                w = tokenize(pattern)

                all_words.extend(w)

                xy.append((w, tag))

        ignore_words = ['?', '.', '!']
        all_words = [stem(w) for w in all_words if w not in ignore_words]

        all_words = sorted(set(all_words))
        tags = sorted(set(tags))

        print(len(xy), "patterns")
        print(len(tags), "tags:", tags)
        print(len(all_words), "unique stemmed words:", all_words)
        print(all_words)

        X_train = []
        y_train = []
        for (pattern_sentence, tag) in xy:
            bag = bag_of_words(pattern_sentence, all_words)
            X_train.append(bag)

            label = tags.index(tag)
            y_train.append(label)

        X_train = np.array(X_train)
        y_train = np.array(y_train)

        num_epochs = 2000
        batch_size = 64
        learning_rate = 0.001
        input_size = len(X_train[0])
        hidden_size = 64
        output_size = len(tags)
        print(input_size, output_size)


        class ChatDataset(Dataset):

            def __init__(self):
                self.n_samples = len(X_train)
                self.x_data = X_train
                self.y_data = y_train

            def __getitem__(self, index):
                return self.x_data[index], self.y_data[index]

            def __len__(self):
                return self.n_samples


        dataset = ChatDataset()
        train_loader = DataLoader(dataset=dataset,
                                  batch_size=batch_size,
                                  shuffle=True,
                                  num_workers=0)

        device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

        if torch.cuda.is_available():
            print("WE USE GRAPHICS CARD, CUZ CUDA IS AVAILABLE!")
        else:
            print("Let's try to run this Shit on our CPU ...")

        model = NeuralNet(input_size, hidden_size, output_size).to(device)

        criterion = nn.CrossEntropyLoss()
        optimizer = torch.optim.Adam(model.parameters(), lr=learning_rate)

        for epoch in range(num_epochs):
            for (words, labels) in train_loader:
                words = words.to(device)
                labels = labels.to(dtype=torch.long).to(device)

                outputs = model(words)

                loss = criterion(outputs, labels)

                optimizer.zero_grad()
                loss.backward()
                optimizer.step()

            if (epoch + 1) % 100 == 0:
                print(f'Epoch [{epoch + 1}/{num_epochs}], Loss: {loss.item():.4f}')
                f = open("logs.txt","a+")
                f.write(f'Epoch [{epoch + 1}/{num_epochs}], Loss: {loss.item():.4f}'+"\n")
                f.close()

        data = {
            "model_state": model.state_dict(),
            "input_size": input_size,
            "hidden_size": hidden_size,
            "output_size": output_size,
            "all_words": all_words,
            "tags": tags
        }

        FILE = 'Models/'+lang+".pth"
        torch.save(data, FILE)

        print(f'final loss: {loss.item():.4f}')
        print(f'training complete. file saved to {FILE}')
    except Exception as e:
        print(str(e))