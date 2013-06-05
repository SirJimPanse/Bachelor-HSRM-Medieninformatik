#Abschnitt 3 "An Informal Ontroduction to Python"
print 2+2
print (50-5*6)
print 7/3
print 7/-3
width = 20
hight = 5*9
print "width = 2,hight =5*9 -> width * hight =",width * hight
print 3 * 3.75/1.5
print 7.0/2
print 1J * 1j
print 3+1j * 3
print (3+1j)*3
print (1+2j)/(1+1j)
a=1.5+0.5j
print a.real
print a.imag
a=3.0+4.0j
print abs(a)
print 'doesn\'t'
print  '"Yes," he said.'
print '"Isn\'t," she said.'
hello = "This is a rather long string containing\n\
several lines of text just as you would do in C.\n\
    Note that whitespace at the beginning of the line is\
 significant."
print hello
word = 'Help' + 'A'
print word
print 'str' 'ing' 
print 'str'.strip() + 'ing' 
print word[4]
print word[2:4]
print word[:2] + word[2:]
print word[10:]
print word[-1] 
print word[:-2]
print len(word)
a = ['spam', 'eggs', 100, 1234]
print a
print a[0]
print a[1:-1]
print a[:2] + ['bacon', 2*2]
print 3*a[:3] + ['Boo!']
a[2] = a[2] + 23
print a
a[0:2] = [1, 12]
print a
a[0:2] = []
print a
a[1:1] = ['bletch', 'xyzzy']
print a
a[:0] = a
print a
a[:] = []
print a
a = ['a', 'b', 'c', 'd']
print a
q = [2, 3]
p = [1, q, 4]
print p
print len(p)
print p[1]
print p[1][0]
p[1].append('xtra')
print p
print q


