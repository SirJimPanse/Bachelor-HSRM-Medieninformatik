import sys, string

if len(sys.argv) > 1:
    d = dict()   
    for i in sys.argv[1:]:
        d[i] = d.get(i, 0) + 1;
        
    most = sorted(d.values())[-1]
    
    for key, value in sorted(d.items()):
        if value == most: print "".join(str(value) + ":" + key)
    
else:
    print "Zu wenig Argumente!"
