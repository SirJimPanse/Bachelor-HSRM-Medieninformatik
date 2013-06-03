'''
Created on 12.04.2013

@author: tland001
'''
import math
class Vector(object):
    def __init__(self, x, y, z):
        self.vector = (x, y, z)
        
    def dot(self,w):
        return float(self.vector[0]*w.vector[0] + self.vector[1]*w.vector[1] + self.vector[2]*w.vector[2])
    
    def vectorlength(self):
        return math.sqrt(math.fabs(self.vector[0]**2 + self.vector[1]**2 + self.vector[2]**2))
    
    def crossproduct(self,w):
        return Vector(self.vector[1]*w.vector[2] - self.vector[2]*w.vector[1],\
                self.vector[2]*w.vector[0]-self.vector[0]*w.vector[2],\
                self.vector[0]*w.vector[1]-self.vector[1]*w.vector[0])
    
    def normalized(self):
        return self/self.vectorlength()
    
    def scale(self, scale):
        return self * scale
        
    def __repr__(self):
        return "".join(("(",str(self.vector[0]),",",str(self.vector[1]),",",str(self.vector[2]),")"))
    
    def __sub__(self, w):
        if type(w) == float:
            return Vector(self.vector[0]-w, self.vector[1]-w, self.vector[2]-w)
        return Vector(self.vector[0]-w.vector[0],self.vector[1]-w.vector[1],self.vector[2]-w.vector[2])
    
    def __rsub__(self,w):
        return self.__sub__(w)
    
    def __add__(self, w):
        return Vector(self.vector[0]+w.vector[0],self.vector[1]+w.vector[1],self.vector[2]+w.vector[2])
    
    def __div__(self,w):
        return Vector(self.vector[0]/w, self.vector[1]/w, self.vector[2]/w)
    
    def __mul__(self, value):
        if type(value) == Vector:
            return Vector(self.vector[0] * value[0], self.vector[1] * value[1], self.vector[2] * value[2])
        return  Vector(self.vector[0] * value, self.vector[1] * value, self.vector[2] * value)
    
    def __rmul__(self, value):
        return self.__mul__(value)
    
    def __getitem__(self,index):
        return self.vector[index]
    
    def reflectRay(self, norm):
        dr = self - 2 * norm.dot(self) * norm
        return dr


