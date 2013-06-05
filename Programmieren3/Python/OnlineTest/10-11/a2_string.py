import string

def rumdreh(zk):
    return zk[::-1]


def vokale(zk):
    s1 = str()
    s2 = str()
    voc = "AEIOUaeiou"
    l1 = [x for x in zk if x in voc]
    l2 = [x for x in zk if x not in voc]
    for i in l1:
        s1 += i
    for e in l2:
        s2 += e
    #besser l1 = "".Join([x for x in zk if x in voc])
    #besser l2 = "".Join([x for x in zk if x not in voc])
    # return l2, l2
    return s1, s2
    

def ersetze(zk, dic):
    string = str()
    z = 0
    leng = len(zk)
    while z < leng:
        if zk[z] == '{':
            if zk[z+1] in dic.keys():
                string += '{' + str(dic[zk[z+1]])
                z += 2
            else:
                string += zk[z]
                z += 1
        else:
            string += zk[z]
            z += 1
    return string

#besser
def ersetze2(zk, dic):
    for i in dic:
        zk = zk.replace('{'+i+'}', str(dic[i]))
    return zk

s = "AbcdefoU12E"
d = {'a':1, 'b':2, 'c':3}
s1 = "{a}a{b}b{c}c{d}d"

print vokale(s)
print ersetze2(s1, d)
