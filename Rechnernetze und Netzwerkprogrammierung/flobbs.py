#!/usr/bin/python
#! -*- coding: utf-8 -*-
import flpconfig, tempfile, os, flputil

class Job(object):
    REC = 'receive'
    INIT_ACK = 'initAck'
    REC_LENGTH = 'recLength'
    INIT = 'init'
    INIT_SEND = 'initSend'
    INIT_REC  = 'initRec'
    SEND = 'send'
    
    readstates  = [REC, INIT_ACK, REC_LENGTH, INIT_REC]
    writestates = [INIT, INIT_SEND, SEND]
    
    def __init__(self, socket, isTree=False):
        self.socket     = socket
        self.peer       = socket.getpeername()
        self.jobFile    = None
        self.finished   = False
        self.failed     = False
        self.state      = None
        self.isTree     = isTree

    def do(self):
        pass
    
    def getReadable(self):
        return not self.writeable
    
    def getWriteable(self):
        return self.state in Job.writestates

    readable  = property(getReadable,None,None,None)
    writeable = property(getWriteable,None,None,None)
    
    
class ResponseJob(Job):
    
    
    def __init__(self, socket):
        Job.__init__(self, socket)
        self.socket = socket
        self.state  = Job.REC
        self.files  = None
        self.actFile = None

    def do(self):
        """ je nach Status auf message warten, senden, schließen"""
        flpconfig.LOGGER.info(" do() - state: %s " %(self.state))
        if self.state == Job.REC:
            self._receive()
        elif self.state == Job.SEND:
            self._send()
        elif self.state == Job.INIT_REC:
            self._initRecv()
        elif self.state == Job.INIT_SEND:
            self._initSend()

    def _receive(self):
        ''' empfängt einen Dateinamen '''
        message = self.socket.recv(flpconfig.SIZE)
        flpconfig.LOGGER.info(" do() - received message: %s " %(message))
        bla = str((flpconfig.PROG+flpconfig.SEP)).encode('utf-8')

        if message.startswith(bla):
            message = message.partition(flpconfig.SEP)[2]
            
            # Beendet-Nachricht
            if message == flpconfig.FINMSG:
                self.finished = True
                flpconfig.LOGGER.info(" do() - finished sending file %s " %(self.jobFile))

            # Dateianfrage
            elif message.startswith(flpconfig.REQUESTMSG):
                self.jobFile = message.partition(flpconfig.SEP)[2]
                self.state = Job.INIT_SEND
                flpconfig.LOGGER.info(" do() - incoming request for file %s " %(self.jobFile))

    
    def _initSend(self):
        ''' Schickt Größe der Datei '''
        self.actFile = open(self.jobFile,"rb")
        size = os.path.getsize(self.jobFile)
        self.socket.send(str(size))
        if size == 0:
            self.state = Job.REC
            flpconfig.LOGGER.info(" do() - INIT_SEND - size == 0 ")
            return
        
        self.state = Job.INIT_REC
        flpconfig.LOGGER.info(" do() - INIT_SEND - sent size: %d " %(size))


    def _initRecv(self):
        ''' Empfängt ACK nach InitSend, schickt dann Größe der Datei '''
        self.socket.recv(flpconfig.SIZE)
        self.state = Job.SEND
        flpconfig.LOGGER.info(" do() - INIT_REC - received ack - start sending chunks..... ")

    def _send(self):
        ''' sends data-strings '''
        part = self.actFile.read(flpconfig.SIZE)
        if part:
            self.socket.send(part)
        else:
            flpconfig.LOGGER.info(self.jobFile + " gesendet")
            self.state = Job.REC


class RequestJob(Job):
    filejobs = []
    maxTries = 3
    
    def __init__(self, socket, jobFile, isTree=False,initial=False,setDirStamp=False):
        Job.__init__(self, socket, isTree)
        RequestJob.filejobs.append(jobFile)
        flpconfig.LOGGER.info("RequestJob.__init__ - filejobs appended: "+jobFile)
        self.jobFile     = jobFile
        self.state       = Job.INIT
        self.length      = 0
        self.received    = 0
        self.tempFile    = None
        self.timesTried  = 0
        self.initial     = initial
        self.setDirStamp = setDirStamp
        
    def do(self):
        ''' je nach Status wird job initialisiert, job(datei)-länge gelesen oder job(datei) empfangen'''
        flpconfig.LOGGER.info("do() - state "+self.state )
        #init - write
        bana = os.path.basename(self.jobFile)
        
        if self.state is Job.INIT:
            
            flpconfig.LOGGER.info(bana+" do() - INIT - sending file request for " + self.jobFile + " to " + str(self.peer))
            self.socket.send(flpconfig.SEP.join([flpconfig.PROG, flpconfig.REQUESTMSG, self.jobFile]))
            self.state = Job.REC_LENGTH
        
        
        #recvLength - read
        elif self.state is Job.REC_LENGTH:
            self.length = int(self.socket.recv(flpconfig.SIZE))
            if self.length == 0:
                self.instantSave()
                return
            flpconfig.LOGGER.info(bana+" do() - REC_LENGTH - "+str(self.length))
            self.tempFile = tempfile.TemporaryFile('w+b')
            self.socket.send("length empfangen")
            self.state = Job.REC
        
        #receive
        elif self.state is Job.REC:
            flpconfig.LOGGER.info(bana+" do() - REC rec=%d, len=%d; tmp=%s" % (self.received,self.length,self.tempFile) )
            
            if self.received <= self.length:
                self.tempFile.write(self.socket.recv(flpconfig.SIZE))
                self.received += flpconfig.SIZE
                flpconfig.LOGGER.info(bana+" do() - REC - tempFile.write, recv'ed = %d" % self.received)
                
                if self.received >= self.length:
                    self.received = self.tempFile.tell()
                    flpconfig.LOGGER.info(bana+" do() - REC - trySave()")
                    self._trySave()


    def instantSave(self):
        ''' liest jobFile aus, speichert diese, gibt "finished"-message zurück und löscht den Job aus Jobliste'''
        f = open(self.jobFile, 'wb')
        f.close()
        
        #kann nicht tree sein (size(tree) nie 0)
        peetree = flpconfig.PEERGROUP[self.peer[0]][0]
        flputil.setTreeMTime(peetree, self.jobFile, self.setDirStamp)
        
        self.socket.send(flpconfig.SEP.join([flpconfig.PROG,flpconfig.FINMSG]))
        self.finished = True
        RequestJob.filejobs.remove(self.jobFile)

    def _trySave(self):
        ''' versucht daten auf das dateisystem zu schreiben '''
        if self.timesTried < RequestJob.maxTries:
                
            if self.received == self.length:
                
                self.tempFile.seek(0)
                f = open(self.jobFile, 'wb')
                
                while True:
                    data =  self.tempFile.read(flpconfig.SIZE)
                    if not data:
                        break
                    f.write(data)
                
                f.close()
                self.tempFile.close()
                
                #set own timestamp
                if not self.isTree:
                    peetree = flpconfig.PEERGROUP[self.peer[0]][0]
                    flputil.setTreeMTime(peetree, self.jobFile, self.setDirStamp)     
                
                self.socket.send(flpconfig.SEP.join([flpconfig.PROG,flpconfig.FINMSG]))
                self.finished = True
                flpconfig.LOGGER.info("completed file request for " + self.jobFile + " to " + str(self.peer))                   
            
            else:
            
                self.socket.send(flpconfig.FINMSG)
                self.timesTried += 1
                self.state = Job.INIT
                self.do()
                flpconfig.LOGGER.info('Dateiübertragung fehlgeschlagen, neuer Request')

        else:
            # TODO: nach Fehlschlag aufräumen?
            flpconfig.LOGGER.info('Dateiübertragung endgültig fehlgeschlagen')
            self.failed = True

        RequestJob.filejobs.remove(self.jobFile)