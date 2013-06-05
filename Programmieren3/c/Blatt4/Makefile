CC = gcc
CFLAGS = -ansi -g -Wall

all: abzaehl llistlib delall

abzaehl.o: abzaehl.c
	$(CC) -c $(CFLAGS) abzaehl.c -o abzaehl.o

abzaehl: abzaehl.o
	$(CC) $(CFLAGS) abzaehl.o -o abzaehl

llistlib.o: llistlib.c
	$(CC) -c $(CFLAGS) llistlib.c -o llistlib.o

llistlib: llistlib.o
	$(CC) $(CFLAGS) llistlib.o -o llistlib

delall.o: delall.c
	$(CC) -c $(CFLAGS) delall.c -o delall.o

delall: delall.o
	$(CC) $(CFLAGS) delall.o -o delall