'''
Created on 31.05.2013

@author: Justin Albert, Tino Landmann, Soeren Kroell
'''

from OpenGL.arrays import vbo
from numpy import array


class ObjLoader(object):

    def __init__(self, filename=None):
        self.filename = filename
        self.objectVertices = []
        self.objectTextures = []
        self.objectNormals = []
        self.objectFaces = []
        self.data = []
        self.my_vbo = None

    def loadObjFile(self, filename=None):
        '''
        Load .obj File and return three lists with object-vertices, object-normals and object-faces
        '''
        if filename == None:
            filename = self.filename
        print filename
        try:
            for lines in file(filename):
                print lines
                # check if not empty
                if lines.split():
                    check = lines.split()[0]
                    if check == 'v':
                        self.objectVertices.append(map(float, lines.split()[1:]))
                    if check == 'vt':
                        self.objectTextures.append(map(float, lines.split()[1:]))
                    if check == 'vn':
                        self.objectNormals.append(map(float, lines.split()[1:]))
                    if check == 'f':
                        first = lines.split()[1:]
                        for face in first:
                            self.objectFaces.append(map(float, face.split('/')))

            for face in self.objectFaces:
                # if no vt is available fill up with 0 at list position 1
                if len(face) == 2:
                    face.insert(1, 0.0)
                # if no vt and no vn is available fill up with 0 at list position 1 and 2
                if len(face) == 1:
                    face.insert(1, 0.0)
                    face.insert(2, 0.0)

            #print self.objectVertices, self.objectNormals, self.objectFaces
            return self.objectVertices, self.objectTextures, self.objectNormals, self.objectFaces

        except IOError as e:
            print "I/O error({0}): {1}".format(e.errno, e.strerror)
        except:
            print "Fehler beim Einlesen des obj Files"

    def createDataFromObj(self):
        ''' get the right data for the vbo'''

        for vertex in self.objectFaces:
            vn = int(vertex[0]) - 1
            tn = int(vertex[1]) - 1
            nn = int(vertex[2]) - 1
            if self.objectNormals:
                self.data.append(self.objectVertices[vn] + self.objectTextures[tn] + self.objectNormals[nn])
                #self.data.append(self.objectVertices[vn] + self.objectNormals[nn])
            else:
                # calc standard normals, if no objectNormals available
                '''normals = [x-y for x,y in zip(self.objectVertices[vn],center)]
                l = math.sqrt(normals[0]**2 +  normals[1]**2 + normals[2]**2)
                normals = [x/l for x in normals]'''
                temp = [0.0, 0.0, 0.0]
                self.data.append(self.objectVertices[vn] + temp)

        print "data", self.data[:5]
        self.my_vbo = vbo.VBO(array(self.data, 'f'))

        return self.data, self.my_vbo
