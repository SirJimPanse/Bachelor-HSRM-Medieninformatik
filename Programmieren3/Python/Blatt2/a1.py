l = [1,2,3]
print "For-Schleife:"
for e in l:
    print e

print "While-Schleife:"
e = 0
while e < len(l):
    print l[e]
    e += 1
print "_______________"

print "While-Schleife:"
d = {1:"eins", 2:"zwei", 3:"drei"}
l = d.keys()
i = 0
while i < len(l):
    s =l[i]
    print s, d[s]
    i = i+1

print "For-Schleife:"
for j in l:
    print j ,d[j]
