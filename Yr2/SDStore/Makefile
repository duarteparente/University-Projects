#==========================================================================================================================================
CC = gcc
CFLAGS = -Wall -g
#==========================================================================================================================================
INCLUDES = -Iinclude
LIBS = -lm


all: server client

server: bin/sdstored

client: bin/sdstore

bin/sdstored: obj/sdstored.o
		$(CC) -g obj/sdstored.o -o bin/sdstored

obj/sdstored.o: src/sdstored.c
		$(CC) $(CFLAGS) -c src/sdstored.c -o obj/sdstored.o

bin/sdstore: obj/sdstore.o
		$(CC) -g obj/sdstore.o -o bin/sdstore

obj/sdstore.o: src/sdstore.c
		$(CC) $(CFLAGS) -c src/sdstore.c -o obj/sdstore.o

.PHONY: clean
clean:
		rm obj/*.o bin/s*