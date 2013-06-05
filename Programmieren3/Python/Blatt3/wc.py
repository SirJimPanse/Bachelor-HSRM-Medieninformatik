import sys, os
import string
#'/usr/share/dict/words'
#'/home/medieninf/tland001/Prog3/Python/Blatt3/text.txt'
#'/home/mi/tland001/tland001/Prog3/Python/Blatt3/text.txt'
#st = '/home/mi/tland001/tland001/Prog3/Python/Blatt3/text.txt'

global lang


def chars(s):
    f = file(s).read()
    #print f[:100]
    anz = 0
    for i in f:
        if i in string.ascii_letters:
            anz += 1
    return anz


def lines(s):
    f = file(s).readlines()
    #print f[:10]
    return len(f)


def words(s):
    f = file(s).read().split()
    #print f[:100]
    return len(f)


def wc(s):
    return chars(s), lines(s), words(s)


def wc_show(s, l=None):
    if l == 'de':
        print "deutsch"
    elif l == 'en':
        print "english"
    else:
        print "Die Datei", s, "hat"
        print lines(s), " Zeilen"
        print words(s), " Woerter"
        print chars(s), " Buchstaben"


def set_lang(l):
    if l == 'de':
        lang = 'de'
    elif l == 'en':
        lang = 'en'
    else:
        print "Falscher Parameter, nur 'de' und 'en' zugelassen"

#wc_show(st)


