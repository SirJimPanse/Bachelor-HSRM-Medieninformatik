from threading import Thread, Lock
import random, timeit
"""Threading A2"""
x = 0
sperre = Lock()
class IntState(Thread):
    def __init__(self):
        Thread.__init__(self)

    def run(self):
        for i in range((10**6)+1):
            rand = random.randint(-1000,1000)
            global x
            x += rand
            x -=rand

class IntStateSafe(Thread):
    def __init__(self):
        Thread.__init__(self)

    def run(self):
        for i in range((10**6)+1):
            rand = random.randint(-1000,1000)
            global x
            sperre.acquire()
            x += rand
            x -= rand
            sperre.release()


if __name__ == '__main__':
    threads = []
    for i in range(5):
        thread = IntState()
        threads.append(thread)
        thread.start()
        
    for t in threads:
        t.join()
        

    if x == 0: print "Alles suuuuper, x: ",x
    else: print "Seuche! x: ",x
    #print timer1.timeit()
"""Threading A3"""


