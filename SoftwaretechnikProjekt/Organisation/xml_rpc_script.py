#/usr/bin/python
# -*- coding:utf-8 -*-
from xmlrpclib import ServerProxy
from time import mktime
from datetime import datetime

import codecs
import xmlrpclib
import getpass, sys

server_url = 'scm.mi.hs-rm.de/trac/2012swtpro/2012swtpro03/login/rpc'

def login():
    message('Bitte einloggen')
    username = raw_input('# Benutzername:')
    password = getpass.getpass('# Passwort:')
    p = ServerProxy('https://%s:%s@%s'%(username,password,server_url))
    try:
        p.system.getAPIVersion()
    except xmlrpclib.ProtocolError:
        choice = ''
        while choice != 'y' and choice != 'n':
            choice = raw_input('# Eingabe falsch, wiederholen?[y/n]')
        if choice == 'y':
            p = login()
        else:
            sys.exit(0)
    return p

def queryTickets(proxy):
    message('Tickets werden geladen')
    output = raw_input('# Bitte Ausgabedatei angeben:')
    outfile = codecs.open(output, encoding='utf-8', mode='w')
    tickets = proxy.ticket.query('milestone=Sprint 2')
    for ticketid in tickets:
        ticket = proxy.ticket.get(ticketid)
        attrs = ticket[3]
        stringTime = timeToString(attrs['time'])
        closeTime = timeToString(attrs['changetime']) if attrs['status'] == 'closed' or attrs['status'] == 'ready_for_approval' else ''
        params = (ticket[0],attrs['summary'],attrs['status'],stringTime,closeTime)
        outfile.write("%s;%s;%s;%s;%s\n"%params)
    outfile.close()
    message('Tickets wurden in Datei geschrieben')

def timeToString(ticketTime):
    dt = datetime.fromtimestamp(mktime(ticketTime.timetuple()))
    return dt.strftime("%d.%m.%Y")

def message(msg):
    print '='*80
    print '# ' + msg
    print '='*80
    
if __name__ == '__main__':
    proxy = login()
    queryTickets(proxy)
