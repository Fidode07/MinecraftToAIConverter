import goslate

primary_text = 'Es wird langsam Zeit!'
gs = goslate.Goslate()
translatet = gs.translate(primary_text, 'en')
print(translatet)
