#!/usr/bin/python
# -*- coding: utf-8 -*-
import hashlib, os, flpconfig, flputil

timestamps = {}

def getHashFile(abs_filepath,fct=hashlib.sha1,size=flpconfig.SIZE):
    f = open(abs_filepath)
    hashFunction = fct()
    while 1:
        buf = f.read(size)
        if not buf:
            break
        hashFunction.update(buf)
    f.close()
    return hashFunction.hexdigest()





def getHashDir(abs_directory):
    ''' computes hash for directory recursively (if anything changed at 
        any level the hash will be different)
        does not hash hidden files/dirs '''
    if not os.path.exists(abs_directory):
        return -1

    hashFunction = hashlib.sha1()

    for root, _, files in os.walk(abs_directory):
        for names in files:
            filepath = os.path.join(root, names)
            
            if flputil.containsHiddenFile(filepath):
                continue
            
            hashFunction.update(hashlib.sha1(filepath).hexdigest())
            try:
                f1 = open(filepath, 'rb')
                while 1:
                    buf = f1.read(flpconfig.SIZE)
                    if not buf:
                        break
                    hashFunction.update(hashlib.sha1(buf).hexdigest())
                f1.close()
            except:
                f1.close()
                continue
            
    return hashFunction.hexdigest()


def createActTimestamps(abs_directory):
    ''' Trägt die Zeitstempel aller Ordner und Dateien in timestamps ein.
        Benötigt den absoluten Pfad zum Gruppenordner als Parameter '''
    if not os.path.exists(abs_directory):
        return -1
    # nur falls abs_directory gruppenordner und nicht floppboxordner!!
    timestamps[abs_directory] = os.path.getmtime(abs_directory)
    
    for root, dirs, files in os.walk(abs_directory):
        for dirName in dirs:
            path = os.path.join(root, dirName)
            timestamps[path] = os.path.getmtime(path)
        for fileName in files:
            path = os.path.join(root, fileName)
            timestamps[path] = os.path.getmtime(path)


def getSuperTimestamp():
    return max(timestamps.values())
