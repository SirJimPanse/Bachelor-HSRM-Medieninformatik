from itertools import islice
"""Generatoren"""

"""Generator der alle Primzahlen generiert"""
def prim():
    i = 1
    while True:
        i += 1
        l = []
        l = [x for x in range(2,i) if i%x == 0]
        if l == []: yield i

def primtwin(g,i):
    return[(x,y) for x in islice(g,0,20) for y in islice(g,0,20) if y-x == 2]

    


                
