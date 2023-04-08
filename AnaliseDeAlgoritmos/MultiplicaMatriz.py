import math

#Utilizando programação dinâmica
def MMmelhor(p, n):
    inf = 2**100
    m = []
    for i in range(n):
        m.append([])
        for j in range(n):
            m[i].append(0)

    tup = []
    for I in range(1, n):
        for i in range(n - I):
            j = I + i
            m[i][j] = inf
            for k in range(i, j):
                q = m[i][k] + p[i] * p[k + 1] * p[j + 1] + m[k + 1][j]
                if q < m[i][j]:
                    m[i][j] = q
                    tup = [m[i][k] , p[i] , p[k + 1] , p[j + 1] , m[k + 1][j], q]
                for l in range(0, 5, 4):
                    if tup[l] == 0:
                        tup[l] = ''
                    elif type(tup[l]) != str or len(tup[l]) == 1:
                        tup[l] = (str)(tup[l]) + " + "
            print("m [",i,",",j,"] =",tup[0],tup[4], tup[1],"*",tup[2],"*",tup[3],"(",tup[1]*tup[2]*tup[3],") =",tup[5])
    for i in m:
        print(i)
    return m[0][n - 1]

#Utilizando recursão
def MMrec(p, i, j):
    if i >= j:
        return 0

    minimo = 2**1000
    tup = [0, 0, 0]
    for k in range(i, j):
        q1 = MMrec(p, i, k)
        q2 = MMrec(p, k + 1, j)
        q = q1 + q2 + p[i - 1]*p[k]*p[j]
        minimo = min(minimo, q)
        if minimo == q:
            tup = [i - 1, k, j]
    return minimo

if __name__ == '__main__':
    p = [10, 100, 5, 50]
    p = [5, 10, 3, 12, 5, 50, 6]
    print(MMrec(p, 1, len(p) - 1))

    print(MMmelhor(p, len(p) - 1))