#ifndef cd_h
#define cd_h
int cd_init(char * sDevice);
int cd_read_sector( int secstart, int secnum);
void getDevices();
int cd_close(); 
int load_from_cd(int command);

#define LOAD_IP_BIN 0
#define LOAD_EXEC 1

#endif
