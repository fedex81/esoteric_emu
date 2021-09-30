package io;

public class Error {
	
	public static final int FILE_ALREADY_LOADED =1;
	public static final int RESTART = 2;
	public static final int CPU_EXEC =3;
	public static final int MEM_ALLOC = 4;
	public static final int badBIOS = 5;
	public static final int badFlash = 6;
	public static final int ioError = 7;
	public static final int FILE_NOT_FOUND = 8;
	
	public static int errornum=0;
	
	private static String [] messages={"A binary is already loaded.To apply the changes you need to restart",
										"A restart is needed",
										"Sh4 emulation core has found a problem",
										"Could not allocate sufficient memory",
										"This bios does not have the correct size",
										"This flash ram does not have the correct size",
										"IO error",
										"Couldn't find the given file: "
										};
	
	public static String perror(int e){
		return messages[e];
	}

}
