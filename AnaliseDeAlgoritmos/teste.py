import re


def le_assinatura():
    '''A funcao le os valores dos tracos linguisticos do modelo e devolve uma assinatura a ser comparada com os textos fornecidos'''
    print("Bem-vindo ao detector automático de COH-PIAH.")
    print("Informe a assinatura típica de um aluno infectado:")

    wal = float(input("Entre o tamanho médio de palavra:"))
    ttr = float(input("Entre a relação Type-Token:"))
    hlr = float(input("Entre a Razão Hapax Legomana:"))
    sal = float(input("Entre o tamanho médio de sentença:"))
    sac = float(input("Entre a complexidade média da sentença:"))
    pal = float(input("Entre o tamanho medio de frase:"))

    return [wal, ttr, hlr, sal, sac, pal]


def le_textos():
    '''A funcao le todos os textos a serem comparados e devolve uma lista contendo cada texto como um elemento'''
    i = 1
    textos = []
    texto = input("Digite o texto " + str(i) + " (aperte enter para sair):")
    while texto:
        textos.append(texto)
        i += 1
        texto = input("Digite o texto " + str(i) + " (aperte enter para sair):")

    return textos


def separa_sentencas(texto):
    '''A funcao recebe um texto e devolve uma lista das sentencas dentro do texto'''
    sentencas = re.split(r'[.!?]+', texto)
    if sentencas[-1] == '':
        del sentencas[-1]
    return sentencas


def separa_frases(sentenca):
    '''A funcao recebe uma sentenca e devolve uma lista das frases dentro da sentenca'''
    return re.split(r'[,:;]+', sentenca)


def separa_palavras(frase):
    '''A funcao recebe uma frase e devolve uma lista das palavras dentro da frase'''
    return frase.split()


def n_palavras_unicas(lista_palavras):
    '''Essa funcao recebe uma lista de palavras e devolve o numero de palavras que aparecem uma unica vez'''
    freq = dict()
    unicas = 0
    for palavra in lista_palavras:
        p = palavra.lower()
        if p in freq:
            if freq[p] == 1:
                unicas -= 1
            freq[p] += 1
        else:
            freq[p] = 1
            unicas += 1

    return unicas


def n_palavras_diferentes(lista_palavras):
    '''Essa funcao recebe uma lista de palavras e devolve o numero de palavras diferentes utilizadas'''
    freq = dict()
    for palavra in lista_palavras:
        p = palavra.lower()
        if p in freq:
            freq[p] += 1
        else:
            freq[p] = 1

    return len(freq)


def compara_assinatura(as_a, as_b):
    sab = 0
    for i in range(6):
        sab = sab + abs(as_a[i] - as_b[i])
    return (sab / 6)


def calcula_assinatura(texto):
    wal = 0
    lista_palavras = []

    for i in separa_sentencas(texto):
        for j in separa_frases(i):
            for k in separa_palavras(j):
                lista_palavras.append(k)

    for i in lista_palavras:
        wal = wal + len(i)
    wal = wal / len(lista_palavras)

    ttr = n_palavras_diferentes(lista_palavras) / len(lista_palavras)

    hlr = n_palavras_unicas(lista_palavras) / len(lista_palavras)

    sal = 0
    for i in separa_sentencas(texto):
        sal = sal + len(i)
    sal = sal / len(separa_sentencas(texto))

    sac = 0

    for i in separa_sentencas(texto):
        sac = sac + len(separa_frases(i))
    sac = sac / len(separa_sentencas(texto))

    pal = 0

    n_frases = 0
    for i in separa_sentencas(texto):
        n_frases = n_frases + len(separa_frases(i))

    for i in separa_sentencas(texto):
        for j in separa_frases(i):
            pal = pal + len(j)
    pal = pal / n_frases
    return [wal, ttr, hlr, sal, sac, pal]

def avalia_textos(textos, ass_cp):
    maior_prob = 1000000000000
    id = 0
    for i in textos:
        id = id + 1
        if (maior_prob > compara_assinatura(calcula_assinatura(i), ass_cp)):
            maior_prob = compara_assinatura(calcula_assinatura(i), ass_cp)
            maior_id = id

    return (maior_id)


ass_cp = le_assinatura()

textos = le_textos()

maior = avalia_textos(textos, ass_cp)

print('O autor do texto', maior, 'está infectado com COH-PIAH')
print()
print(maior == 2)
