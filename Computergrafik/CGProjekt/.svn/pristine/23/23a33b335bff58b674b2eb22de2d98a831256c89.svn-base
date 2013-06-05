'''
Created on 31.05.2013

@author: Justin Albert, Tino Landmann, Soeren Kroell
'''

from OpenGL.GL import *
from OpenGL.GLUT import *
from OpenGL.GLU import *
from OpenGL.arrays import vbo
from mathematic import *
from OpenGL.GL.framebufferobjects import glGenerateMipmap
from OpenGL.GL.shaders import *
from mathematic import *
from numpy import array
import Image
import os


class Skybox(object):

    def __init__(self, shaderlocation, width, height, texturelocation):
        p = [[-5, 5, 5], [5, 5, 5], [-5, -5, 5], [5, -5, 5],
             [-5, 5, -5], [5, 5, -5], [-5, -5, -5], [5, -5, -5]]

        '''self.data = [p[3] + [0, 0], p[1] + [0, 1], p[0] + [1, 1], p[2] + [1, 0],  # front
                     p[2] + [0, 0], p[6] + [1, 0], p[4] + [1, 1], p[0] + [0, 1],  # left
                     p[6] + [0, 0], p[7] + [1, 0], p[5] + [1, 1], p[4] + [0, 1],  # back
                     p[7] + [0, 0], p[3] + [1, 0], p[1] + [1, 1], p[5] + [0, 1],  #right
                     p[0] + [1, 0], p[1] + [0, 0], p[5] + [0, 1], p[4] + [1, 1],  # top 
                     p[2] + [1, 1], p[3] + [0, 1], p[7] + [0, 0], p[6] + [1, 0]]  # bottom'''
        
        self.data = [p[1] + [0, 0], p[0] + [1, 0], p[2] + [1, 1], p[3] + [0, 1],  # front
                     p[0] + [0, 0], p[4] + [1, 0], p[6] + [1, 1], p[2] + [0, 1],  # left
                     p[4] + [0, 0], p[5] + [1, 0], p[7] + [1, 1], p[6] + [0, 1],  # back
                     p[5] + [0, 0], p[1] + [1, 0], p[3] + [1, 1], p[7] + [0, 1],  #right
                     p[5] + [0, 0], p[4] + [1, 0], p[0] + [1, 1], p[1] + [0, 1],  # top 
                     p[3] + [0, 0], p[2] + [1, 0], p[6] + [1, 1], p[7] + [0, 1]]  # bottom

        self.loadShaders(shaderlocation)
        self.actTex = 0
        self.initSkybox(width, height, texturelocation)

    def loadShaders(self, location):
        vertex = open(os.path.join(location, 'imageShader.vert'), 'r').read()
        frag = open(os.path.join(location, 'imageShaderFrag.frag'), 'r').read()
        self.program = compileProgram(compileShader(vertex, GL_VERTEX_SHADER),
                                    compileShader(frag, GL_FRAGMENT_SHADER))

    def initSkybox(self, width, height, location):
        faces = ['front', 'left', 'back', 'right', 'top', 'bottom']
        paths = ['beach', 'clouds', 'water']

        self.fileLength = len(faces)

        self.myVBO = vbo.VBO(array(self.data, 'f'))

        self.boxes = len(paths)
        self.textureList = []

        for path in paths:

            textureIDs = glGenTextures(self.fileLength)

            for c, pic in enumerate(faces):
                im = Image.open(os.path.join(location, path, pic + ".png"))
                width, height = im.size
                #image = array(im)[::-1, :].tostring()  # mirror image on y-axis
                image = array(im).tostring()  # mirror image on y-axis

                glBindTexture(GL_TEXTURE_2D, textureIDs[c])
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB,
                             GL_UNSIGNED_BYTE, image)
                glGenerateMipmap(GL_TEXTURE_2D)

            self.textureList.append(textureIDs)

    def drawSkybox(self, mvpMatrix, mvMatrix):
        """ zeichnet die Skybox """
        glEnableClientState(GL_VERTEX_ARRAY)
        glEnableClientState(GL_TEXTURE_COORD_ARRAY)

        glUseProgram(self.program)
        sendMat4(self.program, "mvMatrix", mvMatrix)
        sendMat4(self.program, "mvpMatrix", mvpMatrix)

        self.myVBO.bind()

        glVertexPointer(3, GL_FLOAT, 20, self.myVBO)
        glTexCoordPointer(2, GL_FLOAT, 20, self.myVBO + 12)

        for e in range(0, self.fileLength):
            glBindTexture(GL_TEXTURE_2D, self.textureList[self.actTex][e])
            glDrawArrays(GL_QUADS, e * 4, 4)

        self.myVBO.unbind()

        glDisableClientState(GL_VERTEX_ARRAY)
        glDisableClientState(GL_TEXTURE_COORD_ARRAY)

    def nextTexture(self):
        self.actTex = (self.actTex + 1) % self.boxes
        
    def preTexture(self):
        self.actTex = self.actTex - 1
        if self.actTex < 0:
            self.actTex = self.boxes - 1
