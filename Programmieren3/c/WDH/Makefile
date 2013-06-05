CC = gcc 
CFLAGS = -ansi -g -Wall

all: groesse testmylib bits myqueuetest

groesse.o: groesse.c
	$(CC) -c $(CFLAGS) groesse.c -o groesse.o

groesse: groesse.o
	$(CC) $(CFLAGS) groesse.o -o groesse

testmylib.o: testmylib.c

mylib.o: mylib.c mylib.h

testmylib: testmylib.o mylib.o
	$(CC) $(CFLAGS) testmylib.o mylib.o -o testmylib

bits.o: bits.c

bits: bits.o
	$(CC) $(CFLAGS) bits.o -o bits

myqueuetest.o: myqueuetest.c

myqueue.o: myqueue.c myqueue.h

myqueuetest: myqueuetest.o myqueue.o
	$(CC) $(CFLAGS) myqueuetest.o myqueue.o -o myqueuetest

%.o : %.c
	$(CC) -c $(CFLAGS) $< -o $@

%.o : %.c %.h
	$(CC) -c $(CFLAGS) $< -o $@

test: ./testmylib
	./testmylib

clean:
	-/bin/rm -f groesse.o groesse
	-/bin/rm -f testmylib.o testmylib