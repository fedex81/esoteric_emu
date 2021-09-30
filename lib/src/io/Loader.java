package io;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import memory.Memory;

public class Loader {

	private static boolean binaryLoaded=false;
	private static boolean biosPresent=false;
	
	/*
	 * Loads a Dreamcast binary from the given file
	 */
	public static void loadBinaryFile (String Filename,boolean check,ByteBuffer mem,int address){
		final File binary;
		FileInputStream stream;
		MappedByteBuffer bin;
		binary = new File(Filename);
		try{
			stream = new FileInputStream(binary);
		}
		catch(FileNotFoundException e){
			Error.errornum = Error.FILE_NOT_FOUND;
			return;
		}
		try{
			bin = stream.getChannel().map(MapMode.READ_ONLY,0,binary.length());
			bin.order(ByteOrder.LITTLE_ENDIAN);
		}
		catch(IOException e){ 
			Error.errornum = Error.ioError;
			System.err.println("Error");
			return;
		}
		System.out.println("Writing to Address " + Integer.toHexString(address));
		/*
		 * We need to check the format of the file here
		 */
		if(check){
			if(BinChecker.isUnscrambled(bin)) {
				System.out.println("Binary is Unscrambled");
				System.out.println("Address " + address);
				bin.rewind();
				mem.position(address);
				mem.put(bin);
			}
			else{
				// binary is scrambled
				System.out.println("Binary is Scrambled");
			//	Scrambler sc = new Scrambler(array);
			//	byte [] a = sc.descramble();
				//System.arraycopy(a, 0, mem, address, a.length);
			}
		}
		else{ // i'm so stupid sometimes.
			bin.rewind();
			mem.position(address);
			mem.put(bin);
		}
		
	// before we do anything else
	System.out.println("Binary Loaded  " + Filename);
	}
	
	private static final void printArray(byte [] array,int end){
		for (int i =0; i < end;i++)
			System.out.print(array[i]);
	}
	
	private static final void readFile(String name,ByteBuffer mem,int address){
		FileInputStream stream;
		try{
			stream = new FileInputStream(name);
		}
		catch(FileNotFoundException e){	
			Error.errornum = Error.FILE_NOT_FOUND;
			return;
		}
		try{
			stream.getChannel().read(mem,address);
			stream.close();
		}
		catch(IOException e){ 
			Error.errornum = Error.ioError;
			return;
		}
	}
	
	/*
	 * Loads the bios and flash files
	 */
	public static void LoadBiosFiles(Config c){
		/* Load the bios*/
		readFile(c.bios, Memory.bios,0);
		if(Error.errornum !=0)
			return;
		/* Load the flash ram*/
		readFile(c.flash, Memory.flash,0);
	}
	
}
