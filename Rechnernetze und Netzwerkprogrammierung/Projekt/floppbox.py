#!/usr/bin/python
#! -*- coding: utf-8 -*-
# hilfreich:
# http://docs.python.org/library/socket.html
# http://ilab.cs.byu.edu/python/select/echoserver.html
# http://docs.python.org/release/3.1.5/howto/sockets.html
import sys, socket, os, logging, select, time, pickle, flpconfig, flputil, flobbs
from Monitor_hash import getHashDir


def send_ident(actTime):
    '''update hash and broadcast info msg regularly'''

    msg = flpconfig.SEP.join([flpconfig.PROG,flpconfig.INFOMSG,flpconfig.FLPGROUP,str(flpconfig.TIMESTAMP),str(flpconfig.SUPERHASH)])    

    if time.time() - actTime > flpconfig.BC_TIME:
        actTime = time.time()
        ourHash = getHashDir(flpconfig.FLPGROUP)
        if ourHash != flpconfig.SUPERHASH:
            flpconfig.LOGGER.info('detected change in files, updating hash and timestamp..')
            flpconfig.DIRTREE = flputil.get_dict()
            flpconfig.TIMESTAMP = time.time()
            flpconfig.SUPERHASH = ourHash
        flpconfig.LOGGER.info('sending udp info broadcast: ' + msg)
        flpconfig.UDPSOCK.sendto(msg,("<broadcast>",flpconfig.UDPPORT))

    return actTime


def handle_diff(ip,initial=False):
    ownTree = flpconfig.DIRTREE
    peerTree   = flpconfig.PEERGROUP[ip][0]
    diffdict   = flputil.get_diff(ownTree,peerTree)
    addedDirs  = diffdict['addedDirs']
    addedDirsSorted = addedDirs.keys()
    addedDirsSorted.sort()

    addedFiles = diffdict['added']
    modFiles   = diffdict['modified']
    remFiles   = diffdict['removed'] 
    
    flpconfig.LOGGER.info('diffdict: ' + str(diffdict))
    
#    if initial:
#        addedFiles += flputil.calc_files_removed(remFiles,ip)
#        addedDirs  += flputil.calc_dirs_removed(remFiles,ip)
#        remFiles = []
    
    for adkey in addedDirsSorted:
        flputil.makeDir(addedDirs[adkey],ip)

    for adkey in addedFiles:
        flputil.makeReq(adkey,ip,setDirStamp=True)

    for adkey in modFiles:
        flputil.makeReq(adkey,ip)
        
    for adkey in remFiles:
        # falls das implementiert wird müssen destruktive Eingaben
        # abgefangen werden / * ~
        
        # und bei 2 inits gleichzeitig darf nicht gelöscht werden, sondern
        # beide bekommen beide dateien (sonst geht ein Teil ganz verloren)
        pass
    

def manage_jobs():
    ''' Fertige Jobs löschen, falls ein Baumrequest dabei war, diesen weiter verfolgen (diff machen) '''
    jobs = flpconfig.JOBS
    
    for jobkey in jobs.keys():
        
        job = jobs[jobkey]
        
        if job.finished or job.failed:
            
            del jobs[jobkey]
            
            if type(job) is flobbs.RequestJob and job.isTree:
                
                peerTree = pickle.load(open(job.jobFile))
                flpconfig.PEERGROUP[job.peer[0]] = (peerTree,flpconfig.PEERGROUP[job.peer[0]][1])
                handle_diff(job.peer[0],initial=job.initial)
                
            flpconfig.LISTEN.remove(job.socket)
            flpconfig.SEND.remove(job.socket)
            job.socket.close()


def handle_tcp(peer,address):
    '''handles incoming TCP connections'''
    flpconfig.LOGGER.info('incoming connection: ' + repr(peer) + ' ' + repr(address))
    flpconfig.JOBS[peer] = flobbs.ResponseJob(peer)
    flpconfig.LISTEN    += [peer]
    flpconfig.SEND      += [peer]
    pickle.dump(flpconfig.DIRTREE,open(flpconfig.MYTREEFILE,'wb'))


def handle_udp_broadcast(data,ip):
    ''' handles incoming UDP broadcasts from other peers '''
    
    flpconfig.LOGGER.info('incoming broadcast: ' + repr(data) + ' from ' + repr(ip))
    prog, msg, group, their_time, their_hash = data.split(flpconfig.SEP)
    
    if prog == flpconfig.PROG and group == flpconfig.FLPGROUP:

        if their_hash != flpconfig.SUPERHASH:
        
            if msg == flpconfig.INFOMSG or msg == flpconfig.INITMSG:

                if ip not in flpconfig.PEERGROUP:
    
                    flpconfig.LOGGER.info('unknown Peer:' + their_time + ' ' + their_hash)
                    flpconfig.PEERGROUP[ip] = None
    
                else: #build a requestJob for the directory tree of peer, if there isnt one yet
    
                    flpconfig.LOGGER.info('updated Peer:' + their_time + ' ' + their_hash)
                    if msg == flpconfig.INITMSG:
                        flpconfig.PEERGROUP[ip] = (None, their_hash)
                        flputil.makeReq(flpconfig.TREEFILE + ip, ip,treeJob=True,initial=True)
                    if flpconfig.PEERGROUP[ip] == None:
                        flpconfig.PEERGROUP[ip] = (None, their_hash)
                        flputil.makeReq(flpconfig.TREEFILE + ip,ip,treeJob=True)
                    elif their_hash != flpconfig.PEERGROUP[ip][1]:
                        flpconfig.PEERGROUP[ip] = (flpconfig.PEERGROUP[ip][0], their_hash)
                        flputil.makeReq(flpconfig.TREEFILE + ip,ip,treeJob=True)


def flpbox(flpGroup):

    #general
    flpconfig.RUNNING = 1
    flpconfig.FLPGROUP = flpGroup
    try:
        flpconfig.PEERGROUP = pickle.load(open(flpconfig.PEERFILE, "rb"))
    except IOError:
        flpconfig.PEERGROUP = {}
        flpconfig.INITIAL   = True
    flpconfig.JOBS  = {}
    flpconfig.LANIP = flputil.get_LAN_IP()
    flpconfig.MYTREEFILE = flpconfig.TREEFILE + flpconfig.LANIP

    #start logging
    logging.basicConfig(filename=flpconfig.LOGFILE, level=logging.INFO)
    #logging.basicConfig(level=logging.INFO)
    flpconfig.LOGGER = logging.getLogger(__name__)
    flpconfig.LOGGER.info('started')

    #create broadcast and unicast sockets
    flpconfig.UDPSOCK = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    flpconfig.UDPSOCK.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, True)
    flpconfig.UDPSOCK.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR,True)
    flpconfig.TCPSOCK = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    flpconfig.TCPSOCK.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, True)

    #bind both sockets then broadcast
    flpconfig.UDPSOCK.bind((flpconfig.INTERFC,flpconfig.UDPPORT))
    flpconfig.TCPSOCK.bind((flpconfig.INTERFC,flpconfig.TCPPORT))
    flpconfig.TCPSOCK.listen(flpconfig.BACKLOG)

    # broadcast first info msg
    flpconfig.TIMESTAMP = actTime = time.time()
    flpconfig.SUPERHASH = getHashDir(flpconfig.FLPGROUP)
    flpconfig.DIRTREE = flputil.get_dict()
    pickle.dump(flpconfig.DIRTREE,open(flpconfig.MYTREEFILE,'wb'))
    flpconfig.LOGGER.info('initial hash: ' + str(flpconfig.SUPERHASH))
    flpconfig.LISTEN = [flpconfig.UDPSOCK, flpconfig.TCPSOCK]
    flpconfig.SEND   = []
    flpconfig.LOGGER.info('entering Main Loop')

    while flpconfig.RUNNING:

        # call select and broadcast info
        manage_jobs()
        readable, writeable, _ = select.select(flpconfig.LISTEN,flpconfig.SEND,[],flpconfig.BC_TIME)
        actTime = send_ident(actTime)

        for s in readable:

            # did we receive a broadcast msg?
            if s == flpconfig.UDPSOCK:
                data, address = s.recvfrom(flpconfig.SIZE)
                # ignore own broadcast messages
                if address[0] != flpconfig.LANIP:
                    handle_udp_broadcast(data,address[0])

            # did someone connect to our peer?
            elif s == flpconfig.TCPSOCK:
                peer, address = s.accept()
                handle_tcp(peer,address[0])

            # is it a active job?
            elif s in flpconfig.JOBS:
                job = flpconfig.JOBS[s]
                if job.readable and not job.finished:
                    job.do()
        
        
        for s in writeable:
            # is a job in a writeable state?
            
            if s in flpconfig.JOBS:
                job = flpconfig.JOBS[s]
                if job.writeable and not job.finished:
                    job.do()

    # clean up after select loop terminates
    flpconfig.UDPSOCK.close()
    flpconfig.TCPSOCK.close()
    logging.info('finished')


if __name__ == '__main__':

    if len(sys.argv) != 2:
        print 'Benutzung: floppbox.py <Gruppe>'
        exit(1)

    if os.path.exists(sys.argv[1]) and os.path.isdir(sys.argv[1]):
        flpbox(sys.argv[1])
    else:
        print 'Den Ordner für die Gruppe "' + sys.argv[1] + '" kenne ich nicht.'
        exit(2)
