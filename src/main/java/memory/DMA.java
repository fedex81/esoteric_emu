package memory;

import gui.Emu;
import scd.Maple;
import sh4.Intc;
import sh4.Sh4Context;

/*
 * Based on info from 
 *  
 * http://mc.pp.se/dc/maplebus.html
 * swirly
 * dcemu
 * 
 */
public final class DMA {
	
	  /**
	   * This info was retreived from the emulator lxdream by Nathan Keynes (www.lxdream.org) 
	   * 
	   * Input data looks like this:
	    *    0: transfer control word
	    *      0: length of data in words (not including 3 word header)
	    *      1: low bit = lightgun mode
	    *      2: low 2 bits = port # (0..3)
	    *      3: 0x80 = last packet, 0x00 = normal packet
	    *    4: output buffer address
	    *    8: Command word
	    *      8: command code
	    *      9: destination address
	    *     10: source address
	    *     11: length of data in words (not including 3 word header)
	    *   12: command-specific data
	    */
	
	/*
	 * Maple uses the root bus which means it can only transfer 32 bytes at a time.
	 */
	
	public static boolean maple_dma = false;
	
	public static final void MapleDMA(){
		// we should handle MAPLE DMA here
		//System.out.println("MAPLE");
		
		boolean last=false;
		int numberOfFrames;
		int port;
		int ComandWord;
		int TransferControlWord;
		int OutputBufferAddress;
		int receiveAddress;
		int sendAddress;		
		int packet;
		int currentAddr = Memory.getMemoryAddress(Memory.ControlRegsDword.get(Memory.MAPLE_DMAADDR));
		//	System.out.println("MAPLE DMA ADDRESS " + Integer.toHexString(currentAddr));	
		while(!last){
				TransferControlWord = Memory.memViewDWORD.get(currentAddr >>> 2);
				OutputBufferAddress = Memory.getMemoryAddress(Memory.memViewDWORD.get((currentAddr +4) >>> 2));
				/*
				 * The frame header and numeric parameters are in big endian byte order. And even being
				 * on java our data is always little endian
				 * Dreamcast Programming by Marcus Comstedt
				 */
				ComandWord = Memory.memViewDWORD.get((currentAddr +8) >>> 2);
				numberOfFrames = (TransferControlWord & 0xff) + 1; // we add 1 to include this frame
				//	System.out.println("Maple number of frames: " + numberOfFrames);
				//	System.out.println("Transfer Control Word "  + Integer.toHexString(TransferControlWord));
				last = ((TransferControlWord & 0x80000000) !=0);
				//System.out.println("Is it the last " +  last);
				port = ((TransferControlWord >> 16) & 0x3);
				//System.out.println("PORT "  + port);
				if (port > Emu.connectedDevices-1) // makes sense no? ^_^
				{
					// if a device in a given port 0xFFFFFFFF is written to it :)
					Memory.memViewDWORD.put(OutputBufferAddress >>> 2,0xFFFFFFFF);
				}
				else if (numberOfFrames > 0){
					
					receiveAddress = (ComandWord >> 8) & 0xFF;
					sendAddress = (ComandWord >> 16) & 0xFF;
					
					if (receiveAddress != 0x20){
						Memory.memViewDWORD.put(OutputBufferAddress >>>2,0xFFFFFFFF);
					}
					else {
						switch (ComandWord & 0xff) {
							// device info
						case 1:
							ComandWord = 0x05 | // device info (response)
							((sendAddress << 8) & 0xFF00) |
							((((receiveAddress == 0x20) ? 0x20 : 0) << 16) & 0xFF0000) |
							((28 << 24) & 0xFF000000);
							
							Memory.memViewDWORD.put(OutputBufferAddress >>> 2,ComandWord);
							Emu.ports[port].writeDeviceInfo(OutputBufferAddress+4);
							break;
							// condition info
						case 9:
							ComandWord = 0x08 | // data transfer (ComandWord)
							((sendAddress << 8) & 0xFF00) |
							((((receiveAddress == 0x20) ? 0x20 : 1) << 16) & 0xFF0000) |
							(((Emu.ports[port].getConditionInfoSize()/4 + 1) << 24) & 0xFF000000);
							//System.out.println("Packet " + Integer.toHexString(ComandWord));
							//System.out.println("OutputBufferAddress " + Integer.toHexString(OutputBufferAddress));
							Memory.memViewDWORD.put(OutputBufferAddress >>>2,ComandWord);
							Memory.memViewDWORD.put((OutputBufferAddress +4) >>> 2, Maple.MAPLE_CONTROLLER);
							//System.out.println("Writing condition info at " + Integer.toHexString(OutputBufferAddress+8));
							Emu.ports[port].getConditionInfo(OutputBufferAddress+8);
						break;
						};
					}
					Emu.interruptController.addInterrupts(Intc.ASIC_EVT_MAPLE_DMA);	
				}	
				currentAddr += numberOfFrames * 4 + 8;
			}
		}
	
	public static void PvrDma(){
		
	}
	
	public static void AicaDma(int channel){
		
	}
	
	public static void GdRomDma(){
		
	}
}
