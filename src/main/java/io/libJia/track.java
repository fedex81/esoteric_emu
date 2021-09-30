package io.libJia;

import com.sun.jna.Structure;

public class track extends Structure {

	int format;
	int sector_size;
	int sectors; // the number of sectors
	int lba;
	int lsn;
}
