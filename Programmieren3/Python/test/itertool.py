from itertools import *
import types

def gen():
    i = 0
    while True:
        yield i
        i += 1


def eislice(gen, start , stop = None, step = None):
    l = []
    if stop == None:
        stop = start
        start = 0
        
    if type(gen) == types.GeneratorType:
        for i in range(start,stop):
            l.append(gen.next())
        l = l[::step]
    else:
        for i in gen[start:stop:step]:
            l.append(i)          
    
    for i in l:
    
    yield i

print "islice: ", list(islice(gen(),0,10,2))
print "eislice: ", list(eislice(gen(),0,10,2))

print "islice: ", list(islice("abcd",1,3))
print "eislice: ", list(eislice("abcd",1,3))

def eimap(func, a, b, *x):   
    l = map(lambda x, y: func(x, y), a, b)
    for i in l:
        yield i
        
print "imap: ", list(imap(pow, eislice(gen(),0, 10, 2), 
						   eislice(gen(), 0, 10, 2)))
print "eimap: ", list(eimap(pow, eislice(gen(), 0, 10, 2), 
							 eislice(gen(), 0, 10, 2)))

print "imap: ", list(imap(pow, (2, 3, 10), [5, 2, 3]))
print "eimap: ", list(eimap(pow, (2, 3, 10), [5, 2, 3]))

def eifilter(boole, lis):
    l = [x for x in lis if boole(x)] 
    for i in l:
        yield i

print "ifilter: ", list(ifilter(lambda x: x % 2, eislice(gen(), 10)))
print "eifilter: ", list(eifilter(lambda x: x % 2, eislice(gen(), 10)))


    
