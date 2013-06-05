import string

stri = "g fmnc wms bgblr rpylqjyrc gr zw fylb. rfyrq ufyr amknsrcpq ypc dmp. bmgle gr gl zw fylb gq glcddgagclr ylb rfyr'q ufw rfgq rcvr gq qm jmle. sqgle qrpgle.kyicrpylq() gq pcamkkclbcb. lmu ynnjw ml rfc spj."
s2 = "map"
def check(s):
    z = 0
    for i in string.ascii_letters:
        if s == string.ascii_letters[z]:
            if s == 'X':
                return 'a'
            elif s == 'Z':
                return 'b'
            return string.ascii_letters[z + 2]
        z += 1

s = str()
for i in s2:
    if i in string.ascii_letters:
        s += str(check(i))
    else:
        s += i
print s
    
                
