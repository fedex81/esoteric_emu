import io.Loader;

import javax.swing.SwingUtilities;

import memory.Memory;

import gui.Emu;
import gui.MainWindow;


public class execGui {

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		Memory.setUpMemoryZones();
		Loader.loadBinaryFile("dc_boot.bin",false,Memory.bios,0);
		Loader.loadBinaryFile("dc_flash.bin",false,Memory.flash,0);
		Loader.loadBinaryFile("ip.bin",false,Memory.mem, Memory.getMemoryAddress(0x8C000000 + 0x00008000));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui.Emu.guiInterface = new MainWindow();
				gui.Emu.guiInterface.setLocationRelativeTo(null);
				gui.Emu.guiInterface.setVisible(true);
			}
		});
	}
}
