/*
 * Overall structure
 * For a CD-R to be bootable on the Dreamcast, it should have two sessions.
 * The first should contain only a normal audio track.
 * It doesn't matter what kind of audio you actually put there, silence is fine.
 * (It has been suggested that a data track could also be used for the first session.
 * I haven't tried this myself though.) The second session should contain a CD/XA data track (mode 2 form 1).
 * This data track should contain a regular ISO9660 file system, and in the first 16 sectors a correct bootstrap (IP.BIN). 
 * 
 * From Marcus Comstedt dreamcast programming site at
 * 
 * http://mc.pp.se/dc/cdr.html
 *  
 */

// a 128kb buffer
char  gdrom_buffer[131072]; 

 // will keep the number of read sectors
int sector_count;
int positionBuffer;

typedef struct session{
	int format;
	int sector_size;
	int sectors; // the number of sectors
	int lba;
	int lsn;
}session;


// struct describing the curent media
typedef struct media
{
	session List [2]; // we will have max 2
	int num_tracks;
}media;


/*
 * String array containing the devices present on the system 
 */

char devices [10][128];

// the number of drives in the system
int getNumberOfAvailableDevices();

/* will keep the ipBin*/
char ipBIN [32768];

/* pointer to the data which contains the executable */
char * exec;

int jCDIO_init();

media * openMedium(char * location);


/* will allocate the media struct above and free the underlying lib allocated resources */
int closeMedium( );

/* this function will fill the devices array above with the correct info */
void getCDRomDevices();

int readSectorSystemMem(int secstart,int secnum,const char * mem);
	
int readSectorDirectMem(int secstart,int secnum,int address,const char * directBuffer);

int LoadFilesMedium();

