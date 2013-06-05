print "Kubikzahlen mit List-Comprehension:"
k = [x**3 for x in range(11) if (x**3)%2 == 0]
print k


def prim(x):
    for i in range(2, x):
        if x % i == 0:
            return True
    return False

print "\nPrimzahlen von (1000-10100) mit List-Comprehension:"
y = [x for x in range(10000, 10101) if not prim(x)]
print y

print "\nPrimzahlen besser geloest (mit List-Comprehension)):"
print [x for x in range(10000, 10101) if[y for y in range(2,x) if x % y == 0] == []]

print "\nalle Teiler einer Zahl List-Comprehension:"
i = 12345
print [x for x in range(2, i) if i % x == 0 ]

print "\nKubikzahlen mit Funktionalen-Primitiven:"
print filter(lambda x: x % 2 == 0, map(lambda x: x ** 3, range(11)))

print "\nalle Teiler einer Zahl mit Funktionalen-Primitiven:"
m = 12345
print filter(lambda x: m % x == 0, range(2, m))

print "\nPrimzahlen von (1000-10100) mit Funktionalen Primitiven:"
print filter(lambda x: not prim(x), range(10000, 10101))

print "\nPrimzahlen besser mit Funktionalen Primitiven:"
#print filter(lambda x: [y for y in range(2,x) if x%y == 0]==[],range(10000, 10101))
#map(lambda y: x%y==0,range(2,x))==[]
#lambda x: x, range(10000,10101)
#lambda y: x%y==0,range(2,x)
print filter(lambda x: filter(lambda y: x%y==0,range(2,x))==[], range(10000,10101))

