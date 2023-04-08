def mediana(n):
    n = sorted(n)
    t = int(len(n))
    if n%2 == 0:
        med = n[int(t/2)] + n[int(t/2 + 1)]
        med = med/2
    else:
        med = n[int(t/2)]
    return med

def medianaLinear(n, p, r, i):
    if p == r:
        return p
    q = particionaMedLinear(n, p, r)
    k = q - p + 1

    if k == i:
        return q
    if k > i:
        return medianaLinear(n, p, q - 1, i)
    return medianaLinear(n, q + 1, r, i - k)

def particionaMedLinear(n, p, r):
    for j in range(p, p + 5*(int(n/5) - 1), 5):
        sorted(n)
