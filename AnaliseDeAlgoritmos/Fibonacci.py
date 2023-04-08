def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)

def fibonacciPD(n):
    f = []
    f.append(0)
    f.append(1)

    for i in range(2, n + 1):
        f.append(f[i - 1] + f[i - 2])
    return f[n]

if __name__ == '__main__':
    n = 34
    for i in range(10):
        fibonacciPD(10000)
    print(fibonacciPD(n))
    print(fibonacci(n))
