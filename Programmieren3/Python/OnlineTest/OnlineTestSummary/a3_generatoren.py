import string

def gen():
    i = 1
    while True:
        yield i
        i += 1

def rotstring(s):
    while True:
        s = s[-1]+s[:-1]
        yield s

def upstring(s):
    boole = 0
    while True:
        z = 0
        for i in s:
            if boole == 0: yield s[:z]+i.upper()+s[z+1:]
            else: yield s[:z]+i.lower()+s[z+1:]
            z += 1
        if boole == 0:
            s = s.upper()
            boole = 1
        else:
            s = s.lower()
            boole = 0

def addsub(gen):
    while True:
        g = gen.next()
        yield g
        yield -g


