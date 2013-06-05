"""Kleineste Zahl zweier Summen von Kubikzahlen"""

l = [[(a,b),(c,d)] for a in range(20) for b in range(20) for c in range(20)\
for d in range(20) if a ** 3 + b ** 3 == c ** 3 + d ** 3 \
and a!=c and a!=d and b!=c and b!=d]

print "kleinste Summe zweier Kubikzahlen\n", l

for x in l:
    print ((x[0][0])**3)+((x[0][1])**3),"==",((x[1][0])**3)+((x[1][1])**3)
