'''
Created on 12.04.2013

@author: tland001
'''
from vector import Vector
from point import Point
from camera import Camera
from ray import Ray
from sphere import Sphere
from plane import Plane
from light import Light
from material import Material
from color import Color
import Image

WIDTH = 800 
HEIGHT = 600
BACKGROUND_COLOR = Color(0,0,0)

"""calculate Pixel and draws View"""
def drawView(camera,objectlist,width,height,backgroundColor,light, level):
    camera.setViewport(width, height)
    image = Image.new("RGB", (width,height))
    for x in range(width):
        for y in range(height):
            ray = camera.calcRay(x,y)
            color = camera.traceRay(level, objectlist, light, ray, BACKGROUND_COLOR)                                   
            image.putpixel((x, y), (int(color[0]*255),int(color[1]*255),int(color[2]*255)))
    image.save("raytracer_recursive.png","PNG")
        
if __name__ == "__main__":

    """Camerasettings"""  
    camera = Camera(45,Point(0,2,10),Vector(0,1,0), Point(0,3,0))
    
    """Light"""
    light = Light(Point(30,30,10),1)
    
    """Materialsettings - Color, AmbientFactor, DiffuseFactor, SpecularFactor, n, ReflectionFactor"""
    redMaterial = Material(Color(0.7,0,0), 0.3, 0.7, 0.2, 10, 0.11)
    greenMaterial = Material(Color(0,0.7,0), 0.3, 0.7, 0.2, 10, 0.11)
    blueMaterial = Material(Color(0,0,0.7), 0.3, 0.7, 0.2, 10, 0.11)
    planeMaterial = Material(Color(0.5,0.5,0.5), 0.2, 0.5, 0, 0, 0)
    
    sphereRed = Sphere(Point(2.5,3,-10),2, redMaterial)
    sphereGreen = Sphere(Point(-2.5,3,-10),2, greenMaterial)
    sphereBlue = Sphere(Point(0,7,-10),2, blueMaterial)
    plane = Plane(Point(0,0,0),Vector(0,1,0),planeMaterial)
    
    objlist = [plane,sphereRed,sphereGreen,sphereBlue]

    """Initialize Viewport"""
    drawView(camera,objlist, WIDTH, HEIGHT, BACKGROUND_COLOR,light,1)

