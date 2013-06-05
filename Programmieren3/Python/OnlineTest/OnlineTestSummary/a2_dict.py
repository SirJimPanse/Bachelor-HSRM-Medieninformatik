string = "a:b\nc:d\n\ntest string"

def dictup(s):
    ret = []
    d = {}
    l = s.split("\n")
    l.remove('')
    for i in l:
        if ':' in i: d[i[0]] = i[2]
        else: ret.append(i)
    ret.append(d)
    return tuple(reversed(ret))

print dictup(string)
