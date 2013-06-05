import string
buc = string.lowercase + string.uppercase
# oder buc ='abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
lis = range(10)

""" Liste mit allen Elementen von lis ausser dem ersten und dem letzten"""
print "Liste mit allen Elementen von lis ausser dem ersten und dem letzten:"
print lis[1:-1],"\n"

""" Liste mit jedem zweiten Element von lis """
print "Liste mit jedem zweiten Element von lis: "
print lis[::2],"\n"

""" Liste mit jedem zweiten Element von lis, ab dem zweiten Element"""
print "Liste mit jedem zweiten Element von lis, ab dem zweiten Element:"
print lis[1::2],"\n"

""" Liste von Tupeln, mit jeweils immer 2 Elementen von lis """
print "Liste von Tupeln, mit jeweils immer 2 Elementen von lis: "
print zip(lis[::2],lis[1::2]),"\n"

""" String mit jedem zweiten Buchstabe von buc """
print "String mit jedem zweiten Buchstabe von buc: "
print buc[::2],"\n"

""" String buc herumgedreht """
print "String buc herumgedreht: "
print buc[::-1]
#ginge auch "".join(reversed(buc))
