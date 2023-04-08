import random


def medianas(A):
    B = sorted(A)
    x = B[int(len(B) / 2)]
    return x


def particiona(A, p, r):
    x = A[r]
    i = p - 1
    for j in range(p, r):
        if A[j] <= x:
            i += 1
            aux = A[i]
            A[i] = A[j]
            A[j] = aux
    aux = A[i + 1]
    A[i + 1] = A[r]
    A[r] = aux
    return i + 1


def selecNil(A, p, r, i):
    if p > r:
        print(p)
        return
    if p == r:
        return A[p]
    q = particiona(A, p, r)
    k = q
    if i == k:
        return A[q]
    if i < k:
        return selecNil(A, p, q - 1, i)
    return selecNil(A, q + 1, r, i)


if __name__ == '__main__':
    A = []
    for i in range(11):
        A.append(random.randint(1, 100))
    print(int(len(A) / 2))
    print(sorted(A))
    B = sorted(A)
    print(B[int(len(A) / 2)])
    print(selecNil(A, 0, len(A) - 1, int(len(A) / 2)))