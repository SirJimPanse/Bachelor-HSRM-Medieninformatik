'''
Created on 20.05.2013

@author: tland001
'''
#import oglFixme
from OpenGL.GLUT import *
from OpenGL.GLU import *
from OpenGL.GL import *
from OpenGL.arrays import vbo
from numpy import *
from math import *
import sys
import os

EXIT = -1
FIRST = 0

width, height = 500, 500

axis = [1, 0, 0]
angle = 0
angleX, angleY, angleZ = 0, 0, 0
actOri = matrix([[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]])


def readfle(string):
    fle = [x.split() for x in file(string)]
    v, f = [], []
    for i in fle:
        if i != [] and i[0] == "v":
            v.append([float(i[1]), float(i[2]), float(i[3])])
        if i != [] and i[0] == "f":
            f.append([int(i[1].split('//')[0]), int(i[2].split('//')[0]),
                      int(i[3].split('//')[0])])
    vTri = [v[y - 1] for x in f for y in x]
    return vTri

points = readfle("bunny.obj")
vbo = vbo.VBO(array(points, 'f'))

boundingBox = [map(min, zip(*points)), map(max, zip(*points))]
scale = 2.0 / max([(x[1] - x[0]) for x in zip(*boundingBox)])

center = [(x[0] + x[1]) / 2.0 for x in zip(*boundingBox)]


def init(width, height):
    """ Initialize an OpenGL window """
    glClearColor(0.0, 0.0, 0.0, 0.0)  # background color
    glMatrixMode(GL_PROJECTION)  # switch to projection matrix
    glLoadIdentity()  # set to 1
    glOrtho(-1.5, 1.5, -1.5, 1.5, -1.0, 1.0)  # multiply with new p-matrix
    glMatrixMode(GL_MODELVIEW)  # switch to modelview matrix


def display():
    """ Render all objects"""
    print "testsaaa"
    glClear(GL_COLOR_BUFFER_BIT)  # clear screen
    glColor(0.0, 0.0, 1.0)  # render stuff
    glLoadIdentity()

    glRotate(angleX, 1, 0, 0)
    glRotate(angleY, 0, 1, 0)
    glRotate(angleZ, 0, 0, 1)

    glScale(scale, scale, scale)
    glTranslatef(-center[0], -center[1], -center[2])
    glMultMatrixf(actOri * rotate(angle, axis))

    vbo.bind()
    glVertexPointerf(vbo)
    glEnableClientState(GL_VERTEX_ARRAY)
    glDrawArrays(GL_POINTS, 0, len(points))
    vbo.unbind()
    glDisableClientState(GL_VERTEX_ARRAY)
    glutSwapBuffers()  # swap buffer


def rotate(angle, axis):
    c, mc = cos(float(angle)), 1 - cos(float(angle))
    s = sin(angle)
    l = sqrt(dot(array(axis), array(axis)))
    x, y, z = array(axis) / l
    r = matrix(
               [[x * x * mc + c, x * y * mc - z * s, x * z * mc + y * s, 0],\
                [x * y * mc + z * s, y * y * mc + c, y * z * mc - x * s, 0],\
                [x * z * mc - y * s, y * z * mc + x * s, z * z * mc + c, 0],
                [0, 0, 0, 1]])
    return r.transpose()


def reshape(width, height):
    """ adjust projection matrix to window size"""
    print width, height
    glViewport(0, 0, width, height)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    if width <= height:
        glOrtho(-1.5, 1.5,
               -1.5 * height / width, 1.5 * height / width,
               -1.0, 1.0)
    else:
        glOrtho(-1.5 * width / height, 1.5 * width / height,
               -1.5, 1.5,
               -1.0, 1.0)
    glMatrixMode(GL_MODELVIEW)


def projectOnSphere(x, y, r):
    x, y = x - width / 2.0, height / 2.0 - y
    a = min(r * r, x ** 2 + y ** 2)
    z = sqrt(r * r - a)
    l = sqrt(x ** 2 + y ** 2 + z ** 2)
    return x / l, y / l, z / l


def keyPressed(key, x, y):
    """ handle keypress events """
    global angleX, angleY, angleZ
    global scale

    if key == chr(27):  # chr(27) = ESCAPE
        sys.exit()
    #Rotation x down
    if key == 's':
        angleX = (angleX + 1) % 360
    #Rotation x up
    if key == 'w':
        angleX = (angleX - 1) % 360
    #Rotation y right
    if key == 'd':
        angleY = (angleY + 1) % 360
    #Rotation y left
    if key == 'a':
        angleY = (angleY - 1) % 360
    #Rotate z left
    if key == 'q':
        angleZ = (angleZ + 1) % 360
    #Rotate z right
    if key == 'e':
        angleZ = (angleZ - 1) % 360
    #Zoom in
    if key == '+':
        scale = scale * 1.1
    #Zoom out
    if key == '-':
        scale = scale * 0.9

    glutPostRedisplay()


def mouse(button, state, x, y):
    """ handle mouse events """
    if button == GLUT_LEFT_BUTTON and state == GLUT_DOWN:
        print "left mouse button pressed at ", x, y


def mousebuttonpressed(button, state, x, y):
    global startP, actOri, angle, doRotation
    r = min(width, height) / 2.0
    if button == GLUT_LEFT_BUTTON:
        if state == GLUT_DOWN:
            doRotation = True
            startP = projectOnSphere(x, y, r)
        if state == GLUT_UP:
            doRotation = False
            actOri = actOri * rotate(angle, axis)
            angle = 0


def mousemoved(x, y):
    global angle, axis, scaleFactor
    if doRotation:
        r = min(width, height) / 2.0
        moveP = projectOnSphere(x, y, r)
        angle = acos(dot(startP, moveP))
        axis = cross(startP, moveP)
        glutPostRedisplay()


def mouseMotion(x, y):
    """ handle mouse motion """
    print "mouse motion at ", x, y


def menu_func(value):
    """ handle menue selection """
    print "menue entry ", value, "choosen..."
    if value == EXIT:
        sys.exit()
        glutPostRedisplay()


def main():
    cwd = os.getcwd()
    glutInit(sys.argv)
    os.chdir(cwd)

    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB)
    glutInitWindowSize(500, 500)
    glutCreateWindow("simple openGL/GLUT template")

    glutDisplayFunc(display)  # register display function
    glutReshapeFunc(reshape)  # register reshape function
    glutKeyboardFunc(keyPressed)  # register keyboard function
    glutMouseFunc(mouse)  # register mouse function
    glutMotionFunc(mouseMotion)  # register motion function
    glutCreateMenu(menu_func)  # register menue function

    glutAddMenuEntry("First Entry", FIRST)  # Add a menu entry
    glutAddMenuEntry("EXIT", EXIT)  # Add another menu entry
    glutAttachMenu(GLUT_RIGHT_BUTTON)  # Attach mouse button to menue

    init(500, 500)  # initialize OpenGL state

    glutMainLoop()  # start even processing


if __name__ == "__main__":
    main()
