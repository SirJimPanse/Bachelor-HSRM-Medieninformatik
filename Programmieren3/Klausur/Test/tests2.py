import string, random
def deep(seq):
    l = []
    for i in seq:
        if isinstance(i,int):
            l.append(i)
        else:
            l.append(deep(i))

    if isinstance(seq,tuple):
        return tuple(l)
    return l

def flatten(seq):
    l = []
    for i in seq:
        if isinstance(i, int):
            l.append(i)
        else:
            l.extend(flatten(i))
    return l

def myxrange(end,start=0,step=1):
    if end < start:
        start,end = end, start
    for i in range(start,end,step):
        yield i

s = "abc14344211"
d = {}
for i in s:
    if i not in string.ascii_letters: d[i] = d.get(i,0)+1
print d

s = ["muh","muhaha", "auto", "deine mudda"]
l = []
for i in s:
    if len(i) < 5: l.append(i)
print l

print filter(lambda x: len(x) < 5, s)

print [x for x in s if len(x) < 5]

l = [1,2,3,4]
def fkt(seq,func=None):
    if func == None: return reduce(lambda x,y: x+y,seq)
    return reduce(lambda x,y: x+y,map(lambda x: func(x), seq))

def gen(seq):
    d = {}
    for i in seq:
        d[i] = d.get(i,0)+1
        print d
        yield (i,d[i])

def func(seq, func=None):
    if func == None:
        return sum(seq)
    return sum(map(func,seq))

