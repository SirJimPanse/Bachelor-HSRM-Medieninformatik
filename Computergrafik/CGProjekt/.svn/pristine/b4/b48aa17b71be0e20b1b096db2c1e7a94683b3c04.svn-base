'''
Created on 31.05.2013

@author: Justin Albert, Tino Landmann, Soeren Kroell
'''
from OpenGL.GL import *
from OpenGL.GLUT import *
from OpenGL.GLU import *
from OpenGL.GL.framebufferobjects import glGenerateMipmap
from OpenGL.GL.shaders import *
from mathematic import geometry as geo
from mathematic import MatrixHandler
from numpy import linalg, array
import Image


class Helicopter(object):

    boundingBox = []
    center = []
    scaleFactor = 0.0
    
    #textureIDs = []

    '''lightPos = [1, 0, 1]
    diffCol = [0.7059, 0.3922, 0.2353, 1]
    ambCol = [0.1765, 0.0980, 0.0588, 1]
    specCol = [0.3529, 0.1961, 0.1176, 1]'''
    
    lightPos = [1,1,1]
    diffCol = [180/255.,100/255.,60/255., 1]
    ambCol = [45/255.,25/255.,15/255.,1]
    specCol = [90/255.,50/255.,30/255.,1]

    def __init__(self, objLoader, filename = None, heli_vbo=None, objectVertices=[], objectNormals=[], objectFaces=[], data=[]):
        self.filename = filename
        self.heli_vbo = heli_vbo
        self.objectVertices = objectVertices
        self.objectNormals = objectNormals
        self.objectFaces = objectFaces
        self.data = data
        self.objLoader = objLoader
        self.handler = MatrixHandler()

        vertex = open('shader/heliShader.vert', 'r').read()
        fragment = open('shader/heliShader.frag', 'r').read()
        self.program = compileProgram(compileShader(vertex, GL_VERTEX_SHADER),
                                    compileShader(fragment, GL_FRAGMENT_SHADER))

        # Datei einlesen, verticies, normals, faces, data, vbo berechnen
        self.initHelicopter()


    def initHelicopter(self):
       
        self.objectVertices, self.objectTextures, self.objectNormals, self.objectFaces = self.objLoader.loadObjFile(self.filename)
        self.data, self.heli_vbo = self.objLoader.createDataFromObj()
        
        # Create BoundingBox
        self.boundingBox = [map(min, zip(*self.objectVertices)), map(max, zip(*self.objectVertices))]
        self.center = [(x[0]+x[1])/2.0 for x in zip(*self.boundingBox)]
        self.scaleFactor = 2.0/max([(x[1]-x[0]) for x in zip(*self.boundingBox)])
        
        # Scale, Center
        self.handler.pushModelMatrix(geo.scaleMatrix(self.scaleFactor, self.scaleFactor, self.scaleFactor))
        self.handler.pushModelMatrix(geo.translationMatrix(-self.center[0], -self.center[1], -self.center[2]))
        
        im = Image.open("./heli_data/500DLINE.JPG")
        width, height = im.size
        image = array(im)[::-1,:].tostring()
        self.textureIDs = glGenTextures(1)
        
        glBindTexture(GL_TEXTURE_2D, self.textureIDs)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image)
        glGenerateMipmap(GL_TEXTURE_2D)


    def rotate(self, angle):
        self.handler.pushModelMatrix(geo.rotationMatrix(angle, (0, 1, 0)))
        
    def gier(self, angle):
        self.handler.pushModelMatrix(geo.rotationMatrix(angle, (1, 0, 0)))
        self.handler.pushModelMatrix(geo.translationMatrix(0, 0, 1))
                                     
    def drawHelicopter(self, pMatrix, mvMatrix):
        glEnableClientState(GL_VERTEX_ARRAY)
        glEnableClientState(GL_NORMAL_ARRAY)
        glEnableClientState(GL_TEXTURE_COORD_ARRAY)

        temp = mvMatrix *  self.handler.mvMat
        mvpMatrix = pMatrix * temp

        normalMat = linalg.inv(mvMatrix[0:3, 0:3]).T

        glUseProgram(self.program)
        geo.sendMat4(self.program, "mvMatrix", mvMatrix)
        geo.sendMat4(self.program, "mvpMatrix", mvpMatrix)
        geo.sendMat3(self.program, "normalMatrix", normalMat)
        geo.sendVec4(self.program, "diffuseColor", self.diffCol)
        geo.sendVec4(self.program, "ambientColor", self.ambCol)
        geo.sendVec4(self.program, "specularColor", self.specCol)
        geo.sendVec3(self.program, "lightPosition", self.lightPos)

        self.heli_vbo.bind()
        
        glVertexPointer(3, GL_FLOAT, 36, self.heli_vbo)
        glNormalPointer(GL_FLOAT, 36, self.heli_vbo + 24)
        glTexCoordPointer(3, GL_FLOAT, 36, self.heli_vbo + 12)

        glBindTexture(GL_TEXTURE_2D, self.textureIDs)
        glDrawArrays(GL_TRIANGLES, 0, len(self.data))

        self.heli_vbo.unbind()

        glDisableClientState(GL_VERTEX_ARRAY)
        glDisableClientState(GL_NORMAL_ARRAY)
        glDisableClientState(GL_TEXTURE_COORD_ARRAY)

    def __repr__(self):
        return 'Heli Data: %s' % (repr(self.data[:5]))
