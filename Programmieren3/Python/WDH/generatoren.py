def nums():
    i = 0
    while True:
        i += 1
        yield i

def pendel(g):
    i = 0
    lis = g
    while True:
        yield lis[i]
        if i == len(lis)-1:
            lis = lis[::-1]
            i = 0
        i += 1

def pendel2(g):
    l = []
    for e in g:
        l.append(e)
        yield e
    while True:
        for i in l[::-1][1:]: # oder i in reversed(l[:-1])
            yield i
        for x in l[1:]:
            yield x

def im_kreis(g):
    i = 0
    lis = g
    while True:
        yield lis[i]
        if i == len(lis)-1:
            i = 0
        else:
            i += 1

def im_kreis2(g):
    l = []
    for e in g:
        l.append(e)
        yield e
    while True:
        for i in l:
            yield i
            

def von_vorne(g):
    i = 0
    akt = 0
    lis = g
    while True:
        if akt == len(lis):
            break
        yield lis[i]
        if i == akt:
            i = 0
            akt += 1
        else:
            i += 1

def von_vorne2(g):
    l = []
    while True:
        l.append(g.next())#oder for e in g: l.append(e), klappt aber beim generator net
        for i in l:
                yield i

if __name__ == '__main__':
    def testgen():
        for i in range(1,6):
            yield i

    import itertools
    lpendel = list(itertools.islice(pendel2(testgen()), 0, 20))
    print "pendel([1,2,3,4,5])[:20] : ", lpendel
    limkreis = list(itertools.islice(im_kreis2(testgen()),0, 20))
    print "imkreis ([1,2,3,4,5])[:20] : ", limkreis
    lvonvorne = list(von_vorne2(testgen()))
    print "von_vorne ([1,2,3,4,5]) : ", lvonvorne


    







        
