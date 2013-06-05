# -*- coding: utf-8 -*-
class Ring(object):
    def __init__(self,l=None):
        if type(l) == list:
            self.list = l
        elif type(l) == int:
            self.list = []
            self.list.append(l)
        else:
            self.list = []
        self.pos = 0

    def add(self,ele):
        if self.pos == 0:
            self.list.append(ele)
        else:
            self.list.insert(self.pos,ele)
            self.pos += 1

    def add_sequence(self,seq):
        aktpos = self.pos
        for ele in seq:
            self.add(ele)
        self.pos = aktpos

    def get_elements(self):
        aktpos = self.pos
        lis = list()
        while True:
            lis.append(self.list[self.pos])
            if self.pos == len(self.list)-1:
                self.pos = 0
            else:
                self.pos += 1
            if self.pos == aktpos:
                break
        return lis
        #besser: print self.list[i:] + self.list[:i]

    def get_current(self):
        return self.list[self.pos]

    def remove_current(self):
        del self.list[self.pos]
        if self.pos >= len(self.list):
            self.pos = 0

    def next(self):
        aktpos = self.pos
        if self.pos == len(self.list)-1:
            self.pos = 0
        else:
            self.pos +=1
        return self.list[aktpos]

    def __len__(self):
        return len(self.list)

    def __repr__(self):
        s = "r["
        count = 0
        for i in self.list:
            if count == self.pos:
                s += "; "
            s += str(i)
            if count != len(self.list)-1 and count+1 != self.pos:
                s += ", "
            count += 1
        s += "]"
        return s
    """
    besser:
    i = self.i
    anf = str(self.ring[:i])[:-1]
    end = str(self.ring[i:])[1:]
    return ’r’ + anf + ’; ’ + end
    """

    def __getitem__(self,index):
        akti = index
        aktpos = self.pos
        while akti > 0:
            if aktpos == len(self.list)-1:
                aktpos = 0
            else:
                aktpos += 1
            akti -= 1
        return self.list[aktpos]

    def __contains__(self,ele):
        if ele in self.list:
            return True
        return False
    #besser: return ele in self.list

    def __iter__(self):
        def it():
            aktpos = self.pos
            ring = self.list[:]
            while True:
                yield ring[aktpos]
                if aktpos == len(ring)-1:
                    aktpos = 0
                else:
                    aktpos += 1
        return it()

    def is_empty(self):
        if self.list == []:
            return True
        return False
        #besser: return self.list == []

    def clear(self):
        self.list = []
        self.pos = 0

print "r = Ring([1,2])"
r = Ring([1,2])
print "IST: ",r,"SOLL: r[; 1, 2]"
print "r.add(3); r"
r.add(3); r
print "IST: ",r,"SOLL: r[; 1, 2, 3]"
print "r.next(), r.next(), r.next(), r.next()"
print "IST: ",r.next(), r.next(), r.next(), r.next(),"SOLL: (1, 2, 3, 1)"
print "IST: ",r,"SOLL: r[1; 2, 3]"
print "r.add(4); r"
r.add(4); r
print "IST: ",r,"SOLL: r[1, 4; 2, 3]"
print "r.next()"
print "IST: ",r.next(),"SOLL: 2"
print "len(r), r"
print "IST: ",len(r), r,"SOLL: 4 r[1, 4, 2; 3]"
print "6 in r, 2 in r"
print "IST: ",6 in r, 2 in r,"SOLL: (False, True)"
print "r.get_current(), r[0], r[1], r[2], r[3], r[4]"
print "IST: ",r.get_current(), r[0], r[1], r[2], r[3], r[4],"SOLL: (3, 3, 1, 4, 2, 3)"
print "r"
print "IST: ",r,"SOLL: r[1, 4, 2; 3]"
print "r.get_elements()"
print "IST: ",r.get_elements(),"SOLL: [3, 1, 4, 2]"
print "r.remove_current(); r"
r.remove_current()
print "IST: ",r,"SOLL: r[; 1, 4, 2]"
print "it = iter(r)"
it = iter(r)
print "it.next(), it.next(), it.next(), it.next(), it.next()"
print "IST: ",it.next(), it.next(), it.next(), it.next(), it.next(),"SOLL: (1, 4, 2, 1, 4)"
print "r"
print "IST: ",r,"SOLL: r[; 1, 4, 2]"
print "r.add_sequence((5,6,7)); r"
r.add_sequence((5,6,7))
print "IST: ",r,"SOLL: r[; 1, 4, 2, 5, 6, 7]"
print "it.next(), it.next(), it.next(), it.next(), it.next()"
print "IST: ",it.next(), it.next(), it.next(), it.next(), it.next(),"SOLL: (2, 1, 4, 2, 1)"
print "r.is_empty()"
print "IST: ",r.is_empty(),"SOLL: False"
print "r.clear(); r.is_empty()"
r.clear()
print "IST: ",r.is_empty(),"SOLL: True"
    
