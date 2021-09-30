INCLUDES = -I /usr/include/
OBJS = cd.o jCDIO.o dcrp.o 1strdchk.o test.o
CC = gcc
CFLAGS = -Wall -fPIC 

all:   jCDIO

clean: 
	rm -f $(OBJS)  "libjCDIO.so"  *.*~*


test: $(OBJS)
	gcc $(OBJS)  -o test -L. -ljCDIO

test.o:test.c
	$(CC) -c $(CFLAGS) $(INCLUDES) test.c

jCDIO: $(OBJS)
		$(CC) -shared -o "libjCDIO.so" $(OBJS)   -liso9660 -lcdio


jCDIO.o: jCDIO.c
	$(CC) -c $(CFLAGS) $(INCLUDES) jCDIO.c 


cd.o: cd.c
	$(CC) -c $(CFLAGS) $(INCLUDES) cd.c

dcrp.o: dcrp.c
	$(CC) -c $(CFLAGS) $(INCLUDES) dcrp.c

1strdchk.o: 1strdchk.c
	$(CC) -c $(CFLAGS) $(INCLUDES) 1strdchk.c
