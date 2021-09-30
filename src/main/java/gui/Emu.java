package gui;

import powervr.PowerVR;
import scd.Controllers;
import scd.Maple;
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
	public final static PowerVR neon250;
	
	
	static{
			sh4cpu = new Sh4Context();
			sh4cpu.setDisassembler(new Sh4Disassembler());
			interruptController = new Intc(sh4cpu);
			memoryMappedRegs = new MMREG(interruptController);
			neon250 = new PowerVR(interruptController);
		// Maple init
			ports = new Maple[4];
			AttachPheriphals(new Controllers());
	}
	
	public static void run(){
		sh4cpu.run();
		syncHardware(sh4cpu.cycles_ran);
	}
	
	public static void AttachPheriphals(Maple dev){
		ports[connectedDevices++] = dev;
	}
	
	private static void syncHardware(int cycles){
		memoryMappedRegs.TMU(cycles);
		neon250.SyncVideoDisplay(cycles);
		interruptController.acceptInterrupts();
	}
}
