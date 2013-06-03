'''
Created on 18.04.2013

@author: tland001
'''
from vector import Vector
from light import Light
from color import Color
class Material(object):
    
    def __init__(self,color, ambientParameter, diffusParameter, specularParameter, n, reflexFactor):
        self.color = color
        self.ka = ambientParameter
        self.kd = diffusParameter
        self.ks = specularParameter
        self.n = n
        self.reflexFactor = reflexFactor
         
    """calculate Ambient Color"""
    def getAmbientColor(self):
        color = self.color * self.ka
        return Color(color[0], color[1], color[2])
        
    """calculate Diffuse Factor"""
    def getDiffus(self, lightray, norm):
        diff = lightray.direction.dot(norm)
        return diff
    
    """calculate Specular Factor"""
    def getSpecular(self,lightray, norm, ray):
            lr = (lightray.direction - 2*(lightray.direction.dot(norm) * norm)) * (-1)
            specular = lr.dot(ray.direction*(-1))       
            return specular

    """Returns Phongshade on Pixelcolor"""
    def getPhongShader(self,lightray, lightIntansity, norm, ray):                         
        color = Color(0,0,0)
        diff = self.getDiffus(lightray,norm)
        if diff > 0:
            color += self.color * diff * self.kd * lightIntansity   
            specular = self.getSpecular(lightray, norm, ray)
            if specular > 0:
                color += self.ks * lightIntansity * (specular**self.n)
        return Color(color[0], color[1], color[2])
        
    
