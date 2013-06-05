'''
Created on 12.04.2013

@author: tland001
'''
import vector


class CheckerboardMaterial(object):
    def __init__(self):
        self.baseColor = (1,1,1)
        self.otherColor = (0,0,0)
        self.ambientCoefficient = 1.0 
        self.diffuseCoefficient =  0.6
        self.specularCoefficient = 0.2
        self.checkSize = 1


    def baseColourAt(self, p):
        v = Vector(p)
        v.scale(1.0 / self.checkSize)
        if (int(abs(v.x) + 0.5) + int(abs(v.y) + 0.5) + int(abs(v.z) + 0.5)) % 2:
            return self.otherColour
        return self.baseColour
