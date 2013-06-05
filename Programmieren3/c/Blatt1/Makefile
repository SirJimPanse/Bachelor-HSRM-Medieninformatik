CC=gcc
CFLAGs=-ansi -g -Wall

all: hello rot13 io heron

hello: hello.c
	$(CC) $(CFLAGS) hello.c -o hello

rot13: rot13.c
	$(CC) $(CFLAGS) rot13.c -o rot13

io: io.c
	$(CC) $(CFLAGS) io.c -o io

heron: heron.c
	$(CC) $(CCFLAGS) heron.c -o heron

run: ./heron
	./heron 2.0
	./heron 4.0
	./heron 7.0
	./heron 16.0
	./heron 17.0

clean: 
	-/bin/rm -f hello hello.o
	-/bin/rm -f rot13 rot.o
	-/bin/rm -f io io.o
	-/bin/rm -f heron heron.o

