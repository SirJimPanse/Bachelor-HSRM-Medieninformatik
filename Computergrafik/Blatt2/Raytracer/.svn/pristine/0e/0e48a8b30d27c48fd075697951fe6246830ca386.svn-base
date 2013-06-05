'''
Created on 12.04.2013

@author: tland001
'''
import math, Image

from ray import Ray
from vector import Vector
from point import Point
from plane import Plane
from color import Color
class Camera(object):
    def __init__(self,angle,position,upVec,centre):
        self.angle = angle * math.pi / 180
        self.e = position
        self.up = upVec
        self.c = centre
        
        """Definition of coordinate system"""
        self.f = (self.c - self.e).normalized()
        self.s = self.f.crossproduct(self.up).normalized()
        self.u = self.s.crossproduct(self.f) * (-1)
          
    def setViewport(self,width, height):
        self.alpha = self.angle/2
        self.aspectratio = (width/float(height))
        self.height = 2*math.tan(self.alpha)
        self.width = self.aspectratio * self.height
        self.pixelWidth = self.width /(width-1)
        self.pixelHeight = self.height/(height-1)
        
    def calcRay(self,x,y):
        xcomp = self.s.scale(x*self.pixelWidth - self.width/2.0)
        ycomp = self.u.scale(y*self.pixelHeight - self.height/2.0)
        return Ray(self.e, self.f + xcomp + ycomp)

    """Return if object between hitobject and light"""
    def shadow(self,lightray,objectlist, aktobject): 
        for object in objectlist:
            if object != aktobject:
                if object.intersectionParameter(lightray) > 0:
                    return True
        return False
    
    """ Gives distance and hitobject from actual Ray back"""
    def getDist(self, ray, objectlist):
        maxdist = float('inf')
        hitobj = None
        for object in objectlist:
            hitdist = object.intersectionParameter(ray) 
            if hitdist and hitdist < maxdist  and hitdist > 0:
                maxdist = hitdist
                hitobj = object
        return (maxdist, hitobj)
    
    """calculate recursive Color for Pixel"""
    def traceRay(self, level, objlist, light, ray, backgroundcolor):
        (hitdist, hitobj) = self.getDist(ray, objlist)
        if hitobj != None:
            hitpoint = ray.pointAtParameter(hitdist)
            norm = hitobj.normalAt(hitpoint)
            lightray = Ray(hitpoint, light.position - hitpoint)
            color = hitobj.material.getAmbientColor()       
            if not self.shadow(lightray, objlist, hitobj):
               color += hitobj.material.getPhongShader(lightray,light.intansity, norm, ray)  
            if level > 0:
                reflectRay = Ray(hitpoint, ray.direction.reflectRay(norm))
                reflectColor = self.traceRay(level-1, objlist,light,reflectRay, backgroundcolor)
                return color + reflectColor * hitobj.material.reflexFactor
            return color
        return backgroundcolor
   
    def __repr__(self):
        return "".join(("Camera: ", "(", "Angle: ",  str(self.angle),",", " Camera Position: ", str(self.e),",", " Up-Vector: ", str(self.up),",", " Centre: ",str(self.c),")"))