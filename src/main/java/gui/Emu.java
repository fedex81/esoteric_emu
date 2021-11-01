package gui;

import memory.IMemory;
import memory.Memory;
import powervr.PowerVR;
import scd.Controllers;
import scd.Maple;
import sh4.Intc;
import sh4.MMREG;
import sh4.Sh4Context;
import sh4.Sh4Disassembler;


public class Emu {

	public Maple [] ports;
	public int connectedDevices =0;
	public Sh4Context sh4cpu;
	public Intc interruptController;
	public MMREG memoryMappedRegs;
	public IMemory memory;
	public PowerVR neon250;

	private static Emu INSTANCE;

	public Emu(IMemory memory){
		this.memory = memory;
		sh4cpu = new Sh4Context(memory);
		sh4cpu.setDisassembler(new Sh4Disassembler());
		interruptController = new Intc(sh4cpu);
		memoryMappedRegs = new MMREG(interruptController);
		neon250 = new PowerVR(interruptController);
		// Maple init
		ports = new Maple[4];
		AttachPheriphals(new Controllers());
		INSTANCE = this;
	}

	
	public void run(){
		sh4cpu.run();
		syncHardware(sh4cpu.cycles_ran);
	}
	
	public void AttachPheriphals(Maple dev){
		ports[connectedDevices++] = dev;
	}
	
	private void syncHardware(int cycles){
		memoryMappedRegs.TMU(cycles);
		neon250.SyncVideoDisplay(cycles);
		interruptController.acceptInterrupts();
	}

	public static int getConnectedDevices() {
		return INSTANCE.connectedDevices;
	}

	public static Maple[] getPorts() {
		return INSTANCE.ports;
	}

	public static Intc getInterruptController() {
		return INSTANCE.interruptController;
	}

	public static Sh4Context getSh4cpu() {
		return INSTANCE.sh4cpu;
	}
}
