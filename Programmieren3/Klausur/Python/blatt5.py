import re
"""regulaere Ausdruecke A3"""

def reg(reg,true,regex):
	pattern = re.compile(regex)
	match = pattern.match(reg)
	if match != None and int(true) == 1:
		return True
	return False
"""
#PLZ
pattern = re.compile('[0-9]{5}')
plz = pattern.match('99947')
print plz.group()
"""
# read in reg.txt file and prepare for checking...
lines = file("reg.txt").readlines()
words = []
for e in lines:
        akt = filter(lambda x: x!= '', e.strip().split('\t'))
        words.append(akt)

for e in words:
        print e[0], e[1], e[2]
        if e[0] == "PLZ": print reg(e[1],e[2], r'\d{5}')
        elif e[0] == "Datum": print reg(e[1],e[2], '([0][1-9]|[1]\d|[2]\d|[3][0-1])\.([0][1-9]|[1][0-2])\.([1-2]\d{3})')
        elif e[0] == "EUR": print reg(e[1], e[2], '\d{1,3}((\.\d{3}){2}|(,\d{2}))?(  EUR)?')
        elif e[0] == "Tel": print reg(e[1], e[2], '((\+\d{1,2} )|0)/?((\d{2,3}/)*|(\d{2,3}-)*)\d{2,3}')
        elif e[0] == "Email": print reg(e[1], e[2], '\D(\w[\.-]?)*(\w)@((([a-z]-?)*)\.)*([a-z]{2})')
"""
# iterate through the list and call responsible methods
for e in words:
        print e[0], e[1], e[2]
        if e[0] == "PLZ":
                print reg(e[1], e[2], r'\d{5}')
        if e[0] == "Datum":
                print reg(e[1], e[2], r'(0\d|[12]\d|3[01])\.(0\d|1[0-2])\.(19\d{2}|20\d{2})')
        if e[0] == "EUR":
                print reg(e[1], e[2], r'\d{1,3}(\.\d{3})*(,\d{2})?(  EUR)?')
        if e[0] == "Tel":
                print reg(e[1], e[2], r'(\+\d{2} [1-9]|0[1-9]\d)([0-9]{2}[- /])*[0-9]*')
        if e[0] == "Email":
                print reg(e[1], e[2], r'[a-zA-Z]\w*[\.-]?\w*@([a-z]*\.)*[a-z]{2,4}')
"""
