import sys
#s = '/home/medieninf/tland001/Prog3/Python/Blatt4/aufg3.obj'


def filter_obj(fle):
    count = 0
    lis = list()
    for i in fle:
        if i == 'v':
            lis.append((float(fle[count + 1]), 
						float(fle[count + 2]),
						float(fle[count + 3])))
        count += 1
    return lis


def filter_tri(fle):
    count = 0
    lis = list()
    for i in fle:
        if i == 'f':
            lis.append((int(fle[count + 1]),
						int(fle[count + 2]),
						int(fle[count + 3])))
        count += 1
    return lis


def objread(fle):
    lis = list()
    lis.append(filter_obj(fle))
    lis.append(filter_tri(fle))
    lis = tuple(lis)
    return lis

"""
if (sys.argv) > 1:
    fle = file(sys.argv[1]).read().split()
    print filter_obj(fle)
else:
    print "Zu wenige Argumente"
"""           
            


