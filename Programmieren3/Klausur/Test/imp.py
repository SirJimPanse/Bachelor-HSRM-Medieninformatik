
"""
#erstens
import paket
paket.set1()
print (paket.w, paket.v[0])
paket.set2()
print(paket.w,paket.v[0])
w = 6
v =[1]
v[0] = 7
print(paket.w, paket.v[0])
"""
#zweitens
from paket import *
set1()
print(w,v[0])
set2()
print(w,v[0])
w = 6
v = [1]
v[0] = 7
print(w,v[0])

