package gui;

import io.libJia.CdioLib;

import java.io.File;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import powervr.PowerVR;
import scd.Controllers;
import scd.Maple;
import sh4.Dynarec;
import sh4.Intc;
import sh4.MMREG;
import sh4.Sh4Context;
import sh4.Sh4Disassembler;


public class Emu {

	public final static Maple [] ports;
	public static int connectedDevices =0;
	public final static Sh4Context sh4cpu;
	public final static Intc interruptController;
	public final static MMREG memoryMappedRegs;
	private static PowerVR neon250;
	public final static Dynarec sh4cpu_fast;
	public static MainWindow guiInterface;
	
	
	
	static{
			sh4cpu = new Sh4Context();
			sh4cpu.setDisassembler(new Sh4Disassembler());
			sh4cpu_fast = new Dynarec();
			interruptController = new Intc();
			memoryMappedRegs = new MMREG();
		// Maple init
			ports = new Maple[4];
		// lib path init
			String jvm = System.getProperty("sun.arch.data.model");
			if(System.getProperty("os.name").startsWith("Windows")){
				String windows_libs = "." + File.separator + "libs" + File.separator + "win" + jvm;
				System.setProperty("jna.library.path",windows_libs);
				System.setProperty("java.library.path",System.getProperty("java.library.path") 
						+ File.pathSeparator + windows_libs);
			}
			else if(System.getProperty("os.name").startsWith("Mac")){
				String macos_libs = "." + File.separator + "libs" + File.separator + "MacOS" + jvm;
				System.setProperty("jna.library.path",macos_libs);
				System.setProperty("java.library.path",System.getProperty("java.library.path") 
						+ File.pathSeparator + macos_libs);
			}
			// were i assume so kind of unix variant
			else{
				String linux_libs ="." + File.separator +  "libs" + File.separator + "Linux" + jvm;
				NativeLibrary.addSearchPath("jCDIO",linux_libs);
				System.setProperty("java.library.path",System.getProperty("java.library.path") 
						+ File.pathSeparator + linux_libs);
			}
			CdioLib.cdInterface.jCDIO_init();
	}
	
	public static void start(){
		neon250 = new PowerVR(interruptController);
		AttachPheriphals(new Controllers());
	}
	
	public static void run(){
		while(true){
			sh4cpu.run();
			//sh4cpu_fast.run();
			syncHardware(sh4cpu.cycles_ran);
		}
	}
	
	private static void AttachPheriphals(Maple dev){
		ports[connectedDevices++] = dev;
	}
	
	private static void syncHardware(int cycles){
		neon250.SyncVideoDisplay(cycles);
	}
}
