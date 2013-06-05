'''
Created on 31.05.2013

@author: Justin Albert, Tino Landmann, Soeren Kroell
'''
import geometry as geo


class MatrixHandler(object):

    def __init__(self):
        self.mvMat = geo.identity() 
        self.pMat = geo.identity()

    def pushModelMatrix(self, matrix):
        self.mvMat = self.mvMat * matrix

    def pushProjectMatrix(self, matrix):
        self.pMat = self.pMat * matrix

    def getMvpMatrix(self):
        return self.pMat * self.mvMat
    
    def clear(self):
        self.mvMat = geo.identity()
        self.pMat = geo.identity()

    def __repr__(self):
        return repr(self.mvMat)

if __name__ == '__main__':
    s = MatrixHandler()
    r = geo.rotate(10, (1, 0, 0))
    print s
    print r
    s.pushModelMatrix(r)
    s.pushModelMatrix(r)
    print s
