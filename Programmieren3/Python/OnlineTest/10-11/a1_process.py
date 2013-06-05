
l = ["abc:def","123:456","def:456","Jim:Panse","17:7"]

def process(lines, split =":", key = 0, del_key = False):
    d = dict()
    for i in lines:
        parts = i.split(split)
        pkey = parts[key]
        if del_key:
            parts = parts[:key] + parts[key+1:]
        d[pkey] = parts
    print d

def process2(lines, split = ":", key = 0, del_key = True):
    d = dict()
    l = []
    for i in lines:
        l.append(i.split(split))
    for i in l:
        if not del_key: d[i[key]] = i
        else: d[i[key]] = i[key+1:]
    return d

process (l)
