from deep_translator import GoogleTranslator

translated = GoogleTranslator(source='de', target='en').translate("Kannste mir ein paar Items besorgen?")
print(translated)
