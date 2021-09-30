#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>

#include <cdio/cdio.h>
#include <cdio/cd_types.h>
#include <cdio/iso9660.h>
#include "cd.h"

#include "jCDIO.h"

// variables libcdio
CdIo * cdio;

CdioList_t * root_file_list;

static int availableNativeDevices;

int cd_init(char * sDevice)
{
	    int i;
	
		if (sDevice == NULL){
				fprintf(stderr, "cdio_get_default_device: %s\n",(char *) sDevice);
				return 0;
		}
		
		else fprintf(stderr,"Using %s",(char*)sDevice);
		
		cdio = cdio_open((char*)sDevice, DRIVER_UNKNOWN);
	
		if (!cdio)
		{
			fprintf(stderr, "cdio_open: cdio NULL\n");
			return 0;
		}		

		gdDevice.num_tracks = cdio_get_num_tracks(cdio);

		//filing the gdDevice.List with the info on the Cd
	
		for(i=0; i < gdDevice.num_tracks;i++)
		{
			// this probably is too much, keeping both, but oh well..i'll get to it
			gdDevice.List[i].lba = 	cdio_get_track_lba(cdio, i);
			gdDevice.List[i].lsn =  cdio_get_track_lsn(cdio, i);
			switch(cdio_get_track_format(cdio, i))
    		{
        		case TRACK_FORMAT_AUDIO:	gdDevice.List[i].sector_size = 2352; gdDevice.List[i].format = 0; break;
        		case TRACK_FORMAT_XA:		gdDevice.List[i].sector_size = 2048; gdDevice.List[i].format = 2048;break;
       			case TRACK_FORMAT_DATA:		gdDevice.List[i].sector_size = 2048; gdDevice.List[i].format = 1024;break;
       			default:
       				gdDevice.List[i].sector_size = 2048; gdDevice.List[i].format = 1024;	 
       			break;
			}	
		} 

	return 1;
}

void getDevices(){
	int i;
	char ** drives =  cdio_get_devices(DRIVER_DEVICE);
	for(i = 0; drives[i] != NULL; i++){
		strcpy(devices[i],drives[i]);
	}
	availableNativeDevices = i;
	free(drives);
}

int getNumberOfAvailableDevices(){
	return availableNativeDevices;
}

int cd_close(){
	cdio_destroy(cdio);
	return 0;
}


int cd_read_sector(int secstart, int secnum)
{
	int ret = 0, i;
 
	fprintf(stderr, "Reading sectores, secstart %d, secnum %d\n", secstart, secnum);
	
	ret = cdio_read_data_sectors(cdio, &gdrom_buffer[positionBuffer], secstart - 150 + i, CDIO_CD_FRAMESIZE,secnum);
			
	if (ret != 0)
			fprintf(stderr, "Error reading  sector  %d\n", secstart - 150 + i);

	positionBuffer += ISO_BLOCKSIZE*secnum;
	return ret;
}

int cd_read_sector_direct(int secstart, int secnum,int address,char * buffer)
{
	int ret = 0, i;
 
	fprintf(stderr, "Reading sectores, secstart %d, secnum %d\n", secstart, secnum);
	
	ret = cdio_read_data_sectors(cdio,&buffer[address], secstart - 150 + i, CDIO_CD_FRAMESIZE,secnum);
			
	if (ret != 0)
			fprintf(stderr, "Error reading  sectors \n");

	return ret;
}

static iso9660_stat_t * findFile(char * filename){
	CdioListNode_t *entnode;

	root_file_list = iso9660_fs_readdir(cdio, "/", false);

	if (root_file_list == NULL)
	{
		fprintf(stderr, "Couldn't open root dir.\n");
		return NULL;
	}
	
	
	_CDIO_LIST_FOREACH(entnode, root_file_list)
	{
				char filename[31];
				iso9660_stat_t *p_statbuf = (iso9660_stat_t *) _cdio_list_node_data (entnode);
				iso9660_name_translate(p_statbuf->filename, filename);
				
				
				if (!strcasecmp(filename,filename))
				{
					return p_statbuf;
				}
	}	
	return NULL;
}

static int loadIp(){
	lsn_t lsn = 0;
	uint32_t size = 0;
	uint32_t secsize = 0;
		
	iso9660_stat_t * p_statbuf = findFile("ip.bin");
	
	/*
	 *	if the file is not present and this is not a bootable cd / cdimage 
	 *  return
	 */
	
	if(p_statbuf == NULL) {
		if(gdDevice.num_tracks == 1) {
		 	fprintf(stderr,"Could not find ip.bin in the iso9660 FS");
		 	return 0;
		}
	}
	else{
		lsn = p_statbuf->lsn;
		secsize = p_statbuf->secsize;
		size = p_statbuf->size;
	}

	
	if (size > 0)
	{
		fprintf(stderr, "leyendo %s desde lsn %d, %d sectores, %d bytes por sector.\n", "ip.bin", lsn, secsize, ISO_BLOCKSIZE);

		 if (cdio_read_data_sectors(cdio,ipBIN, lsn, ISO_BLOCKSIZE, secsize) == DRIVER_OP_SUCCESS) 
				fprintf(stderr, "File Read Sucessfully.\n"); 
	}	// we assume it is a bootable image so we read the first 16 sectors if we're searching for the ip.bin
	else {
		if (cdio_read_data_sectors(cdio,ipBIN,gdDevice.List[gdDevice.num_tracks].lsn,ISO_BLOCKSIZE,16) > 0) 
				fprintf(stderr, "File read sucessfully.\n"); 
				return 1;
	}
	return size;
}

static int loadExec(char * fname){
	lsn_t lsn = 0;
	uint32_t size = 0;
	uint32_t secsize = 0;
	
	iso9660_stat_t * p_statbuf = findFile("1st_read.bin");
	
	if(p_statbuf == NULL) {
		 	fprintf(stderr,"Could not find 1st_read.bin in the iso9660 FS");
		 	return 0;
	}
	else{
		lsn = p_statbuf->lsn;
		secsize = p_statbuf->secsize;
		size = p_statbuf->size;
	}
	
	// we won't need this anymore
	_cdio_list_free(root_file_list, true);

	// necesitamos el lsn y el size
	if (size > 0)
	{
		fprintf(stderr, "Reading %s from lsn %d\n", fname, lsn);
		
		 exec = (char *) malloc(size);	

		 if (cdio_read_data_sectors(cdio,exec, lsn, ISO_BLOCKSIZE, secsize) == DRIVER_OP_SUCCESS)
				fprintf(stderr, "File Read Sucessfully.\n"); 
		 else return 0;
	}
	return size;
}

int load_from_cd(int command)
{
	switch(command){
		case LOAD_IP_BIN:
			return loadIp();
		case LOAD_EXEC:
			return loadExec("1st_read.bin");
	}
	return 0; // just to keep gcc happy
}
