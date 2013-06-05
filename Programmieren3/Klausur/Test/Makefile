CC =gcc
CFLAGS =-ansi -g -Wall
#SRC = auseinander.c a.c 
all: strcat zeigersalat auseinander
#define make.depend

strcat: strcat.c
	$(CC) $(CFLAGS) strcat.c -o strcat

zeigersalat: zeigersalat.c
	$(CC) $(CFLAGS) zeigersalat.c -o zeigersalat

#Regel: Erst ausführbare Datei, dann Objektdatei, dann Library
auseinander: auseinander.o a.o
	$(CC) $(CFLAGS) auseinander.o a.o -o auseinander

a.o: a.c a.h

auseinander.o: auseinander.c

#depend:
#	$(CC) -MM $(SRC)> make.depend

#.SUFFIXES: .c .o .h



%.o: %.c
	$(CC)-c$(CFLAGS) $< -o $@

%.o: %.c %.h
	$(CC)-c$(CFLAGS) $< -o $@

run:
	./auseinander

clean:
	-/bin/rm -f a.o auseinander.o