# -*- coding: utf-8 -*-
import string
vowels = 'AEIOUaeiou'
f = file("/usr/share/dict/words").read()

def konsonanten(s):
    for x in s[:10]:
        print x
    return len([x for x in s if x not in vowels and x in string.ascii_letters])

#besser len([x for x in s if x not in vocals AND x IN STRING.ASCII_LETTERS])

print "Konsonanten (Meine Fkt):"
print konsonanten(f),"\n"

def konsonanten2(s):
    anz = 0
    for i in s:
        if i not in vowels and i in string.ascii_letters:
            anz +=1
    return anz

print "Konsonanten (Barth Fkt):"
print konsonanten2(f),"\n"

def vokale(s):
    vocs = {'A':0,'E':0,'I':0,'O':0,'U':0,'a':0,'e':0,'i':0,'o':0,'u':0}
    for x in s:
        if vocs.has_key(x):
            vocs[x] += 1
    return vocs

print "vokale (Meine Fkt):"
print vokale(f),"\n"

def vokale2(s):
    d = dict()
    for i in s:
        if i in vowels:
            d[i] = d.get(i,0)+1#fuegt d vokal hinzu, wenn noch kein value, dann
    return d                   #dann value = 0 und direkt erhöhen um 1

print "vokale Barth Fkt: "
print vokale2(f),"\n"

"""häufigste kleine Vokale"""
def hkv(s):
    l = []
    d = vokale(s)
    for key in d:
        if key in string.lowercase:
            l.append((d[key],key))
    return sorted(l)[::-1]

print "haeufigste kleine Vokale (Meine Fkt):"
print hkv(f),"\n"

def hkv2(s):
    l = []
    for vokale, anzahl in vokale2(s).items():
        if vokale in 'aeiou':
            l.append((anzahl,vokale))
    l.sort(reverse=True)
    return l

print "haeufigste kleine Vokale (Barth Fkt):"
print hkv2(f)
