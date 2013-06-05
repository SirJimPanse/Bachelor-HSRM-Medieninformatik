'''
Created on 31.05.2013

@author: Justin Albert, Tino Landmann, Soeren Kroell
'''

from numpy import sqrt, dot, array, matrix, cross, cos, sin, tan
from OpenGL.GL import *


def identity():
    return matrix([[1, 0, 0, 0],
                   [0, 1, 0, 0],
                   [0, 0, 1, 0],
                   [0, 0, 0, 1]])


def lookAtMatrix(ex, ey, ez, cx, cy, cz, ux, uy, uz):
    e = array([ex, ey, ez])  # eye position
    c = array([cx, cy, cz])  # center
    up = array([ux, uy, uz])  # up vector

    # normalize UpVector
    lup = sqrt(dot(up, up))
    up = up / lup
    # get the view direction
    f = c - e
    lf = sqrt(dot(f, f))
    f = f / lf
    # calculate s
    s = cross(f, up)
    ls = sqrt(dot(s, s))
    s = s / ls
    # calculate u
    u = cross(s, f)
    # create LookAtMatrix
    l = matrix([
                [s[0], s[1], s[2], -dot(s, e)],
                [u[0], u[1], u[2], -dot(u, e)],
                [-f[0], -f[1], -f[2], dot(f, e)],
                [0, 0, 0, 1]])
    return l


def rotationMatrix(angle, axis):
    c, mc = cos(angle), 1 - cos(angle)
    s = sin(angle)
    l = sqrt(dot(array(axis), array(axis)))
    x, y, z = array(axis) / l
    r = matrix([
                [x * x * mc + c, x * y * mc - z * s, x * z * mc + y * s, 0],
                [x * y * mc + z * s, y * y * mc + c, y * z * mc - x * s, 0],
                [x * z * mc - y * s, y * z * mc + x * s, z * z * mc + c, 0],
                [0, 0, 0, 1]])
    return r


def scaleMatrix(sx, sy, sz):
    s = matrix([[sx, 0, 0, 0],
                [0, sy, 0, 0],
                [0, 0, sx, 0],
                [0, 0, 0, 1]])
    return s


def translationMatrix(tx, ty, tz):
    t = matrix([[1, 0, 0, tx],
                [0, 1, 0, ty],
                [0, 0, 1, tz],
                [0, 0, 0, 1]])
    return t


def perspectiveMatrix(fovy, aspect, zNear, zFar):
    f = 1.0 / tan(fovy / 2.0)
    aspect = float(aspect)
    zNear = float(zNear)
    zFar = float(zFar)

    p = matrix([
        [f / aspect, 0, 0, 0],
        [0, f, 0, 0],
        [0, 0, (zFar + zNear) / (zNear - zFar), (2 * zFar * zNear) / (zNear - zFar)],
        [0, 0, -1, 0]
        ])
    return p


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


def projectOnSphere(x, y, r, width, height):
    x, y = x - width / 2.0, height / 2.0 - y
    a = min(r * r, x ** 2 + y ** 2)
    z = sqrt(r * r - a)
    l = sqrt(x ** 2 + y ** 2 + z ** 2)
    return x / l, y / l, z / l


def sendValue(shaderProgram, varName, value):
    varLocation = glGetUniformLocation(shaderProgram, varName)
    glUniform1f(varLocation, value)


def sendVec3(shaderProgram, varName, value):
    varLocation = glGetUniformLocation(shaderProgram, varName)
    glUniform3f(varLocation, *value)


def sendVec4(shaderProgram, varName, value):
    varLocation = glGetUniformLocation(shaderProgram, varName)
    glUniform4f(varLocation, *value)


def sendMat3(shaderProgram, varName, matrix):
    varLocation = glGetUniformLocation(shaderProgram, varName)
    glUniformMatrix3fv(varLocation, 1, GL_TRUE, matrix.tolist())


def sendMat4(shaderProgram, varName, matrix):
        varLocation = glGetUniformLocation(shaderProgram, varName)
        glUniformMatrix4fv(varLocation, 1, GL_TRUE, matrix.tolist())

if __name__ == '__main__':

    print lookAtMatrix(0, 0, 5, 0, 0, 0, 0, 1, 0)
