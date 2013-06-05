#!/usr/bin/python
# -*- coding: utf-8 -*-
import os, random, sys, string

# Dateien holen
path = '/usr/share/games/fortunes'

files = filter(lambda x: not x.endswith('.dat') and not x.endswith('.u8'), os.listdir(path))
"""Fortune Aufgabe"""
zitate = []

for fl in files:
    eingabe = file(path+'/'+fl)
    [zitate.append(x) for x in eingabe.read().split('\n%\n')]

if len(sys.argv) > 1:
        zitate = filter(lambda x: sys.argv[2] in x, zitate) 
        print zitate[random.randint(0, len(zitate)+1)]
else:
    print zitate[random.randint(0,len(zitate)+1)]
