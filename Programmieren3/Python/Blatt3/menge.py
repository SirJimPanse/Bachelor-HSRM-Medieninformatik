class Set(object):


    def __init__(self,l=None):
        if type(l) == list:
            self.list = l
        elif type(l) == int:
            self.list = []
            self.list.append(l)
        else:
            self.list = []


    def add(self,elem):
        self.list.append(elem)


    def union_update(self, seq):
        for  ele in seq:
            self.list.append(ele)


    def union(self, seq):
        return self.list + seq

    def remove(self, elem):
        if elem in self.list:
            z = 0
            for i in self.list:
                if i == elem:
                    del self.list[z]
                z += 1


    def difference_update(self, seq):
        for ele in seq:
            self.remove(ele)


    def difference(self, seq):
        return [x for x in self.list if x not in seq]
          
  
    def clear(self):
        self.list = []


    def size(self):
        return len(self.list)


    def __len__(self):
        return len(self.list)


    def __eq__(self, seq):
        z = 0
        if len(self.list)!= len(seq):
            return False
        while(z < len(self.list)):
            if self.list[z] != seq[z]:
                return False
            z += 1
        return True

    def __ne__(self, seq):
        z = 0
        if len(self.list)!= len(seq):
            return True
        while(z < len(self.list)):
            if self.list[z] != seq[z]:
                return True
            z += 1
        return False


    def __add__(self, seq):
        return self.list + seq
    

    def __radd__(self, seq):
        return seq + self.list


    def __getitem__(self, index):
        return self.list[index]


    def __contains__(self, elem):
        if elem in self.list:
            return True
        return False


    def __sub__(self, seq):
        if type(seq) == Set:
            l = [i for i in self.list if i not in seq.list]
            return l
        elif type(seq) == list:
            l = [i for i in self.list if i not in seq]
            return l
    

    def __rsub__(self, seq):
        l = [i for i in seq if i not in self.list]
        return l
        

    def __repr__(self):
        if type(self) == OrderedSet:
            return str(self.Set.list)
        return str(self.list)


class OrderedSet(Set):


    def __init__(self, l):
            self.Set = Set(sorted(l.list)) 

  
    def __iter__(self):

        class itk(object):


            def __init__(self, l):
                self.index = 0
                self.Set = Set(l[:])


            def next(self):
                if self.index >= len(self.Set.list):
                    raise StopIteration
                self.index += 1
                return self.Set[self.index - 1]
        return itk(self.Set)
        
