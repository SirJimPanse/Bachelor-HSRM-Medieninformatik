# -*- coding: utf-8 -*-
import sys
""" wörter zählen A1 """
def count_words(l):
    d = {}
    lis = []
    for i in l:
        d[i] = d.get(i,0)+1
    most = sorted(d.values())[-1]
    for e,k in d.items():
        if k == most:
            lis.append((e,k))
    return sorted(lis)
"""   
if len(sys.argv) > 1:
    l = count_words(sys.argv[1:])
    for i in l:
        print "".join(str(i[1])+":"+i[0])
"""

"""komprimier A2"""

def comp(string):
    string = string + '\0'
    s = str()
    akt = string[0]
    count = 1
    for i in string[1:]:
        if i != akt:
            if count == 1: s += akt
            else:
                s+= akt + str(count)
                count = 1
        else:
            count += 1
        akt = i
    return s
    
if len(sys.argv) > 1:
    for i in sys.argv[1:]:
        print comp(i)
    

