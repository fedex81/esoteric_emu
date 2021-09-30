package io;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	/*
	 * Implements a config file parser for this application
	 */
		
	public String bios;
	public String flash;
	public String IsoPlugin;
	public String CdPlugin;
	public String Browser;
	
	private static final String config="Esoteric.ini";
	
	private File file;
	
	private Properties configuration;
	private FileInputStream streamIn;
	private FileOutputStream streamOut;
	
		
	public Config(){
		file = new File(config);
		try{
			streamIn = new FileInputStream(file);
			streamOut = new FileOutputStream(file);
		}
		catch(FileNotFoundException e){
			Error.errornum = Error.FILE_NOT_FOUND;
		}
		configuration = new Properties();
	}
	
	public String getBiosPath(){
		return bios;
	}
	
	public String getFlashPath(){
		return flash;
	}
	
	public void load(){
		/*we open the config file*/
		try{
			configuration.load(streamIn);
			streamIn.close();
		}
		catch(IOException e){
			Error.errornum = Error.ioError;
			return;
		}
		/*we retreive info on the bios*/
		bios = configuration.getProperty("bios");
		/*we retreive info on the flash rom*/
		flash = configuration.getProperty("flash");
		/*we retreive info on the IsoPlugin*/
		IsoPlugin = configuration.getProperty("IsoPlugin");
		/*we retreive info on the cdPlugin*/
		CdPlugin = configuration.getProperty("CdPlugin");
		Browser = configuration.getProperty("Browser");
	}
	
	public void write(String [] input){
		/*we set info on the bios*/
		configuration.setProperty("bios",input[0]);
		/*we set info on the flash rom*/
		configuration.setProperty("flash",input[1]);
		/*we set info on the IsoPlugin*/
		configuration.setProperty("IsoPlugin",input[2]);
		/*we set info on the cdPlugin*/
		configuration.setProperty("CdPlugin",input[3]);
		
		/*we write the config file*/
		try{
			configuration.store(streamOut,"Dcemu configuration file");
			streamOut.close();
		}
		catch(IOException e){
			Error.errornum = Error.ioError;
			return;
		}
	}
	
}
