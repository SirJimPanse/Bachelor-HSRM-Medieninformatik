CC = gcc
CFLAGS = -ansi -g -Wall
SYMLINK = ln -s

all: komprimier split test
	-/bin/rm -f dekomprimier
	$(SYMLINK) komprimier dekomprimier

komprimier.o: komprimier.c
	$(CC) -c $(CFLAGS) komprimier.c -o komprimier.o

komprimier: komprimier.o
	$(CC) $(CFlAGS) komprimier.o -o komprimier

split.o: split.c
	$(CC) -c $(CFLAGS) split.c -o split.o

split: split.o
	$(CC) $(CFLAGS) split.o -o split 

test.o: test.c
	$(CC) -c $(CFLAGS) test.c -o test.o

test: test.o
	$(CC) $(CFLAGS) test.o -o test

clean:
	-/bin/rm -f komprimier.o komprimier
	-/bin/rm -f dekomprimier