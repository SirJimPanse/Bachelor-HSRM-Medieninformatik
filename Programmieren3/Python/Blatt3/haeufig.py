import text

#'/usr/share/dict/words'
#'/home/mi/tland001/tland001/Prog3/Python/Blatt3/text.txt'
#'/home/mi/tland001/tland001/Prog3/Python/Blatt3/shake.txt'
st = '/home/mi/tland001/tland001/Prog3/Python/Blatt3/text.txt'
shake = '/home/mi/tland001/tland001/Prog3/Python/Blatt3/shake.txt'


def rev_key_val(d):
    dic = dict()
    for anzahl, value in d.items():
        dic[value] = dic.get(value, anzahl)
    return dic
    
def h_chars(s):
    l = rev_key_val(text.count.count_chars(s))
    l = list(l.items())
    l.sort(reverse=True)
    #print "25 haeufigsten Buchstaben:"
    return l[:26]
        
def h_words(s):
    l = rev_key_val(text.count.count_words(s))
    l = list(l.items())
    l.sort(reverse=True)
    #print "\n","25 haeufigsten Woerter:"
    return l[:26]

print h_chars(shake)
print h_words(shake)
print "25 haeufigste Wort aus Sommernachtstraum:"
print h_words(shake)[0]

