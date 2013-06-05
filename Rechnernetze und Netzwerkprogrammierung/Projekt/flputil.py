#!/usr/bin/python
#! -*- coding: utf-8 -*-
import socket, os, Monitor_hash, flpconfig, flobbs, pickle

def containsHiddenFile(filepath):
    return len(filepath.split('/.')) > 1

def dumpPeers():
    pickle.dump(flpconfig.PEERGROUP, open(flpconfig.PEERFILE, 'rb'))
    
def calc_files_removed(removed,ip):

    peerTree = flpconfig.PEERGROUP[ip][0]
    result = []
    for path in removed:
        
        breadcrumbs = path.split(os.path.sep)
        modFil = breadcrumbs[-1]
        breadcrumbs = breadcrumbs[:-1]
        act = peerTree
        
        for breadcrumb in breadcrumbs:
            act = act[breadcrumb]
        
        if modFil in act:
            result += calc_files_removed([act[modFil][flpconfig.PATH_STR]],ip)
        else:
            result.append(path)
            
    return result

def calc_dirs_removed(removed,ip):

    peerTree = flpconfig.PEERGROUP[ip][0]
    result = []
    for path in removed:
        
        breadcrumbs = path.split(os.path.sep)
        modFil = breadcrumbs[-1]
        breadcrumbs = breadcrumbs[:-1]
        act = peerTree
        
        for breadcrumb in breadcrumbs:
            act = act[breadcrumb]
        
        if modFil in act:
            result += calc_dirs_removed([act[modFil][flpconfig.PATH_STR]],ip)
            result += [path]
            
    return result

def setTreeMTime(treedict, actPath, setParent=False, isDir=False):
    ''' Setzt mtime zum parent directory von actPath auf den in treedict angegebenen timestamp falls parent=True, sonst ist den  von actPath '''
    breadcrumbs = actPath.split(os.path.sep)
    modFil = breadcrumbs[-1]
    breadcrumbs = breadcrumbs[:-1]
    
    #act soll dann das dict des parent sein
    act = treedict
    for breadcrumb in breadcrumbs:
        act = act[breadcrumb]
    
    #setmtime vom parent
    if setParent:
        parent_ts = act[flpconfig.TIMESTAMP_STR]
        setmtime(act[flpconfig.PATH_STR], parent_ts)
    
    #setmtime der Datei
    if isDir:
        modFil_ts = act[modFil][flpconfig.TIMESTAMP_STR]
        setmtime(act[modFil][flpconfig.PATH_STR], modFil_ts)
    else:
        modFil_ts = act[flpconfig.DIR_STR][modFil][flpconfig.TIMESTAMP_STR]
        setmtime(act[flpconfig.DIR_STR][modFil][flpconfig.PATH_STR], modFil_ts)

def makeReq(fname,ip,treeJob=False,initial=False,setDirStamp=False):
    ''' create a requestJob and add it everywhere necessary, if not already '''
    build = True
    
    for jobfile in flobbs.RequestJob.filejobs:
        if jobfile == fname:
            build = False
            flpconfig.LOGGER.info("makeReq - %s already in filejobs: " % (fname))
            break

    if build:

        job = flobbs.RequestJob(socket.create_connection((ip,flpconfig.TCPPORT)), fname, treeJob, initial, setDirStamp)
        flpconfig.JOBS[job.socket] = job
        flpconfig.LISTEN += [job.socket]
        flpconfig.SEND   += [job.socket]


def makeDir(v_Frechel,ip=None):
    ''' create a directory and set the mtime (also set mtime of parent directory accordingly) '''
    dirPath = v_Frechel[flpconfig.PATH_STR]
    os.mkdir(dirPath)
    setTreeMTime(flpconfig.PEERGROUP[ip][0], dirPath,setParent=True, isDir=True)
    
def setmtime(path,mtime):
    os.utime(path,(os.path.getatime(path),mtime))

def isOld(oldTimestamp, newTimestamp):
    ''' True if oldTimestamp is older than newTimestamp, False otherwise '''
    return compare_timestamps(oldTimestamp, newTimestamp) < 0

def _isFile(k, parent):
    ''' True if k is a file in the directory dictionary '''
    return parent != None and flpconfig.DIR_STR in parent.keys() and parent[flpconfig.DIR_STR] == k

def _diff(l1, l2):
    """ [l1\l2] """
    return [ele for ele in l1 if ele not in set(l2)]

def _getDirEntries(dir_entry):
    """gibt ein tuple aus dictionaries zurück, die alle unterordner und files
    des aktuellen Ordners unter deren Path abgelegt enthalten. """
    dirs = {}
    files = {}

    # sich selbst (Ordner) hinzufügen
    dirs[dir_entry[flpconfig.PATH_STR]] = dir_entry

    # alle enthaltenen files hinzufügen
    filecontainer = dir_entry[flpconfig.DIR_STR]
    for filename in filecontainer:
        files[filecontainer[filename][flpconfig.PATH_STR]] = filecontainer[filename]

    # alle enthaltenen Unterordner und deren Files hinzufügen
    dels = [flpconfig.TIMESTAMP_STR, flpconfig.HASH_STR, flpconfig.DIR_STR, flpconfig.PATH_STR]
    entries = _diff(dir_entry.keys(), dels)
    for entry in entries:
        subdirs, subfiles = _getDirEntries(entry)
        for subdir in subdirs:
            dirs[subdir] = subdirs[subdir]
        for subfile in subfiles:
            files[subfile] = subfiles[subfile]

    return dirs, files


def get_diff(mine, theirs, myParent=None, theirParent=None, oldK=None, ctx=None):
    """Gibt ein dict zurück, das unter 'added'.keys() alle Dateipfade
    enthält, die im eigenen Gruppenordner hinzugefügt werden müssen,
    unter 'addedDirs'.keys() die Pfade zu Ordnern, die angelegt werden müssen,
    unter 'removed'.keys() die Pfade zu Ordnern und Files, die gelöscht
    werden sollen und
    unter 'modified'.keys() die Pfade zu allen Dateien, die geändert werden müssen."""

    res = ctx if ctx != None else {'added': {}, 'modified': {}, 'removed': {}, 'addedDirs': {}}
    for k in mine:
        # possibly removed
        if k not in theirs:
            if _isFile(mine, myParent):
                # k ist Datei
                if isOld(mine[k][flpconfig.TIMESTAMP_STR], theirParent[flpconfig.TIMESTAMP_STR]):
                    # eigene Datei älter als Ordner des anderen
                    res['removed'][mine[k][flpconfig.PATH_STR]] = mine[k]
            else:
                # k ist Ordner
                if isOld(mine[k][flpconfig.TIMESTAMP_STR], theirParent[oldK][flpconfig.TIMESTAMP_STR]):
                    res['removed'][mine[k][flpconfig.PATH_STR]] = mine[k]

    for k in theirs:
        # possibly added
        if k not in mine:
            if _isFile(theirs, theirParent):
                if not isOld(theirs[k][flpconfig.TIMESTAMP_STR], myParent[flpconfig.TIMESTAMP_STR]):
                    # Datei des anderen neuer als Timestamp des eigenen Parentordners
                    res['added'][theirs[k][flpconfig.PATH_STR]] = theirs[k]
            
            else:
                if not isOld(theirs[k][flpconfig.TIMESTAMP_STR], myParent[oldK][flpconfig.TIMESTAMP_STR]):
                    # Alle Dateien aus dem aktuellen Ordner und seinen Unterordnern
                    #in 'addedÄ merken, den Ordner und Unterordner in 'addedDirs'
                    dirs, files = _getDirEntries(theirs[k])
                    for actDir in dirs:
                        res['addedDirs'][actDir] = dirs[actDir]
                    for actFile in files:
                        res['added'][actFile] = files[actFile]

        # modified
        elif mine[k] != theirs[k]:
            if type(theirs[k]) == dict:
                if _isFile(theirs, theirParent):
                    if isOld(mine[k][flpconfig.TIMESTAMP_STR], theirs[k][flpconfig.TIMESTAMP_STR]):
                        res['modified'][theirs[k][flpconfig.PATH_STR]] = theirs[k]
                else:
                    get_diff(mine[k], theirs[k], mine, theirs, k, res) 
                
    return res


def get_dict():
    '''returns a dictionary as described in
       https://scm.mi.hs-rm.de/trac/2012netze/2012netze07/wiki/WikiStart#Verzeichnisbaum
       recursively'''
    path = flpconfig.FLPGROUP
    result = {}
    path = path.rstrip(os.sep)
    start = path.rfind(os.sep) + 1
    for path, _, files in os.walk(path):
        # keine unsichtbaren Ordner holen
        if containsHiddenFile(path):
            continue
        folders = path[start:].split(os.sep)
        myPath = flpconfig.FLPGROUP+path.partition(flpconfig.FLPGROUP)[2]
        subdir = {
                    flpconfig.PATH_STR : myPath,
                    flpconfig.HASH_STR : Monitor_hash.getHashDir(path),
                    flpconfig.TIMESTAMP_STR : os.path.getmtime(path),                                  
                    flpconfig.DIR_STR : {fil : {flpconfig.HASH_STR : Monitor_hash.getHashFile(path + os.sep + fil), \
                                                flpconfig.TIMESTAMP_STR : os.path.getmtime(path + os.sep + fil), \
                                                flpconfig.PATH_STR : os.path.join(myPath, fil)} \
                                         for fil in files if not fil.startswith('.')}
                 }
        parent = reduce(dict.get, folders[:-1], result)
        parent[folders[-1]] = subdir
    return result


def get_LAN_IP():
    '''returns own ip within local area network (no connection is established) '''
    s = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
    s.connect(('hs-rm.de',0))
    return s.getsockname()[0]


def compare_timestamps(ts1, ts2):
    ''' compare to floating point time stamp variables '''
    return 0 if eq_timestamps(ts1,ts2) else -1 if ts1 < ts2 else 1


def eq_timestamps(ts1, ts2):
    ''' float equality (6 decimal places) '''
    return abs(ts1-ts2)<1e-6


def pretty(d, indent=0):
    ''' print a directory dictionary in a pretty way '''
    print '\t'*indent + '{ '
    for key, value in d.iteritems():
        print '\t' * (indent+1) + str(key)
        if isinstance(value, dict):
            pretty(value, indent + 2)
        else:
            print '\t' * (indent + 2) + str(value)
    print '\t'*indent + '}'


''' Tests '''
if __name__ == '__main__':

    mine = {'test': {'FILES': {'comment': {'PATH': '/test/comment', 'HASH': 'da39a3ee5e6b4b0d3255bfef95601890afd80709', 'TIME': 1340389009.0}, \
                            'doc': {'PATH': '/test/doc', 'HASH': '81546abf157e7a5787fb4652df1e41767512dcac', 'TIME': 1340389015.0}, \
                            'doc~': {'PATH': '/test/doc~', 'HASH': 'da39a3ee5e6b4b0d3255bfef95601890afd80709', 'TIME': 1340388987.0}}, \
                  'PATH': '/test', 'HASH': 'b01703e36e27104fd52b28c24890f95bab3c3b40', \
                  'subtest': {'FILES': {'subdoc': {'PATH': '/test/subtest/subdoc', 'HASH': 'b3f7968ca1a26c0752865e33dfd69c4356500ab0', 'TIME': 1340389036.0}, \
                                        'subdoc~': {'PATH': '/test/subtest/subdoc~', 'HASH': 'da39a3ee5e6b4b0d3255bfef95601890afd80709', 'TIME': 1340389021.0}}, \
                              'PATH': '/test/subtest', 'HASH': 'b3f7968ca1a26c0752865e33dfd69c4356500ab0', 'TIME': 1340389020.0}, \
                  'TIME': 1340389014.0} \
           }

    theirs = {'test': {'FILES': {'comment': {'PATH': '/test/comment', 'HASH': 'da39a3ee5e6b4b0d3255bfef95601890afd8070a', 'TIME': 1340389019.0}, \
                            'doc': {'PATH': '/test/doc', 'HASH': '81546abf157e7a5787fb4652df1e41767512dcaa', 'TIME': 1340389025.0}, \
                            'doc~': {'PATH': '/test/doc~', 'HASH': 'da39a3ee5e6b4b0d3255bfef95601890afd80709', 'TIME': 1340388987.0}}, \
                  'PATH': '/test', 'HASH': 'b01703e36e27104fd52b28c24890f95bab3c3b40', \
                  'subtest': {'FILES': {'subdoc': {'PATH': '/test/subtest/subdoc', 'HASH': 'b3f7968ca1a26c0752865e33dfd69c4356500ab0', 'TIME': 1340389036.0}, \
                                        }, 
                              'PATH': '/test/subtest', 'HASH': 'b3f7968ca1a26c0752865e33dfd69c4356500ab0', 'TIME': 1340389036.0}, \
                  'TIME': 1340389014.0} \
           }
    #pretty(old)
    #pretty(new)
    pretty(get_diff(mine, theirs))
