# -*- coding: utf-8 -*-
import random
"""append mit Slicing simulieren - Blatt 1 A2"""
l = [1,2,3]
ele = 4
l[len(l):] = [ele]

"""Blatt 1 A3"""
l = [1,2,3] #[1, 2, 3, [...]]
l.append(l)
print l
l = [1,2,3] # [1,2,3,1,2,3]
l = l + l

"""Blatt 1 A4"""
l = range(101)
print l[:10]
print l[len(l)-10:]
print l[::10]
print l[len(l)/2:(len(l)/2)+1]

"""Liste 25 Rnd Zahlen zw 1-100, durchlaufen if %3 == 0 in neue Liste **2
   mit forschleife, listcomp, primitven loesen"""

l = [random.randint(1,100) for x in range(26)]
new = []
for i in l:
    if i%3 == 0:
        new.append(i**2)
print "for:   ",new
new = []

new = [x**2 for x in l if x%3 == 0]
print "lcomp: ", new
new = []

new = map(lambda x: x**2 ,filter(lambda y: y%3 == 0,l))
print "primi: ",new



