import string

lis = [("igude#","was","geh*t"),("hier","geht","einiges#")]

def f(eintraege, sep = ":", esc = ["#", "*"]):
    l = []
    l2 = []
    for x in eintraege:
        z = 0
        while z < len(x):
            if z == len(x)-1 and eintraege.index(x) != len(eintraege)-1: l.append(x[z]+'\\n')
            else: l.append(x[z])
            z += 1     
    s = str()
    for i in l:
        for k in i:
            if k in esc: s += "\\" + k
            else: s += k
        l2.append(s)
        s = str()
    s = sep.join(l2)
    return s

print f(lis)
