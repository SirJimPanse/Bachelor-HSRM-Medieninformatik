'''
Created on 14.04.2013

@author: medieninf
'''
from vector import Vector
class Point(object):
    def __init__(self, x, y, z):
        self.point = (x,y,z)
        
    def __repr__(self):
        return "".join(("(",str(self.point[0]),",",str(self.point[1]),",",str(self.point[2]),")"))
    
    def __sub__(self, w):
        return Vector(self.point[0]-w.point[0],\
                self.point[1]-w.point[1],\
                self.point[2]-w.point[2])
    
    def __add__(self, w):
        return Point(self.point[0]+w[0],\
                self.point[1]+w[1],\
                self.point[2]+w[2])
    
    def __div__(self,w):
        return Point(self.point[0]/w,\
                self.point[1]/w,\
                self.point[2]/w)
        
    def __getitem__(self,index):
        return self.point[index]
        
        