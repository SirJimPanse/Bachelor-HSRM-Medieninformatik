#!/usr/bin/python
# -*- coding: utf-8 -*-
"""Unzip indem Liste einfach noch einmal gezipt wird"""
def unzip(l):
    return zip(*l)


"""Unzip Funktion ohne nochmaligem zipen ()umstaendlich"""
def unzip2(l):
    length = sorted([len(x) for x in l])[0]
    i = 0
    new = []
    tup=[]
    while i < length:
        for x in l:
            tup.append(x[i])
        i += 1
        tup = tuple(tup)
        new.append(tup)
        tup=[]
    return new        

"""Test's"""
# unzip(zip((1,2,3,4),(5,6),(7,8))) == [(1,2),(5,6),(7,8)]

# t = [(1,2),(3,4),(5,6)]
# unzip(unzip(t)) == t
