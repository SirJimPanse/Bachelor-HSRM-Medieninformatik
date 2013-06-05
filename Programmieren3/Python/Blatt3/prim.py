from itertools import islice


def prim():
    z = 3
    while True:
        prim = True
        for i in range(2, z):
            if z % i == 0:
                prim = False
        if prim == True:
            yield z
        z += 1
        b = True


def prim2():
    z = 3
    while True:
       l = [x for x in range(2, z) if z % x == 0]
       if l == []:
           yield z
       z += 1


def prim_twin():
    return[(x, y) for x in islice(prim(), 0, 20) 
			for y in islice(prim(), 0, 20) if y - x == 2]
    
            
