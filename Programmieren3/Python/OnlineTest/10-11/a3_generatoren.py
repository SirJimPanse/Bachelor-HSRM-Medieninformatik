
def gen():
    z = 0
    while True:
        yield z
        z += 1

def gen2():
    z = 10
    while True:
        yield z
        z += 5

def test(x):
    return x%2 == 0
        
def vielfach(g1,n):
    while True:
        i = 0
        g = g1.next()
        while i < n:
            yield g
            i += 1

def nmischen(g1, n ,g2):
    while True:
        i = 0
        g = g1.next()
        a = g2.next()
        while i < n:
            yield g
            i += 1
        yield a

def getestet(g1, test):
    while True:
        g = g1.next()
        while test(g) == False:
            g = g1.next()
        yield g
