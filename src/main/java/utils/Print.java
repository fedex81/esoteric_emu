package utils;

import java.lang.String;
import java.util.Formatter;
/*
 * Implements some useful methods
 */

public class Print {
	
	private static StringBuilder opString; 
	private Formatter form;
	
	public Print(int capacity){
		opString= new StringBuilder(capacity);
		form = new Formatter(opString);
	}
	
	public Print(){
		opString = new StringBuilder(30);
		form = new Formatter(opString);
	}
	
	/*
	 *  This a implementation of sprintf very specific to our needs
	 */
	
	public static String sprintOpcode(StringBuffer buffer,String x,String [] regs){
		
		
		return opString.toString();
	}
	
		

}
