import sys


if len(sys.argv) > 1:
    for i in sys.argv[1:]:
        c = 1
        string = str()
        s = i[0]
        i = i+"\n"
        for x in i[1:]:
            if x == s: c += 1
            else:
                if c > 1:
                    string += s + str(c)
                    c = 1
                else: string += s
            s = x
        print string


if len(sys.argv) > 1:
    l = []
    string = str()
    count = 1
    for arg in sys.argv[1:]:
        letter = arg[0]
        for ele in arg[1:]:
            if ele == letter:
                count += 1
            else:
                if count > 1:
                    string += letter+str(count)
                    count = 1
                else:
                    string += letter
            letter = ele

        if count > 1:
            string += letter+str(count)
            count = 1
        else:
            string += letter
            
        l.append(string)
        string = str()

    for i in l:
        print i
        
else:
    print "Zu wenige Argumente!"
