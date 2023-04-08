def lowestComumSubsequence(X, Y):
    m = len(X)
    n = len(Y)
    c = []
    l = []
    for i in range(n + 1):
        c.append([])
        l.append([])
        for j in range(m + 1):
            c[i].append(0)
            l[i].append(' ')

    for i in range(1, n + 1):
        for j in range(1, m + 1):
            if X[j - 1] == Y[i - 1]:
                c[i][j] = c[i - 1][j - 1] + 1
                l[i][j] = l[i - 1][j - 1] + X[j - 1]
                print("Y[",i - 1, "], X[", j - 1,"]=", X[j - 1])
            else:
                c[i][j] = max(c[i - 1][j], c[i][j - 1])
                if c[i - 1][j] >= c[i][j - 1]:
                    l[i][j] = l[i - 1][j]
                else:
                    l[i][j] = l[i][j - 1]
    for i in range(n + 1):
        print(c[i])
    print()
    for i in range(n + 1):
        print(l[i])
    print()
    return c[n][m]

if __name__ == '__main__':
    X = ['A', 'B' ,'R', 'A', 'C' ,'A', 'D' ,'A' ,'B' ,'R','A']
    Y = ['Y' ,'A' ,'B' ,'B' ,'A' ,'D' ,'A' ,'B' ,'B' ,'A' ,'D ','D' ,'O' ',O', 'lau']

    print(lowestComumSubsequence(X, Y))