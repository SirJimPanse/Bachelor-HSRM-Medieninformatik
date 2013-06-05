CC=gcc
CFLAGS=-ansi -g -Wall

all: rot13 io heron test

rot13: rot13.c
	$(CC) $(CFLAGS) rot13.c -o rot13

io: io.c
	$(CC) $(CFLAGS) io.c -o io

heron: heron.c
	$(CC) $(CFLAGS) heron.c -o heron

test: test.c
	$(CC) $(CFLAGS) test.c -o test

clean:
	-/bin/rm -f rot13 rot13.o
