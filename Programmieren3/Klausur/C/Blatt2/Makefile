CC = gcc
CFLAGS = -ansi -g -Wall
SYMLINK = ln -s

all: soundex komprimier
	-/bin/rm -f dekomprimier
	$(SYMLINK) komprimier dekomprimier

soundex: soundex.c
	$(CC) $(CFLAGS) soundex.c -o soundex

komprimier.o: komprimier.c
	$(CC) $(CFLAGS) komprimier.c -o komprimier.o

komprimier: komprimier.o
	$(CC) $(CFLAGS) komprimier.o -o komprimier