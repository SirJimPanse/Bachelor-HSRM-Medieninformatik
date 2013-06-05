FLPGROUP   = None
SUPERHASH  = None
TIMESTAMP  = 0
DIRTREE    = None #dict (file/dir -> dict)
MYTREEFILE = None #string
JOBS       = None #dict (socket -> job)
INITIAL    = False

# string constants
LOGFILE    = '.flplog'
TREEFILE   = '.flptree.'
PEERFILE   = '.flppeers'
PROG       = "flp"
SEP        = ":"

# networking
LANIP      = None  #string
UDPPORT    = 21331
TCPPORT    = 11337
INTERFC    = ''
SIZE       = 1024
BACKLOG    = 5
UDPSOCK    = None #socket
TCPSOCK    = None #socket
RUNNING    = 0
PEERGROUP  = None #dict (ip -> baum) 
LISTEN     = None #list (sockets)
SEND       = None #list (sockets)
LOGGER     = None
BC_TIME    = 5.00 # seconds

# messages (group will be added in when main is started)
INFOMSG    = "info"
INITMSG    = "init"
REQUESTMSG = "request"
FINMSG     = "finished"
#evtl splitten in erfolgs- und fail message

# dictionary strings
TIMESTAMP_STR = 'TIME'
HASH_STR      = 'HASH'
DIR_STR       = 'FILES'
PATH_STR      = 'PATH'