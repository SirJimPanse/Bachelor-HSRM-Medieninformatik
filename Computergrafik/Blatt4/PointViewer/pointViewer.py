'''
Created on 03.05.2013

@author: tland001
'''

from Tkinter import *
from Canvas import *
import sys
import math
import random
import numpy
# import num.py um matrizen zu multiplizieren

WIDTH = 400  # width of canvas
HEIGHT = 400  # height of canvas

HPSIZE = 1  # double of point size (must be integer)
COLOR = "#0000FF"  # blue

NOPOINTS = 1000

pointList = []  # list of points (used by Canvas.delete(...))
points = []

rotAngle = 10 * (math.pi / 180)
rotMatrix = [math.cos(rotAngle), 0, math.sin(rotAngle),   0,\
                      0,         1,           0,          0,\
           - math.sin(rotAngle), 0, math.cos(rotAngle),   0,\
                      0,         0,           0,          1]

upVec = (0, 1, 0)
alpha = 60 / (math.pi / 180)

aspect = WIDTH / HEIGHT


def readfle(string):
    fle = file(string).read().split()
    count = 0
    akt = []
    for i in fle[::3]:
        akt.append([float(i), float(fle[count + 1]), float(fle[count + 2])])
        count += 3
    return akt


def createBoundingBox(pointList):
    minX = min([x[0] for x in pointList])
    maxX = max([x[0] for x in pointList])
    minY = min([x[1] for x in pointList])
    maxY = max([x[1] for x in pointList])
    minZ = min([x[2] for x in pointList])
    maxZ = max([x[2] for x in pointList])
    return [[minX, maxX], [minY, maxY], [minZ, maxZ]]


def createBoundingSphere(BoundingBox):
    midX = BoundingBox[0][0] + (BoundingBox[0][1] - BoundingBox[0][0]) / 2.
    midY = BoundingBox[1][0] + (BoundingBox[1][1] - BoundingBox[1][0]) / 2.
    midZ = BoundingBox[2][0] + (BoundingBox[2][1] - BoundingBox[2][0]) / 2.
    mid = [midX, midY, midZ]

    xLength = math.fabs(BoundingBox[0][1] - midX)
    yLength = math.fabs(BoundingBox[1][1] - midY)
    zLength = math.fabs(BoundingBox[2][1] - midZ)
    radius = max(xLength, yLength, zLength)
    return [mid, radius]


def translate(BoundingBox):
    global points
    midX = BoundingBox[0][0] + (BoundingBox[0][1] - BoundingBox[0][0]) / 2.
    midY = BoundingBox[1][0] + (BoundingBox[1][1] - BoundingBox[1][0]) / 2.
    midZ = BoundingBox[2][0] + (BoundingBox[2][1] - BoundingBox[2][0]) / 2.
    for i in pointCloud:
        x, y, z = i[0] - midX, \
               i[1] - midY, \
               i[2] - midZ
        points.append([x, y, z])


def scale():
    global points
    maxX = max([x[0] for x in points])
    maxY = max([y[1] for y in points])
    maxZ = max([z[2] for z in points])
    maxV = max(maxX, maxY, maxZ)
    for i in points:
        i[0] = i[0] / maxV
        i[1] = i[1] / maxV
        i[2] = i[2] / maxV


def transform():
    global points
    transformedObject = []
    for i in points:
        x = WIDTH - ((i[0] * (WIDTH / 2)) + WIDTH / 2)
        y = HEIGHT - ((i[1] * (HEIGHT / 2)) + HEIGHT / 2)
        transformedObject.append([x, y])
    return transformedObject


def quit(root=None):
    """ quit programm """
    if root == None:
        sys.exit(0)
    root._root().quit()
    root._root().destroy()


def setObject():
    boundingBox = createBoundingBox(pointCloud)
    translate(boundingBox)
    scale()


def draw():
    """ draw points """
    transformObject = transform()
    for i in transformObject:
        x, y = 400 - i[0], i[1]
        p = can.create_oval(x - HPSIZE, y - HPSIZE, x + HPSIZE, y + HPSIZE,
                            fill=COLOR, outline=COLOR)
        pointList.insert(0, p)


def rotYp():
    """ rotate counterclockwise around y axis """
    for i in points:
        i[0] = i[0] * rotMatrix[0] + i[1] * rotMatrix[1] + i[2] * rotMatrix[2]
        i[1] = i[0] * rotMatrix[4] + i[1] * rotMatrix[5] + i[2] * rotMatrix[6]
        i[2] = i[0] * rotMatrix[8] + i[1] * rotMatrix[9] + i[2] * rotMatrix[10]
    print points[1]
    can.delete(*pointList)
    draw()


def rotYn():
    """ rotate clockwise around y axis """

    for i in points:
        i[0] = i[0] * rotMatrix[0] + i[1] * rotMatrix[1] + i[2] * rotMatrix[2]
        i[1] = i[0] * rotMatrix[4] + i[1] * rotMatrix[5] + i[2] * rotMatrix[6]
        i[2] = i[0] * rotMatrix[8] + i[1] * rotMatrix[9] + i[2] * rotMatrix[10]
    #print "In rotYn: ", NOPOINTS
    can.delete(*pointList)
    draw()


if __name__ == "__main__":
    #check parameters
    """
    if len(sys.argv) != 1:
       print "pointViewerTemplate.py"
       sys.exit(-1)
    """
    # create main window
    mw = Tk()
    pointCloud = readfle("elephant.obj")
    # create and position canvas and buttons
    cFr = Frame(mw, width=WIDTH, height=HEIGHT, relief="sunken", bd=1)
    cFr.pack(side="top")
    can = Canvas(cFr, width=WIDTH, height=HEIGHT)
    can.pack()
    bFr = Frame(mw)
    bFr.pack(side="left")
    bRotYn = Button(bFr, text="<-", command=rotYn)
    bRotYn.pack(side="left")
    bRotYp = Button(bFr, text="->", command=rotYp)
    bRotYp.pack(side="left")
    eFr = Frame(mw)
    eFr.pack(side="right")
    bExit = Button(eFr, text="Quit", command=(lambda root=mw: quit(root)))
    bExit.pack()

    # draw points
    setObject()
    draw()

    # start
    mw.mainloop()
