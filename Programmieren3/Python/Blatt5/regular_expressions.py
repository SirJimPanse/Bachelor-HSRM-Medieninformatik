import re

def reg(reg,true,regex):
	pattern = re.compile(regex)
	match = pattern.match(reg)
	if != None and int(true) == 1:
		return True
	return False

postleitzahl = re.compile("\d{5}[01000-99998]")
plz1 = postleitzahl.match("99947")
print plz1.group()

