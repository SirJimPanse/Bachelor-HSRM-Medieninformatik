class Set(object):

    def __init__(self, l=None):
        if l == None: self.list = []
        else: self.list = l

    def add(self, elem):
        if elem not in self.list: self.list.append(elem)

    def union_update(self, seq):
        for elem in seq: self.add(elem)

    def union(self, seq):
        return [x for x in self.list if x in seq]

    def remove(self, elem):
        if elem in self.list: self.list.remove(elem)

    def difference_update(self, seq):
        for elem in seq: self.remove(elem)

    def difference(self, seq):
        return [x for x in self.list if x not in seq]

    def clear(self):
        self.list = []

    def size(self):
        return len(self.list)

    def __repr__(self):
        return "Set("+str(self.list)+")"

    def __len__(self):
        return len(self.list)

    def __eq__(self,seq):
        if len(self.list) != len(seq): return False
        for ele in seq:
            if ele not in self.list:
                return False
        return True

    def __ne__(self, seq):
        if len(self.list) != len(seq): return True
        for ele in seq:
            if ele not in self.list:
                return True
        return False

    def __add__(self,seq):
        if isinstance(seq,list):
            for ele in seq: self.add(ele)
        elif isinstance(seq,type(self)): self.union_update(seq)
        return Set(self.list)
        
    def __radd__(self,seq):
        return self.__add__(seq)

    def __sub__(self, seq):
        if isinstance(seq,list):
            return [x for x in self.list if x not in seq]
        return [x for x in self.list if x not in seq.list]

    __rsub__ = __sub__

    def __getitem__(self, index):
        return self.list[index]

    def __contains__(self, elem):
        if elem in self.list: return True
        return False

class OrderedSet(Set):

    def __init__(self, l = None):
        if l == None: self.list = []
        else: self.list = sorted(l)

    def __iter__(self):
        class itk(object):
            def __init__(self,l):
                    self.list = l[:]
                    self.index = 0
            def next(self):
                if self.index >= len(self.list):
                    raise StopIteration
                self.index += 1
                return self.list[self.index-1]
        return itk(self.list)
                    

    
        

    

    

    
        
