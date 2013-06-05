'''
Created on 31.05.2013

@author: Justin Albert, Tino Landmann, Soeren Kroell
'''

from mathematic import geometry as geo


class Camera(object):

    def __init__(self, e, c, up, fovy, aspect):
        self.e = e
        self.c = c
        self.up = up
        self.fovy = fovy
        self.aspect = aspect
        self.initCamera()
        
    def initCamera(self):
        
        self.mvMat = geo.lookAtMatrix(self.e[0], self.e[1], self.e[2], 
                                     self.c[0], self.c[1], self.c[2], 
                                     self.up[0], self.up[1], self.up[2])
        
        self.pMat = geo.perspectiveMatrix(self.fovy, self.aspect, 0.1, 30)
        
        print "cam initialisiert"
    
    def changeSight(self, axis):
        self.mvMat = geo.lookAtMatrix(axis[0], -axis[1], -axis[2], 
                                     self.c[0], self.c[1], self.c[2], 
                                     self.up[0], self.up[1], self.up[2])
    
    
        