import string
low = string.lowercase

string = "a1b2c3z4"
string2 = "0,1,2,3,4,5,6,7,8,9,10"

def strrot(s):
    ret = str()
    for i in s:
        if i in low:
            if i == 'z': ret += 'a'
            else: ret += low[low.index(i)+1]
        else: ret += i
    return ret

def strrep(s):
    ret = str()
    d = {'1':"EINS", '2':"ZWEI", '3':"DREI"}
    l = "456789"
    for i in s:
        if i == '0': ret += 'NULL'
        elif i in d.keys(): ret += d[i]
        elif i in l: ret += "VIELE"
        else: ret += i
    return ret

print strrot(string)
print strrep(string2)
