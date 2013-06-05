def gen():
    i = 0
    while True:
        yield i
        i += 1

def lucasfolge():
    z = 1
    i = 2
    j = int()
    yield i
    yield z
    while True:
        yield z + i
        j = z
        z = z + i
        i = j

def pellfolge():
    z = 1
    i = 0
    j = int()
    yield i
    yield z
    while True:
        yield i + z*2
        j = z
        z = i + z*2
        i = j

def generatormax(gen, anzahl):
    z = 0
    while z != anzahl:
        yield gen.next()
        z += 1

def generatorsprung(gen, lis):
    if lis == []:
        print "LEER"
    else:
        yield gen.next()
        for i in lis:
            k = 0
            while k != i:
                gen.next()
                k += 1
            yield gen.next()

def generatorrueck(gen, start, stop):
    z = 0
    l = []
    while z != stop:
        l.append(gen.next())
        z += 1
    for i in list(reversed(l))[:-start]:
        yield i
    
        
        
        


        
        
    
