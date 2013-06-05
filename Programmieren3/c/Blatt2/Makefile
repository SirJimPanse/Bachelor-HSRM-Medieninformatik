CC = gcc -lm
CFLAGS = -ansi -g -Wall 
ALL = soundex roemisch rot13 rot18 rot1 zeichne
ROTLIB = rot13lib.o
ROT13 = rot13.c
ROT18 = rot18.o

all: $(ALL)

soundex: soundex.c
	$(CC) $(CFLAGS) soundex.c -o soundex

roemisch: roemisch.c
	$(CC) $(CFLAGS) roemisch.c -o roemisch

rot13: rot13.o $(ROTLIB)
	$(CC) $(CFLAGS) rot13.o rot13lib.o -o rot13 

$(ROTLIB): rot13lib.c rot13lib.h

rot13.o: $(ROT13)

$(ROT18): rot18.c
	$(CC) -c $(CFLAGS) rot18.c -o $(ROT18)

rot18: $(ROT18) $(ROTLIB)
	$(CC) $(CLFAGS) $(ROT18) $(ROTLIB) -o rot18

rot1.o: rot1.c


rot1: rot1.o $(ROTLIB)
	$(CC) $(CFLAGS) rot1.o $(ROTLIB) -o rot1

zeichne.o: zeichne.c

zeiche: zeichne.o
	$(CC) $(CFLAGS) zeichne.o -o zeichne

%.o : %.c
	$(CC) -c $(CFLAGS) $< -o $@

%.o : %.c %.h
	$(CC) -c $(CFLAGS) $< -o $@

test: ./rot1
	

clean:
	-/bin/rm -f soundex soundex.o
	-/bin/rm -f roemisch roemisch.o
	-/bin/rm -f rot13 rot13.o
	-/bin/rm -f rot18 rot18.o
