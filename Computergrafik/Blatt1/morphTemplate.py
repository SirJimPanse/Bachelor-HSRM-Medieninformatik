from Tkinter import *
from Canvas import *
import sys
WIDTH  = 400 # width of canvas
HEIGHT = 400 # height of canvas
HPSIZE = 2 # half of point size (must be integer)
CCOLOR = "#0000FF" # blue
elementList = [] # list of elements (used by Canvas.delete(...))
polygon = [[50,50],[350,50],[350,350],[50,350],[50,50]]
time = 0
dt = 0.01


def readfle(string):
    fle = file(string).read().split()
    count = 0
    akt = []
    for i in fle[::2]:
        akt.append([float(i)*400,400 - (float(fle[count+1])*400)])
        count +=2
    return akt


def drawObjekts():
    """ draw polygon and points """
    # TODO: inpterpolate between polygons and render
    for (p,q) in zip(polygon,polygon[1:]):
        elementList.append(can.create_line(p[0], p[1], q[0], q[1],
                                           fill=CCOLOR))
        elementList.append(can.create_oval(p[0]-HPSIZE, p[1]-HPSIZE,
                                           p[0]+HPSIZE, p[1]+HPSIZE,
                                           fill=CCOLOR, outline=CCOLOR))
           
def quit(root=None):
    """ quit programm """
    if root==None:
        sys.exit(0)
    root._root().quit()
    root._root().destroy()


def draw():
    """ draw elements """
    can.delete(*elementList)
    del elementList[:]
    drawObjekts()
    can.update()


def interpolate(p,q,time):
    return (1-time)*p + time*q


def forward():
    global time
    global polygon
    while(time<1):
        time += dt
        # TODO: interpolate
        aktList = []
        count = 0
        for i in polygonA:
            aktList.append([interpolate(i[0],polygonZ[count][0],time),interpolate(i[1],polygonZ[count]	   [1],time)])
            count += 1
        polygon = aktList
        print time
        draw()


def backward():
    global time
    global polygon
    while(time>0):
        time -= dt
        # TODO: interpolate
        aktList = []
        count = 0
        for i in polygonZ:
            aktList.append([interpolate(polygonA[count][0],i[0],time),interpolate(polygonA[count][1],i[1],time)])
            count += 1
        polygon = aktList
        print time
        draw()
   
if __name__ == "__main__":
    # check parameters
    """
    if len(sys.argv) != 3:
       print "morph.py firstPolygon secondPolygon"
       sys.exit(-1)
    """
    # TODOS:
    # - read in polygons
    # - transform from local into global coordinate system
    # - make both polygons contain same number of points
    polygonA = readfle("polygonA.dat")
    polygonZ = readfle("polygonZ.dat")
    polygonZ.append(polygonZ[-1])
   
    polygon = polygonA
    # create main window
    mw = Tk()
    mw._root().wm_title("Morphing")
    # create and position canvas and buttons
    cFr = Frame(mw, width=WIDTH, height=HEIGHT, relief="sunken", bd=1)
    cFr.pack(side="top")
    can = Canvas(cFr, width=WIDTH, height=HEIGHT)
    can.pack()
    cFr = Frame(mw)
    cFr.pack(side="left")
    bClear = Button(cFr, text="backward", command=backward)
    bClear.pack(side="left")
    bClear = Button(cFr, text="forward", command=forward)
    bClear.pack(side="left")
    eFr = Frame(mw)
    eFr.pack(side="right")
    bExit = Button(eFr, text="Quit", command=(lambda root=mw: quit(root)))
    bExit.pack()
    draw()
   
    # start
    mw.mainloop()
