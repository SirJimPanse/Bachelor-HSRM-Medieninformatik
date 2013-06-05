import types, random
def gen():
    i = 1
    while True:
        yield i
        yield i+3
        i += 1

def secondonly(gen):
    i = 0
    l = []
    if type(gen) == types.ListType:
        while i < len(gen):
            if gen[i] in gen[:i] and gen[i] not in l:
                l.append(gen[i])
            i += 1
        for k in l:
            yield k        
    else:
        while True:
            g = gen.next()
            l.append(g)
            if l.count(g) == 2: yield g

def count(gen):
    i = 1
    l = []
    if type(gen) == types.ListType:
        for x in gen:
            l.append(x)
            yield '('+":".join((str(x),str(l.count(x))))+')'
    else:
        while True:
            g = gen.next()
            l.append(g)
            yield '('+":".join((str(g),str(l.count(g))))+')'
    
