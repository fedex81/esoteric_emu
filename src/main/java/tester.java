import sh4.Bios;
import sh4.Sh4Context;
import memory.Memory;

import gui.Emu;
import io.Loader;


public class tester {

	/**
	 * @param args gg_3dEngine
	 */
	public static void main(String[] args) {
		Memory.setUpMemoryZones();
		Loader.loadBinaryFile("dc_boot.bin",false,Memory.bios,0);
		Loader.loadBinaryFile("dc_flash.bin",false,Memory.flash,0);
		Loader.loadBinaryFile("ip.bin",false,Memory.mem, Memory.getMemoryAddress(0x8C000000 + 0x00008000));
		//Memory.bios.position(Memory.getMemoryAddress(Bios.DREAMCAST_ROMFONT_BASE));
		//Memory.bios.put(Bios.romfont);
		Loader.loadBinaryFile("plasma.bin",false,Memory.mem,Memory.getMemoryAddress(0x8C000000 + 0x00010000));
		Emu test = new Emu();
		//Sh4Context.debugging = true;
		while(true)
			test.run();
		/*Sh4Context cpu = new Sh4Context();
		cpu.setDisassembler(new Sh4Disassembler());
		cpu.cycles = 10;
		cpu.run();
		*/
	}

}
