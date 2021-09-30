package io.libJia;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface CdioLib extends Library  {
	
	public static final int CD=1;
	public static final int CUE=2;
	public static final int NRG=3;
	public static final int ISO=4;

	CdioLib INSTANCE = (CdioLib)Native.loadLibrary("jCDIO", CdioLib.class);
	
		
	//ByteBuffer gdrom_buffer; 
	
	int jCDIO_init();
	
	int openMedium(int medium, String location);

	/* will allocate the media struct above and free the underlying lib allocated resources */
	int closeMedium( );

	int readSectorFromMedium(int secstart, int secnum);

	int LoadFilesMedium();

	Pointer getCDRomDevices();

}
