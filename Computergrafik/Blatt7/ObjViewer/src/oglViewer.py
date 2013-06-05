'''
Created on 20.05.2013

@author: tland001
Tastenkonfig:
A = Objekt nach Links drehen (um Y-Achse)
D = Objekt nach Rechts drehen (um Y-Achse)
S = Objekt nach unten drehen (um X-Achse)
W = Objekt nach Oben drehen (um X-Achse)
Q = Objekt nach Links drehen (um Z-Achse)
E = Objekt nach Rechts drehen (um Z-Achse)

P = Perspektive Wechseln (Orthogonal / Perspektivisch)

+ = Hintergrundfarbe wechseln
- = Objektfarbe wechseln

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

WIDTH, HEIGHT = 500, 500
bgColor = 0
objColor = 0
color = (1.0, 1.0, 0.0)

doZoom = False
doRot = False
doTrans = False

perspect = False
ortho = True

aspect = float(WIDTH/HEIGHT)
fov = 45
near = 0.1
far = 30.0

axis = [1, 0, 0]
angle = 0
angleX, angleY, angleZ = 0, 0, 0
actOri = matrix([[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]])

oldY = 0.0
oldX = 0.0
newX = 0.0
newY = 0.0

distance = 0

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


def init(WIDTH, HEIGHT):
    """ Initialize an OpenGL window """
    glClearColor(0.0, 0.0, 0.0, 0.0)  # background color
    if perspect:  
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        gluPerspective(fov, aspect, near, far)
        gluLookAt (0,0,4 + scale,0,0,0,0,1,0)
        glMatrixMode(GL_MODELVIEW)
    elif ortho:     
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(-1.5, 1.5, -1.5, 1.5, -10.0, 10.0)
        glMatrixMode(GL_MODELVIEW)


def display():
    #print center
    """ Render all objects"""
    glClear(GL_COLOR_BUFFER_BIT)  # clear screen
    glColor(color[0], color[1], color[2])  # render stuff   
    glLoadIdentity()
    
    glTranslate(newX, newY, 0.0)                    
    glMultMatrixf(actOri * rotate(angle, axis))
    glScale(scale, scale, scale)
    
    glTranslate(-center[0], -center[1], -center[2])

    vbo.bind()
    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
    glVertexPointerf(vbo)
    glEnableClientState(GL_VERTEX_ARRAY)
    glDrawArrays(GL_TRIANGLES, 0, len(points))
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
    global WIDTH, HEIGHT, aspect
    WIDTH, HEIGHT = width, height
    glViewport(0, 0, WIDTH, HEIGHT)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()  
    if ortho:
        if WIDTH <= HEIGHT:
            glOrtho(-1.5, 1.5, -1.5 * HEIGHT / WIDTH, 1.5 * HEIGHT / WIDTH, -10.0, 10.0)
        else:
            glOrtho(-1.5 * WIDTH / HEIGHT, 1.5 * WIDTH / HEIGHT,-1.5, 1.5, -10.0, 10.0)
    if perspect:
        if WIDTH <= HEIGHT:
            aspectHeight = float(HEIGHT)/ WIDTH
            aspect = float(WIDTH)/HEIGHT
            gluPerspective(fov*aspectHeight, aspect, near, far)
        else:
            aspect =  float(WIDTH/HEIGHT)
            gluPerspective(fov, aspect, near, far)
        gluLookAt (0,0,4 + scale,0,0,0,0,1,0) 

    glMatrixMode(GL_MODELVIEW)


def projectOnSphere(x, y, r):
    x, y = x - WIDTH / 2.0, HEIGHT / 2.0 - y
    a = min(r * r, x ** 2 + y ** 2)
    z = sqrt(r * r - a)
    l = sqrt(x ** 2 + y ** 2 + z ** 2)
    return x / l, y / l, z / l


def keyPressed(key, x, y):
    """ handle keypress events """
    global angleX, angleY, angleZ, angle, actOri
    global scale
    global bgColor, objColor, color
    global perspect, ortho

    if key == chr(27):  # chr(27) = ESCAPE
        sys.exit()
    #Rotation x down
    elif key == 's':
        angle = (angle + 10) / float(WIDTH)
        axis = [1, 0, 0]
        actOri = actOri * rotate(angle, axis)       
    #Rotation x up
    elif key == 'w':
        angle = (angle - 10) / float(WIDTH)
        axis = [1, 0, 0]
        actOri = actOri * rotate(angle, axis)
    #Rotation y right
    elif key == 'd':
        angle = (angle + 10) / float(HEIGHT)
        axis = [0, 1, 0]
        actOri = actOri * rotate(angle, axis)
    #Rotation y left
    elif key == 'a':
        angle = (angle - 10) / float(HEIGHT) 
        axis = [0, 1, 0]
        actOri = actOri * rotate(angle, axis)
    #Rotate z left
    elif key == 'q':
        angle = (angle + 10) / float(HEIGHT)
        axis = [0, 0, 1]
        actOri = actOri * rotate(angle, axis)
    #Rotate z right
    elif key == 'e':
        angle = (angle - 10) / float(HEIGHT)
        axis = [0, 0, 1]
        actOri = actOri * rotate(angle, axis)

    #backcolor
    if key == '+':
        if bgColor == 5:
            bgColor = 0
        else:
            bgColor += 1
            
    if key == "-":
        if objColor == 5:
            objColor = 0
        else:
            objColor += 1
            
    if key == "p":
        if perspect:
            perspect = False
            ortho = True
        else:
            perspect = True
            ortho = False
        init(WIDTH, HEIGHT)
            
    if bgColor == 0:
        glClearColor(0.0, 0.0, 0.0, 0.0)  # black
    elif bgColor == 1:
        glClearColor(0.0, 1.0, 0.0, 0.0)  # gree
    elif bgColor == 2:
        glClearColor(1.0, 0.0, 0.0, 0.0)  # red
    elif bgColor == 3:
        glClearColor(0.0, 0.0, 1.0, 0.0)  # blue
    elif bgColor == 4:
        glClearColor(1.0, 1.0, 1.0, 0.0)  # white
    elif bgColor == 5:
        glClearColor(1.0, 1.0, 0.0, 0.0)  # yellow
        
    if objColor == 0:
        color = (1.0, 1.0, 0.0)  # yellow
    elif objColor == 1:
        color = (0.0, 1.0, 0.0)  # green
    elif objColor == 2:
        color = (1.0, 0.0, 0.0)  # red
    elif objColor == 3:
        color = (1.0, 1.0, 1.0)  # white
    elif objColor == 4:
        color = (0.0, 0.0, 0.0)  # black
    elif objColor == 5:
        color = (0.0, 0.0, 10)  # blue
        
    glutPostRedisplay()


def mousebuttonpressed(button, state, x, y):
    global startP, actOri, angle, doRot, scale, doZoom, doTrans, oldX, oldY
    r = min(WIDTH, HEIGHT) / 2.0
    oldX, oldY = 0, 0
    if button == GLUT_LEFT_BUTTON:
        if state == GLUT_DOWN:
            doRot = True
            startP = projectOnSphere(x, y, r)
        if state == GLUT_UP:
            doRot = False
            actOri = actOri * rotate(angle, axis)
            angle = 0
    elif button == GLUT_MIDDLE_BUTTON:
        if state == GLUT_DOWN:
            doZoom = True
        if state == GLUT_UP:
            doZoom = False
    elif button == GLUT_RIGHT_BUTTON:
        if state == GLUT_DOWN:
            doTrans = True
        if state == GLUT_UP:
            doTrans = False
            

def mouseMotion(x, y):
    global angle, axis, oldY, oldX, oldZ, center, angleX, angleY, anglez, newX, newY, distance, scale  
    transX, transY = 0, 0
    if oldX:
      transX = x - oldX 
    if oldY:
      transY = y - oldY   
      
    if doRot:
        r = min(WIDTH, HEIGHT) / 2.0
        moveP = projectOnSphere(x, y, r)
        angle = acos(dot(startP, moveP))
        axis = cross(startP, moveP)
    elif doZoom:
        print oldY, y
        if oldY > y:
            scale = scale * 1.1
        if oldY < y:
            scale = scale * 0.9
    elif doTrans:
        print "old", newX, newY
        if x - oldX != 0:
            newX = newX + transX / float(WIDTH)
        if y - oldY != 0:
            newY = newY - transY / float(HEIGHT)
        print "new", newX, newY
    oldX = x
    oldY = y
    glutPostRedisplay()
    
def menu_func(value):
    """ handle menue selection """
    print "menue entry ", value, "choosen..."
    if value == EXIT:
        sys.exit()
        glutPostRedisplay()


def main():
    # Hack for Mac OS X
    cwd = os.getcwd()
    glutInit(sys.argv)
    os.chdir(cwd)

    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB)
    glutInitWindowSize(500, 500)
    glutCreateWindow("simple openGL/GLUT template")

    glutDisplayFunc(display)  # register display function
    glutReshapeFunc(reshape)  # register reshape function
    glutKeyboardFunc(keyPressed)  # register keyboard function
    glutMouseFunc(mousebuttonpressed) #register pressed function 
    glutMotionFunc(mouseMotion)  #register motion function
    glutCreateMenu(menu_func)  # register menue function

    glutAddMenuEntry("First Entry", FIRST)  # Add a menu entry
    glutAddMenuEntry("EXIT", EXIT)  # Add another menu entry
    # glutAttachMenu(GLUT_RIGHT_BUTTON)  # Attach mouse button to menue

    init(WIDTH, HEIGHT)  # initialize OpenGL state

    glutMainLoop()  # start even processing


if __name__ == "__main__":
    main()