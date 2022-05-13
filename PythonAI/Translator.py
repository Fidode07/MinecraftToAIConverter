import json
from deep_translator import GoogleTranslator


class MyCustomTranslator:
    def __init__(self) -> None:
        print("ModelTranslator was booted!")

    def translateModel(self, lang: str):
        abschnitte = []
        data = None

        def translate(inputsetence: str, lang: str):
            translated = GoogleTranslator(source="de", target=lang).translate(inputsetence)
            print(translated)
            return translated

        with open('Models/de.json', 'r') as f:
            data = json.load(f)

        with open("Models/"+lang+".json", "w") as f:
            f.write("""{\n  "intents": [""")

        print(" ")

        for absch in data['intents']:
            print(absch)
            abschnitte.append(absch)

        i = 0
        for abschnitt in abschnitte:
            i+=1
            with open("Models/"+lang+".json", "a") as f:
                print(" ")
                print(" ")
                print("I: " + str(i))
                print("Len: " + str(len(abschnitte)))
                print(" ")
                if i >= len(abschnitte):
                    f.write('\n    {\n      "tag": "'+abschnitt["tag"]+'",')
                    f.write('\n      "patterns": [')
                    curI = 0
                    patterLen = len(abschnitt['patterns'])
                    for pattern in abschnitt['patterns']:
                        curI+=1
                        print(" ----- ")
                        enPattern = translate(pattern, lang=lang)
                        print("Translate: " + pattern)
                        print(" ----- ")
                        if curI >= patterLen:
                            f.write('\n        "'+str(enPattern)+'"')
                        else:
                            f.write('\n        "'+str(enPattern)+'",')

                    f.write("\n      ],\n      \"responses\": [")

                    curI = 0
                    respLen = len(abschnitt["responses"])
                    for response in abschnitt['responses']:
                        curI+=1
                        print(" ----- ")
                        enReponse = translate(response, lang=lang)
                        print("Translate: " + response)
                        print(" ----- ")
                        print(" ")
                        if curI >= respLen:
                            f.write('\n        "'+str(enReponse)+'"')
                        else:
                            f.write('\n        "'+str(enReponse)+'",')
                    f.write("\n      ]\n    }")
                else:
                    f.write('\n    {\n      "tag": "'+abschnitt["tag"]+'",')
                    f.write('\n      "patterns": [')
                    curI = 0
                    patterLen = len(abschnitt['patterns'])
                    for pattern in abschnitt['patterns']:
                        curI+=1
                        print(" ----- ")
                        enPattern = translate(pattern, lang=lang)
                        print("Translate: " + pattern)
                        print(" ----- ")
                        print(" ")
                        if curI >= patterLen:
                            f.write('\n        "'+str(enPattern)+'"')
                        else:
                            f.write('\n        "'+str(enPattern)+'",')

                    f.write("\n      ],\n      \"responses\": [")

                    curI = 0
                    respLen = len(abschnitt["responses"])
                    for response in abschnitt['responses']:
                        curI+=1
                        print(" ----- ")
                        enReponse = translate(response, lang=lang)
                        print("Translate: " + response)
                        print(" ----- ")
                        print(" ")
                        if curI >= respLen:
                            f.write('\n        "'+str(enReponse)+'"')
                        else:
                            f.write('\n        "'+str(enReponse)+'",')

                    f.write("\n      ]\n    },")

        with open("Models/"+lang+".json", "a") as f:
            f.write("""\n  ]\n}""")
