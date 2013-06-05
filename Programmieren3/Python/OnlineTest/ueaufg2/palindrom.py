import string
l = ["Die","Anna","hat","den","Otto", "lieb"]
s1 = "Das ist   sehr    uncool"

def palindrom(lis):
    for i in lis:
        index = l.index(i)
        if i.lower() == i.lower()[::-1]: s = '*'+i+'*'
        else: s = i + i[::-1]
        lis.remove(i)
        lis.insert(index, s)
    return lis

def delleer(s):
    st = str()
    s = s.split(' ')
    for i in s:
        if i is not '':
            st += i+' ' 
    return st

print palindrom(l)
print delleer(s1)
        
