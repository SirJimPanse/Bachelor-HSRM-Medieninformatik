
def add(x): return x+1
def mul(x): return x*2
def summe(*p): return sum(p)

def hintereinander(*funcs):
    def f(x):
        for func in reversed(funcs):
            x = func(x)
        return x
    return f

def partiell(func, *args):
    def f(*x):
        return func(*args+x)
    return f
        

def partiell2(func, *args):
    def f(*x):
        z = 0
        for i in args:
            z += func(i)
        for y in x:
            z += func(y)
        return z
    return f

g = hintereinander(add, mul)
print g(3)
g = hintereinander(mul, add)
print g(3)

g = partiell(summe, 1,2,3)
print g(4,5,6)

