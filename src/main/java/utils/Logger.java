package utils;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	public static boolean enabled = false;

	private static FileWriter logMem;
	private static FileWriter logCPU;
	private static FileWriter logSerial;
	
	public static final int MEM = 0;
	public static final int CPU = 1;
	
	static {
		try {
			 logMem = new FileWriter("memlog.txt");
			 logCPU = new FileWriter("cpulog.txt");
			 logSerial = new FileWriter("serialog.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void serialLog(char character){
		if(!enabled){
			return;
		}
		try {
			logSerial.append(character);
			logSerial.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void log(int logSource,String str){
		if(!enabled){
			return;
		}
		switch (logSource) {
		case MEM:
			try {
					logMem.write(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
		case CPU:
				try {
					logCPU.write(str);
					logCPU.write("\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		break;
		default:
			break;
		}
	}
}
