import wc
import string
#'/usr/share/dict/words'
#'/home/mi/tland001/tland001/Prog3/Python/Blatt3/text.txt'
#st = '/usr/share/dict/words'


def count_chars(s):
    f = file(s).read()
    d = dict()
    for letter in f:
        if letter in string.ascii_letters:
            d[letter] = d.get(letter, 0) + 1
    return d
    

def count_words(s):
    f = file(s).read().split("\n")
    d = dict()
    for word in f:
        d[word] = d.get(word, 0) + 1
    return d

#print count_chars(st),"\n"
#print count_words(st)

