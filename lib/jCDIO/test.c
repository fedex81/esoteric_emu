#include "jCDIO.h"
#include <stdio.h>
#include <stdlib.h>

int main(){
 int i;
 jCDIO_init();
 getCDRomDevices();
 int available = getNumberOfAvailableDevices();
  puts("GETS HERE");
  for (i =0;i < available ; i++)
	printf("Device -> %s\n", devices[i]);
 closeMedium( );
 return 0;
}
