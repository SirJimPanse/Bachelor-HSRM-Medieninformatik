'''
Created on 20.05.2013

@author: tland001
'''
'''
Created on 20.05.2013

@author: tland001
'''

from OpenGL.GL import *
from OpenGL.GLUT import *
from OpenGL.arrays import vbo
from numpy import array
import sys
import math

transPoints = []


def readfle(string):
    fle = file(string).read().split()
    count = 0
    akt = []
    for i in fle[::2]:
        akt.append([float(i), float(fle[count + 1])])
        count += 2
    return akt


def createBoundingBox(pointList):
    minX = min([x[0] for x in pointList])
    maxX = max([x[0] for x in pointList])
    minY = min([x[1] for x in pointList])
    maxY = max([x[1] for x in pointList])
    return [[minX, maxX], [minY, maxY]]


def translate(BoundingBox):
    global transPoints
    midX = BoundingBox[0][0] + (BoundingBox[0][1] - BoundingBox[0][0]) / 2.
    midY = BoundingBox[1][0] + (BoundingBox[1][1] - BoundingBox[1][0]) / 2.
    for i in points:
        x, y = i[0] - midX, \
               i[1] - midY
        transPoints.append([x, y])


def scale():
    global transPoints
    maxX = max([x[0] for x in points])
    maxY = max([y[1] for y in points])
    maxV = max(maxX, maxY)
    for i in transPoints:
        i[0] = i[0] / maxV
        i[1] = i[1] / maxV


points = readfle("trianglePoints.obj")
boundingBox = createBoundingBox(points)
translate(boundingBox)
scale()
triList = [transPoints[0], transPoints[1], transPoints[2],
           transPoints[3], transPoints[4], transPoints[5],
           transPoints[6], transPoints[5], transPoints[7],
           transPoints[8], transPoints[7], transPoints[9],
           transPoints[10], transPoints[11], transPoints[12],
           transPoints[13]]
vbo = vbo.VBO(array(triList, 'f'))


def initGL(width, heigt):
    glClearColor(0.0, 0.0, 1.0, 0.0)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    glOrtho(-1.5, 1.5, -1.5, 1.5, -1.0, 1.0)
    glMatrixMode(GL_MODELVIEW)


def display():
    global vbo
    glClear(GL_COLOR_BUFFER_BIT)
    glColor3f(.75, .75, .75)
    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
    vbo.bind()
    glVertexPointerf(vbo)
    glEnableClientState(GL_VERTEX_ARRAY)
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 16)
    vbo.unbind()
    glDisableClientState(GL_VERTEX_ARRAY)
    glFlush()


def main():
    glutInit(sys.argv)
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB)
    glutInitWindowSize(500, 500)
    glutCreateWindow("Einfaches_Open_GL")
    glutDisplayFunc(display)
    initGL(500, 500)
    glutMainLoop()


if __name__ == "__main__":
    main()
