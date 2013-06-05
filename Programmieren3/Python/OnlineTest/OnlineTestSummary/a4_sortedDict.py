class SortedDict(object):

    def __init__(self,dic = None):
        if dic == None: self.dic = {}
        else: self.dic = dic

    def __iter__(self):
        for i in sorted(self.dic.items()): yield i[0]+':'+ str(i[1])

    def __repr__(self):
        d = {}
        for x in sorted(self.dic.items()):
            d[x[0]] = d.get(x[0], x[1])
        self.dic = d
        return str(self.dic)

    def __contains__(self,ele):
        if ele in self.dic.keys() or ele in self.dic.values(): return True
        return False

    def keys(self):
        return self.dic.keys()

    def values(self):
        return self.dic.values()

    def items(self):
        return self.dic.items()
