'''
Created on 14.05.2013

@author: tland001
'''


class vec(object):

    def __init__(self, c1, c2, c3=1):
        self.vec = (c1, c2, c3)

    def dot(self, v):
        v1 = self.vec[1] * v.vec[2] - self.vec[2] * v.vec[1]
        v2 = self.vec[2] * v.vec[0] - self.vec[0] * v.vec[2]
        v3 = self.vec[0] * v.vec[1] - self.vec[1] * v.vec[0]
        return vec(v1, v2, v3)

    def __sub__(self, v):
        return vec(self.vec[0] - v.vec[0], self.vec[1] - v.vec[1],
                   self.vec[2] - v.vec[2])

    def __rsub__(self, v):
        return self.__sub__(v)

    def __getitem__(self, index):
        return self.vec[index]

    def __repr__(self):
        return str(self.vec)

if __name__ == "__main__":
    print  vec(1, 1, 1).dot(vec(3, 2, 1))
