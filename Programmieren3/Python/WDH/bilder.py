import sys

def heller(p, V, pixel):
    pic = []
    for i in pixel:
        pic.append(str(int((int(i) + (int(p)/100.0) * float(V)))))
    return pic

def gamma(g, V, pixel):
    pic = []
    for i in pixel:
        pic.append(str(int(V * (float(i)/float(V))**float(g))))
    return pic

def binarisieren(s, V, pixel):
    pic = []
    for i in pixel:
        if i >= int(s):
            pic.append(str(V))
        else:
            pic.append(str(0))
    return pic

def save_pmg(name, header,pixel):
    with file(name, "w") as fle:
        fle.write(header[0]+"\n")
        fle.write(header[1]+" ")
        fle.write(header[2]+"\n")
        fle.write(header[3]+"\n")
        fle.write(" ".join(l))

if len(sys.argv) == 5:
    header = file(sys.argv[3]).read().split()[:4]
    pixel = [int(i) for i in file(sys.argv[3]).read().split()[4:]]
    V = int(header[-1])
    p = sys.argv[2]
    name = sys.argv[4]
    
    if sys.argv[1] == 'heller':
        l = heller(p,V,pixel)       
    elif sys.argv[1] == 'gamma':
        l = gamma(p,V,pixel)
    elif sys.argv[1] == 'binarisieren':
        l = binarisieren(p,V,pixel) 
    else:
        print "gibts nicht!"
    save_pmg(name, header , l)

else:
    print "Zu wenige Argumente"
