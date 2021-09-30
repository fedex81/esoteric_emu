/***********************************************************************************
 *  1st_read.bin File Checker - Visual Basic to C conversion
 *  
 *  LyingWake <LyingWake@gmail.com>
 *  http://www.consolevision.com/members/fackue/
 *
 *  This is a port of 1st_read.bin File Checker 1.5's source code
 *  from Visual Basic 6.0 to Microsoft Visual C++.
 *
 *  Thanks to all those that helped; JustBurn, Quzar, GPFerror and
 *  and anyond else I forgot.
 
 Adapted by Rui Caridade for Esoteric
 ***********************************************************************************/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "1strdchk.h"


char *memoryspace;  /* A pointer that will be stored the file */


/*******************************  
 *   0 = Unscrambled 
 *   1 = Scrambled
 *  -1 = File not found 
 *******************************/

/* ///////////////////////////////////////////////////////////////////////////////////////////////// */

int IdentifyBin(int size,char *filename) 
{
 int rIdentifyBin;
 int i;

 char abc1[] = "abcdefghijklmnopqrstuvwxyz1234567890";
 char abc2[] = "abcdefghijklmnopqrstuvwxyz0123456789";
 char abc3[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
 char abc4[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
 char abc5[] = "1234567890abcdefghijklmnopqrstuvwxyz";
 char abc6[] = "0123456789abcdefghijklmnopqrstuvwxyz";
 char abc7[] = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 char abc8[] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 char temp[] = "#...'...*...-.../...2...4...7...9...;...=...?...A...C...E...G...I...J...L...N...O...Q...R...T...U...W...X...Z...";
 char temp2[] = "0123456789abcdef....(null)..0123456789ABCDEF";
 char bortmnt[] = "0123456789ABCDEF....Inf.NaN.0123456789abcdef....(null)...";
 char dreamsnes[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.0123456789-";
 char tetris[] = "abcdefghijklEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()";
 char punch[] = "PORTDEV INFOENBLSTATRADRTOUTDRQCFUNCEND";
 char netbsd[] = "$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

 rIdentifyBin = Scrambled;
 
 /* Allocate memory to store the file plus the NULL-termination char */
 memoryspace = (char *)malloc(size+1); 
 
 memcpy(memoryspace,filename,size);
 
 /* This is the little cheat ;) */
 for (i = 0; i < size; i++) 
 { 
	if (memoryspace[i] == 0x00)
	{
		memoryspace[i] = '.';
	} 
 } 
 
 /* We can't forget the NULL-termination char or program will crash! */
 memoryspace[size] = '\0'; 

 if (strstr(memoryspace, abc1)      ||
     strstr(memoryspace, abc2)      ||
     strstr(memoryspace, abc3)      ||
     strstr(memoryspace, abc4)      ||
     strstr(memoryspace, abc5)      ||
     strstr(memoryspace, abc6)      ||
     strstr(memoryspace, abc7)      ||
     strstr(memoryspace, abc8)      ||
     strstr(memoryspace, temp)      ||
     strstr(memoryspace, temp2)     ||
     strstr(memoryspace, bortmnt)   ||
     strstr(memoryspace, dreamsnes) ||
     strstr(memoryspace, tetris)    ||
     strstr(memoryspace, punch)     ||
     strstr(memoryspace, netbsd)) 
 {
	rIdentifyBin = Unscrambled;
 }
 
 /* We dont need the memory anymore */
 free(memoryspace);  

 return rIdentifyBin;
} 

