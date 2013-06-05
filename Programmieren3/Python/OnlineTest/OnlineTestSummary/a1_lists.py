l = [1,2,3,4,5,1,2,3,6,6]
l2 = [1,2,3,4,5,1,2,3,6,6]
l3 = [1,2,3]
l4 = [3,4,5,6]

def del_double(l):
    z = 0
    s = l
    for i in l:
        for x in s:
            if x == i:
                z += 1
        if z > 1:
            s.remove(i)
        z = 0
    return s

def del_double_rev(l):
    l.reverse()
    l = del_double(l)
    return l

def lists(l1,l2):
    l3 = l1
    for i in l2:
        if i not in l1:
            l3.append(i)
    return l3
    
        
print del_double(l)
print del_double_rev(l2)
print lists(l3,l4)
