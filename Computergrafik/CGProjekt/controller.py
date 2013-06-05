from OpenGL.GL import *
from OpenGL.GLUT import *
from OpenGL.GLU import *
from OpenGL.arrays import vbo
from OpenGL.GL.framebufferobjects import glGenerateMipmap
from OpenGL.GL.shaders import *
from mathematic import geometry as geo
from mathematic import MatrixHandler
from numpy import cos, sin, cross, dot, array, matrix
from object import Skybox, Helicopter, Camera
from util import ObjLoader

import Image
import time
import sys
import math


skybox = None
stack = None
helicopter = None
WIDTH, HEIGHT = 800, 800
shaderLocation = 'shader'
textureLocation = 'textures'
doRotation = False
cameraHeli = Camera((0, 0, 4), (0, 0, 0), (0, 1, 0), 45., float(WIDTH)/HEIGHT)
cameraSkybox = Camera((0, 0, -4), (0, 0, 0), (0, 1, 0), 45., float(WIDTH)/HEIGHT)
camera2 = Camera((0, -2, 1), (0, 0, 0), (0, 1, 0), 45., float(WIDTH)/HEIGHT)
camera3 = Camera((0, 5, 1), (0, 0, 0), (0, 1, 0), 45., float(WIDTH)/HEIGHT)
HELI_OBJ_FILE = 'heli_data/HELICOPTER500D.obj'

axis = [1, 0, 0]
actOri = matrix([[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]])
angle = 0
moveP = (0.1, 0.1, 0.1)

camList = [cameraHeli, cameraSkybox, camera2, camera3]

actCam = 0

def init():
    global skybox, mvMat, pMat, stack, HELI_OBJ_FILE, helicopter
    skybox = Skybox(shaderLocation, WIDTH, HEIGHT, textureLocation)
    stack = MatrixHandler()
    # create Helicopter
    helicopter = Helicopter(ObjLoader(), HELI_OBJ_FILE)
    glEnable(GL_DEPTH_TEST)


def keyPressed(key, x, y):
    global actCam
    if key == chr(27):
        sys.exit(1)
    elif key == 't':
        stack.pushModelMatrix(geo.rotationMatrix(0.1, (0, 1, 0)))
      
    elif key == '+':  # next texture
        skybox.nextTexture()
        
    elif key == '-':
        skybox.preTexture()
       
    elif key == '1':
        actCam = (actCam + 1) % len(camList)
        glutReshapeWindow(WIDTH, HEIGHT)
       
    elif key == 'a':
        helicopter.rotate(0.01)
        glutReshapeWindow(WIDTH, HEIGHT)
       
    elif key == 'd':
        helicopter.rotate(-0.01)
        glutReshapeWindow(WIDTH, HEIGHT)
       
    elif key == 'w':
        helicopter.gier(-0.01)
        glutReshapeWindow(WIDTH, HEIGHT)
    glutPostRedisplay()


def display():
    global cameraHeli
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)  # clear screen
    cameraHeli.changeSight(moveP)
    #helicopter.rotate(actOri * geo.rotate(angle, axis))
    #stack.pushModelMatrix(actOri * geo.rotate(angle, axis))
    skybox.drawSkybox(stack.getMvpMatrix(), stack.mvMat)
    helicopter.drawHelicopter(camList[actCam].pMat, camList[actCam].mvMat)
    glutSwapBuffers()


def reshape(width, height):
    """ adjust projection matrix to window size"""
    print "actCam ", actCam
    global WIDTH, HEIGHT
    WIDTH, HEIGHT = width, height
    aspect = float(height) / width
    glViewport(0, 0, width, height)
    stack.clear()
    stack.pushProjectMatrix(camList[actCam].pMat)
    stack.pushModelMatrix(camList[actCam].mvMat)

def mousebuttonpressed(button, state, x, y):
    """ Callback Function for Mouse-Event """
    global startP, actOri, angle, doRotation, doZoom, startPoint, doTranslate, fixedPoint
    r = min(WIDTH, HEIGHT) / 2.0
    if button == GLUT_LEFT_BUTTON:
        doZoom, doTranslate = False, False
        if state == GLUT_DOWN:
            doRotation = True
            startP = geo.projectOnSphere(x, y, r, WIDTH, HEIGHT)
        if state == GLUT_UP:
            doRotation = False
            actOri = actOri * geo.rotate(angle, axis)
            angle = 0


def mousemoved(x, y):
    """ Callback Function f or MouseMotion """
    global angle, axis, translate, startPoint, transX, transY, fixedPoint, objectPosition, moveP

    if doRotation:
        r = min(WIDTH, HEIGHT) / 2.0
        moveP = geo.projectOnSphere(x, y, r, WIDTH, HEIGHT)
        angle = math.acos(dot(startP, moveP))
        axis = cross(startP, moveP)
        glutPostRedisplay()


def main():
    glutInit(sys.argv)
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB)  # bitmaske
    glutInitWindowSize(WIDTH, HEIGHT)
    glutCreateWindow("HelicopterAnimation")
    glutDisplayFunc(display)
    glutKeyboardFunc(keyPressed)
    glutReshapeFunc(reshape)
    glutMouseFunc(mousebuttonpressed)
    glutMotionFunc(mousemoved)
    init()
    glutMainLoop()

if __name__ == '__main__':
    main()
