w = 2
v = [1]
v[0] = 3

def set1():
    w = 4
    v[0] = 4


def set2():
    global w,v
    w = 4
    v[0] = 4
