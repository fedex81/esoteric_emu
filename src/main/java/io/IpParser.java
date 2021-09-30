package io;

import java.io.UnsupportedEncodingException;


public final class IpParser {


	// offsets for the several IP.BIn fields
	private static final int IP_SIZE [] = {0x00f,0x00f,0x00f,0x007,0x007,0x009,0x005,0x00f,0x00f,0x00f,0x07f};
	
	public String [] Fields;
	
	public static final int HW_ID =0;
	public static final int MK_ID =1;
	public static final int DEV_ID =2;
	public static final int AREA =3;
	public static final int PHER = 4;
	public static final int PROD_NUM = 5;
	public static final int PROD_VER = 6;
	public static final int REL_DATE = 7;
	public static final int BOOT = 8;
	public static final int CPNY = 9;
	public static final int NAME = 10;
	

	
	public IpParser(){
		Fields = new String[11];
	}
	
	public void fill(byte [] buffer) throws UnsupportedEncodingException{
		int offset =0;
		for(int i =0; i < 11; i++){
			Fields[i] = new String(buffer,offset,IP_SIZE[i],"ISO-8859-1");
			offset += IP_SIZE[i] + 0x1;
		}
	}
	
	/*
	 * The IP.Bin should have been loaded by now to the address on the main memory.
	 */
	public void fill(byte [] buffer,int address) throws UnsupportedEncodingException{
		int offset = address;
		for(int i =0; i < 11; i++){
			Fields[i] = new String(buffer,offset,IP_SIZE[i],"ISO-8859-1");
			offset += IP_SIZE[i] + 0x1;
		}
	}

	
	public String getField(int field){
		return Fields[field];
	}
	
	public void print(){
		for(int i=0; i < 11; i++)
			System.out.println(Fields[i]);
	}
	
}
