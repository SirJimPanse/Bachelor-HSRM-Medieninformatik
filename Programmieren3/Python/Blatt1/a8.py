# -*- coding: utf-8 -*-
d = {}
print dir(d), "\n"
de2en = {1:'one', 2:'two', 3:'three', 'eins':'one', 'zwei':'two', 'drei':'three'}
print "dict:",de2en
print "len():", len(de2en)
print "de2en[1] (dict[key]):",de2en[1],"\n"
print "de2en.values(), de2en.keys():",de2en.values(), de2en.keys(),"\n"
print "de2en.items():",de2en.items(),"\n"
print "key(1) in de2en:", 1 in de2en,"\n"
del de2en[1]
print "del de2en[key(1)]:",de2en,"\n"
de2en.clear()
print "de2en.clear():",de2en
