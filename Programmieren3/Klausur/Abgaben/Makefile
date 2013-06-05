CC=gcc
CFLAGS=-ansi -g -Wall

all: bits mylib queue

bits: bits.c
	$(CC) $(CFLAGS) bits.c -o bits

mylib: mylib.c
	$(CC) $(CFLAGS) mylib.c -o mylib

queue.o: queue.c
	$(CC) $(CFLAGS) queue.c -o queue

queue: queue.o
	$(CC) $(CFLAGS) queue.o -o queue
