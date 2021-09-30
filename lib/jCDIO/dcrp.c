/* dcrp.c - scrambles and unscrambles Dreamcast-executables
 *
 * 2000 <c> Maiwe
 *
 * contact: jlo@ludd.luth.se
 *
*/

/* Notes:
 * ~~~~~~
 *
 * Dreamcast(tm) has a special method for booting regular CDs as opposed
 * to GDs. The executable file to be booted must be scrambled in a
 * certain way in order to execute properly which is what this program
 * does.
 *
*/

#include <stdlib.h>
#include <stdio.h>
#include "dcrp.h"
#include "jCDIO.h"

void usage(void);
void scramble(void);
void scramble2(int, char *);
void copy_data(char *);
void unscramble(void);
void unscramble2(int, char *);
void copy_data2(char *);

unsigned int permutation_key = 0;
unsigned short permutation_buf[0x10000];
char * input_buf;
char * output_buf;
int buf_pos;
int data_size;


int
descrambler(int size)
{
	if (!(output_buf = malloc(size))) {
		fprintf(stderr, "Could not allocate memory");
		return 0;
	}
	data_size = size;
	input_buf = exec;
	free(input_buf);
	unscramble();
	exec = output_buf;
	return 1;
}


void
usage()
{
	fprintf(stderr, "dcrp v1.1 13-Jun-2000 <c> Maiwe\n");
	fprintf(stderr, "usage: dcrp <scramble | unscramble> <input> <output>\n");
	exit (-1);
}

void
scramble()
{

	int j;
	char *ptr;
	int align;
	int size = data_size;

	permutation_key = size & 0x0000ffff;
	buf_pos = 0;

	align = size & 0x0000001f;
	if (align != 0)
		size -= align;

	ptr = input_buf;
	j = 0x00200000;
	do {
		while (size >= j) {
			scramble2(j, ptr);
			size = size - j;
			ptr = ptr + ((j>>2)<<2);
		}
		j = j>>1;
	} while (j >= 0x20);

	if (align != 0)
		copy_data(ptr);

}

void
scramble2(size, addr)
	int size;
	char *addr;
{

	unsigned short *ptr = permutation_buf;
	int i;
	int bs = size>>5;
	unsigned short *k;
	unsigned short index;
	int m;
	unsigned short temp;

	for (i = 0; i < bs; i++)
		ptr[i] = i;

	k = &ptr[bs-1];
	for (m = bs - 1; m >= 0; m--) {
		permutation_key = (permutation_key * 2109 + 9273) & 0x7fff;
		index = (((permutation_key + 0xc000) & 0xffff) * m)>>16;

		temp = ptr[index];
		ptr[index] = *k;
		*k = temp;
		copy_data(addr + ((*k)<<5));
		k--;
	}

}

void
copy_data(addr)
	char *addr;
{
	int i;

	for (i = 0; i < 0x20 && buf_pos < data_size; i++)
		*(output_buf + (buf_pos++)) = addr[i];
}

void
unscramble()
{

	int j;
	char *ptr;
	int align;
	int size = data_size;

	permutation_key = size & 0x0000ffff;
	buf_pos = 0;

	align = size & 0x0000001f;
	if (align != 0)
		size -= align;

	ptr = output_buf;
	j = 0x00200000;
	do {
		while (size >= j) {
			unscramble2(j, ptr);
			size = size - j;
			ptr = ptr + ((j>>2)<<2);
		}
		j = j>>1;
	} while (j >= 0x20);

	if (align != 0)
		copy_data2(ptr);

}

void
unscramble2(size, addr)
	int size;
	char *addr;
{

	unsigned short *ptr = permutation_buf;
	int i;
	int bs = size>>5;
	unsigned short *k;
	unsigned short index;
	int m;
	unsigned short temp;

	for (i = 0; i < bs; i++)
		ptr[i] = i;

	k = &ptr[bs-1];
	for (m = bs - 1; m >= 0; m--) {
		permutation_key = (permutation_key * 2109 + 9273) & 0x7fff;
		index = (((permutation_key + 0xc000) & 0xffff) * m)>>16;

		temp = ptr[index];
		ptr[index] = *k;
		*k = temp;
		copy_data2(addr + ((*k)<<5));
		k--;
	}

}

void
copy_data2(addr)
	char *addr;
{
	int i;

	for (i = 0; i < 0x20 && buf_pos < data_size; i++)
		addr[i] = *(input_buf + (buf_pos++));
}
