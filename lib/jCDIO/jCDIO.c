#include "jCDIO.h"
#include "cd.h"
#include "dcrp.h"
#include "1strdchk.h"
#include "unistd.h"
#include <stdlib.h>
#include <stdio.h>
#include <cdio/cdio.h>

static media gdDevice;

int jCDIO_init(){
			// we init the cdio lib
	 return cdio_init();
}

media * openMedium(char * location){
	/* the cast is due to the possible difference in charsets between the 
	 * jvm and the underlying OS 
	 */
	if(cd_init((char*) location))
		return &gdDevice;
	else return NULL;
}

/* will allocate the media struct above and free the underlying lib allocated resources */
int closeMedium( ){
	return cd_close();
}

int readSectorSystemMem(int secstart,int secnum){
 	return cd_read_sector(secstart,secnum);
}
	
int readSectorDirectMem(int secstart,int secnum,int address,const char * directBuffer){
	return cd_read_sector_direct(secstart,secnum,address,directBuffer);
}

void getCDRomDevices(){
		return getDevices();
}

/* Loads the ip.bin and the binary and returns different then 0 if
 * everything went smoothly 
 */
int LoadFilesMedium()
{
	int size =0;
	if(load_from_cd(LOAD_IP_BIN)) {
		if ((size = load_from_cd(LOAD_EXEC))) {
			// 1 if scrambled
			if(IdentifyBin(size,exec)){
					return descrambler(size);
			}
			else return size;
		}
  	 }	
	return 0;
}
