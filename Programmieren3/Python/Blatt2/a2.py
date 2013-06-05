#!/usr/bin/python
# -*- coding: utf-8 -*-
import os

"""ggT rekursiv geloest"""
def ggTr(x, y):
    if x == y:
        return x
    elif x > y:
        return ggTr(x - y, y)
    elif x < y:
        return ggTr(y, x)


"""ggT iterativ geloest"""
def ggT(x, y):
    while y > 0:
      x, y = y, x % y
    return x


"""ggT für Liste von Zahlen"""
def ggTl (l=[]):
    return reduce(lambda x, y: ggT(x, y), l)


l = []
for line in file("ggts.dat").readlines():
    l.append(int(line.strip()))

ggts = map(lambda x,y: ggT(x,y),l,l[1:])

mittelwert = reduce(lambda x, y: x + y, ggts, 0)
mittelwert /= len(ggts)
print "Mittelwert aller ggT's: ", mittelwert
