def f(a, b, c=1):
    print a, b, c


def g(a, b, *c, **d):
    print a, b, c, d


def h(a, b, c=1, *d, **e):
    print a, b, c, d, e
