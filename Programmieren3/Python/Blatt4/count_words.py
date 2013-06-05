import sys


def most_counted(d):
    z = 0
    for x in d.values():
        if x > z:
            z = x
    return z


def words_in_list(d):
    l = []
    for word, count in d.items():
        if  count == most_counted(d):
            l.append((count, word))
    return sorted(l)
   
 
def words(s):
    d = dict()
    for i in s:
        d[i] = d.get(i,0) + 1
    l = words_in_list(d)
    return l  

if (sys.argv) > 1:
    l = words(sys.argv[1:])
    for i in l:
        print i[0],':',i[1]
        """
        d = i[0]
        e = i[1]
        print "%d:%e" %(d,e)
        """
else:
    print "Zu wenig Argumente"
"""
"%(name)s heisst %(name)s" %
{"name": "Eric"}

  """  
    
