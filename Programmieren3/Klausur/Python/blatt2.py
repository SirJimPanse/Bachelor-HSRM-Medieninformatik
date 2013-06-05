# -*- coding: utf-8 -*-

"""for als while Schleife , while als for-Schleife A2"""
l = [1,2,3]
i = 0
while(i < len(l)):
        print l[i]
        i += 1

d = {1:"EINS",2:"ZWEI",3:"DREI"}
for key, value in d.items():
    print key, value

"""ggT Aufgaben A2"""
def ggTr(x,y):
    if x == y:
        return x
    elif x > y:
        return ggTr(x - y, y)
    elif x < y:
        return ggTr(y, x)

def ggTi(x, y):
    while y > 0:
        x,y = y, x%y
    return x

def ggTl(l):
    l = map(lambda x,y: ggTi(x,y),l,l[1:])
    return reduce(lambda x,y: x+y,l)/len(l)

"""Ausdruecke auswerten A3"""
def f(a,b,c=1):
    print a,b,c #-> c ist Default, a,b müssen gesetzt werden
def g(a,b,*c,**d):
    print a,b,c,d#->a,b müssen gesetzt werden, *c, **d optional für listen(*c)
                 #bzw dict(**d)
def h(a,b,c=1,*d,**e):
    print a,b,c,d,e#->a,b müssen, c Default, danach werden alle zahlen in liste
                    #und Zuweisungen in dic gespeichert

"""List-Comprehension A5"""
#gerade Kubikzahlen LC
print [x**3 for x in range(1,11) if x**3%2 ==0]
print filter(lambda x: x**3%2==0 ,map(lambda x:x**3,range(1,11)))
#Alle Teiler einer Zahl  z ausser 1,z
i = 1234
print [x for x in range(2,i) if i%x==0]
print filter(lambda x: i%x==0, range(2,i))
#Alle Primzahlen zwischen 10000 u 10100
print [x for x in range(10000,10101)if[y for y in range(2,x) if x%y==0]==[]]
print filter(lambda x: filter(lambda y: x%y==0, range(2,x))==[],range(10000,10101))

"""Ramanujan"""
print [((a,b),(c,d)) for a in range(20) for b in range(20) for c in range(20) for d in range(20) if a**3 + b**3 == c**3 + d**3 and a!=c and b!=d and a!=d and b!=c]

"""Zip-FKT"""
# unzip(zip((1,2,3,4),(5,6),(7,8))) == [(1,2),(5,6),(7,8)]

# t = [(1,2),(3,4),(5,6)]
# unzip(unzip(t)) == t
def unzip(l):
        lis = []
        leng = sorted([len(x) for x in l])[0]
        i = 0
        while i < leng:
                tups = []
                for tup in l:
                        tups.append(tup[i])
                tups = tuple(tups)
                lis.append(tups)
                i += 1
        return lis
        
