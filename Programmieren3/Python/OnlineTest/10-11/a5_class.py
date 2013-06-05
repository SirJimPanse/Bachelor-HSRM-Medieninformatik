instanzen = 0
class Klasse(object):
    #instanzen = 0
    def __init__(self,zahl1 = None):
        global instanzen
        if zahl1 != None:
            self.zahl = zahl1
        else:
            self.zahl = None
        #besser: einfach self.zahl = zahl1
        #besser: Klasse.instanzen += 1
        instanzen += 1

    @staticmethod
    def wieviel():
        return instanzen
        #return Klasse.instanzen

    def add(self,para):
        if self.zahl != None:
            return para + self.zahl
        return para*2

class Erbe(Klasse):

    def __init__(self, zahl1 = None, zahl2 = None):
        Klasse.__init__(self, zahl1)
        # self.zahl2 = zahl2
        if zahl1 != None:
            self.zahl1 = zahl1
        else:
            self.zahl1 = None

        if zahl2 != None:
            self.zahl2 = zahl2
        else:
            self.zahl2 = None
    
    def mul(self,para2):
        if self.zahl2 != None:
            return para2 * self.zahl2
        return para2 * 3

    def __mul__(self, para2):
        return self.mul(para2)

    __rmul__ = __mul__
