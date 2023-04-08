def mochilaPD(n, w, v, W):
    n += 1
    t = []
    for i in range(n + 1):
        t.append([])
        for j in range(W + 1):
            t[i].append(0)

    for Y in range(W + 1):
        t[0][Y] = 0
        for i in range(1, n):
            a = t[i - 1][Y]
            if w[i - 1] > Y:
                b = 0
            else:
                b = t[i - 1][Y - w[i - 1]] + v[i - 1]
            t[i][Y] = max(a, b)
        for j in range(n):
            print(t[j])
        print()
    return t[n][W]


if __name__ == '__main__':
    w = [4, 2, 1, 3]
    v = [50, 20, 30, 450]
    n = len(w)
    W = 5
    mochilaPD(n, w, v, W)
