package memory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.MemoryManagerMXBean;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import scd.Maple;
import sh4.Bios;
import sh4.Intc;
import sh4.MMREG;
import sh4.Sh4Context;
import utils.Logger;
import gui.Emu;

import org.lwjgl.BufferUtils;

import powervr.PowerVR;
/*
 *
 *  INDEXES ARE IN TERMS OF BYTES NO MATTER WHAT
 *
 */
public final class Memory implements IMemory {

	/* memory sizes */
		
	private static final int biosSize = 2 * 1024 * 1024; // 2 megabytes
	private static final int flashSize = 131 * 1024; // 128kbytes
	private static final int videoSize = 8 * 1024 * 1024; // 8 megabytes
	private static final int soundSize = 0x00200000; // 2 megabytes
	private static final int ramSize = 16 * 1024 * 1024; // 16 megabytes
	private static final int regmapSize = 16 * 1024 * 1024; // i need to find some way to reduce this value
	
	/*
	 * a0702c00: (av_ctrl)
	+-------------------------------------+
	| 31-16 | 15-10 | 9-8   | 7-1 | 0     |
	|n/a  | n/a   | cable | n/a | reset |
	+-------------------------------------+

		cable:
		type of video cable the display is to be set for:

		0: VGA
		1: ???
		2: RGB
	 */
	public static final int cable =0x00000000;
	
	/* Memory areas*/
	public static final ByteBuffer bios;
	public static final IntBuffer biosViewDWORD;
	public static final ShortBuffer biosViewWord;
	
	public static final ByteBuffer flash;
	public static final IntBuffer flashViewDWORD;
	public static final ShortBuffer flashViewWord;
	
	public static final ByteBuffer mem;
	public static final IntBuffer memViewDWORD;
	public static final ShortBuffer memViewWord;
	
	public static final ByteBuffer regmap;

	// these 2 are ByteBuffer due to the way lwjgl works
	public static final ByteBuffer video;
	public static final ShortBuffer videoViewWord;
	
	// for efficient texture upload
	public static final IntBuffer TextureMemory;
	
	public static final ByteBuffer sound;
	public static final IntBuffer SamplesMemory;
	public static final ShortBuffer soundViewWord;
	
	/* STOREQUEUES */
	public static final IntBuffer SQ0;
	public static final IntBuffer  SQ1;
	public static final ByteBuffer bSQ0;
	public static final ByteBuffer bSQ1;
	public static final LongBuffer SQ0ViewQWORD;
	public static final LongBuffer SQ1ViewQWORD;
	

	public static final ByteBuffer ControlRegs;
	public static final ShortBuffer ControlRegsWord;
	public static final IntBuffer ControlRegsDword;
	

	public static final ByteBuffer AicaRegsArea;
	public static final ShortBuffer AicaRegsAreaWord;
	public static final IntBuffer AicaRegsAreaDword;
	
	
	public static final ByteBuffer CacheArea;
	public static final ShortBuffer CacheAreaWORD;
	public static final IntBuffer CacheAreaDWORD;

	
	private static final int videoArea = 1;

	private static final int biosArea = 2;
	
	private static final int ramArea = 3;

	private static final int flashArea = 4;
	
	private static final int soundArea = 5;
	
	private static final int STOREQUEUES = 6;
	
	private static final int PVR = 7;
	
	private static final int RTC = 8;
	
	private static final int AICA_REGS = 9;
	
	private static final int regmapID = 10;
	
	private static final int HashSize = 65536;
	
		
	/* 0xa05f6c04 */
	public static int  MAPLE_DMAADDR = (Memory.getMemoryAddress(0xa05f6c04) & 0xFFFF) >>> 2 ;
	
	/* 0xa05f6c10 */
	public static int MAPLE_RESET2 =(Memory.getMemoryAddress(0xa05f6c10) & 0xFFFF) >>> 2 ;
	
	/* 0xa05f6c14 */
	public static int MAPLE_ENABLE = (Memory.getMemoryAddress(0xa05f6c14) & 0xFFFF) >>> 2;
	
	/* 0xa05f6c18 */
	public static int MAPLE_STATE = (Memory.getMemoryAddress(0xa05f6c18) & 0xFFFF) >>> 2;
	
	/* 0xa05f6c80 */
	public static int MAPLE_SPEED = (Memory.getMemoryAddress(0xa05f6c80) & 0xFFFF) >>> 2;
	
	/* 0xa05f6c8c */
	public static int MAPLE_RESET1 = (Memory.getMemoryAddress(0xa05f6c8c) & 0xFFFF) >>> 2;;
	
	/*
	 * 0x1 when the G2 BUS is in USE
	 */
	private static int g2_fifo;
	
	private static int AICA_FIFO = 0x1;
	
	public static final int ASIC_ACK_A =  (Memory.getMemoryAddress(0xa05f6900) & 0xFFFF) >>> 2;  //	SB_ISTNRM	normal interrupt status

	public static final int ASIC_ACK_B =  (Memory.getMemoryAddress(0xa05f6904) & 0xFFFF) >>> 2;	//SB_ISTEXT	external interrupt status

	public static final int ASIC_ACK_C	 = (Memory.getMemoryAddress(0xa05f6908) & 0xFFFF) >>> 2;//	SB_ISTERR	error interrupt status

	public static final int ASIC_IRQD_A	 =( Memory.getMemoryAddress(0xa05f6910) & 0xFFFF) >>> 2;	//SB_IML2NRM	Level-2 normal interrupt mask control

	public static final int ASIC_IRQD_B	 =( Memory.getMemoryAddress(0xa05f6914) & 0xFFFF) >>> 2;	//SB_IML2EXT	Level-2 external interrupt mask control

	public static final int ASIC_IRQD_C	 =( Memory.getMemoryAddress(0xa05f6918) & 0xFFFF) >>> 2;

	public static final int ASIC_IRQB_A	 =( Memory.getMemoryAddress(0xa05f6920) & 0xFFFF) >>> 2;	//SB_IML4NRM	Level-4 normal interrupt mask control

	public static final int ASIC_IRQB_B = ( Memory.getMemoryAddress(0xa05f6924) & 0xFFFF)>>> 2;	//SB_IML4EXT	Level-4 external interrupt mask control

	public static final int ASIC_IRQB_C = ( Memory.getMemoryAddress(0xa05f6928) & 0xFFFF)>>> 2;

	public static final int ASIC_IRQ9_A	 =( Memory.getMemoryAddress(0xa05f6930) & 0xFFFF)>>> 2;	//SB_IML6NRM	Level-6 normal interrupt mask control

	public static final int ASIC_IRQ9_B = ( Memory.getMemoryAddress(0xa05f6934) & 0xFFFF)>>> 2;	//SB_IML6EXT	Level-6 external interrupt mask control

	public static final int ASIC_IRQ9_C = ( Memory.getMemoryAddress(0xa05f6938) & 0xFFFF)>>> 2;

	public static final int SB_PDTNRM  =( Memory.getMemoryAddress(0x005F6940) & 0xFFFF)>>> 2;	//PVR-DMA trigger select from normal interrupt

	public static final int	SB_PDTEXT  =( Memory.getMemoryAddress(0x005F6944) & 0xFFFF)>>> 2;		//PVR-DMA trigger select from external interrupt

	// we keep this one so the have a reference to the ByteBuffer
//	private static final ByteBuffer volatileScrapBuffer;
	
	private static final int mem_zone_hash [];
	
	
	// BIOS
	
	private static final int HACK_BASE	=	0x8C000100;
	private static final int HACK_ROMFONT=	0x000;
	private static final int HACK_GDROM	=	0x100;
	private static final int HACK_SYSINFO=	0x200;
	private static final int HACK_FLASHROM=	0x300;
	private static final int HACK_UNKNOWN=	0x400;

	private static final int SYSCALL_SYSINFO	=	Memory.getMemoryAddress(0x8C0000B0);
	private static final int SYSCALL_ROMFONT	=	Memory.getMemoryAddress(0x8C0000B4);
	private static final int SYSCALL_FLASHROM =	Memory.getMemoryAddress(0x8C0000B8);
	private static final int SYSCALL_GDROM	=	Memory.getMemoryAddress(0x8C0000BC);
	private static final int SYSCALL_UNKNOWN=	Memory.getMemoryAddress(0x8C0000E0);
	
	
	public static final int _word_index(int address){
		return address >>> 1;
	}
	
	public static final int _dword_index(int address){
		return address >>> 2;
	}

	public static final Memory INSTANCE = new Memory();
	
	static {
		bios = ByteBuffer.allocate(biosSize);
		bios.order(ByteOrder.LITTLE_ENDIAN);
		biosViewDWORD = bios.asIntBuffer();
		biosViewWord = bios.asShortBuffer();
		
		
		flash = ByteBuffer.allocate(flashSize);
		flash.order(ByteOrder.LITTLE_ENDIAN);
		flashViewDWORD = flash.asIntBuffer();
		flashViewWord = flash.asShortBuffer();
					
		video = BufferUtils.createByteBuffer(videoSize);
		video.order(ByteOrder.LITTLE_ENDIAN);
		
		TextureMemory = video.asIntBuffer();
		videoViewWord = video.asShortBuffer();
				
		sound = BufferUtils.createByteBuffer(soundSize);
		sound.order(ByteOrder.LITTLE_ENDIAN);
		SamplesMemory = sound.asIntBuffer();
		soundViewWord = sound.asShortBuffer();
		
		mem = ByteBuffer.allocate(ramSize);
		mem.order(ByteOrder.LITTLE_ENDIAN);
		memViewDWORD = mem.asIntBuffer();
		memViewWord = mem.asShortBuffer();
		
		 
		regmap = ByteBuffer.allocate(regmapSize);
		regmap.order(ByteOrder.LITTLE_ENDIAN);
		
		bSQ0 = BufferUtils.createByteBuffer(32);
		bSQ0.order(ByteOrder.LITTLE_ENDIAN);
		SQ0 = bSQ0.asIntBuffer();
		SQ0ViewQWORD = bSQ0.asLongBuffer();
		
		bSQ1 = BufferUtils.createByteBuffer(32);
		bSQ1.order(ByteOrder.LITTLE_ENDIAN);
		SQ1 = bSQ1.asIntBuffer();
		SQ1ViewQWORD = bSQ1.asLongBuffer();
		
		mem_zone_hash = new int [HashSize];
		
		
		/* Keeps the system control regs, the pvr regs, the g1/g2 control regs, the gdrom regs */
		ControlRegs = ByteBuffer.allocate(0x10000);
		ControlRegs.order(ByteOrder.LITTLE_ENDIAN);
		
		ControlRegsWord = ControlRegs.asShortBuffer();
		ControlRegsDword = ControlRegs.asIntBuffer();
		
		// goes from 0xa0700000 to 0xa0710000
		AicaRegsArea = ByteBuffer.allocate(0x10000);
		AicaRegsArea.order(ByteOrder.LITTLE_ENDIAN);
		AicaRegsAreaWord = AicaRegsArea.asShortBuffer();
		AicaRegsAreaDword = AicaRegsArea.asIntBuffer();
		
		CacheArea = ByteBuffer.allocate(32*1024); // 32ks ?
		CacheAreaWORD = CacheArea.asShortBuffer();
		CacheAreaDWORD = CacheArea.asIntBuffer();
		
		
		/*
		 * BIOS HACKS FROM DCEMU (Thank you Mr Ivan Toledo ^_^)
		 */
		
	 	int dwvalor = HACK_BASE + HACK_ROMFONT;	memViewDWORD.put(_dword_index(SYSCALL_ROMFONT), dwvalor);
		dwvalor = HACK_BASE + HACK_GDROM;	memViewDWORD.put(_dword_index(SYSCALL_GDROM), dwvalor);
	 	dwvalor = HACK_BASE + HACK_SYSINFO; memViewDWORD.put(_dword_index(SYSCALL_SYSINFO) , dwvalor);
	 	dwvalor = HACK_BASE + HACK_FLASHROM; memViewDWORD.put(_dword_index(SYSCALL_FLASHROM) , dwvalor);
	 	dwvalor = HACK_BASE + HACK_UNKNOWN; memViewDWORD.put(_dword_index(SYSCALL_UNKNOWN) , dwvalor);

//		vamos a hacer esto:
//			HACK_BASE       :	RTS
//			HACK_BASE + 2	:	MOV.L (disp*4 + PC & 0xFFFFFFFC + 4), R0
//	 		HACK_BASE + 4	:	<POSICION DEL FONT EN LA MEMORIA>
	 
	 	short wvalor = 0x000B;		memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_ROMFONT )), wvalor);
	 	wvalor =(short) 0xD000;		memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_ROMFONT +2)), wvalor);

	 	memViewDWORD.put(_dword_index(getMemoryAddress(HACK_BASE + HACK_ROMFONT + 4 )),Bios.DREAMCAST_ROMFONT_BASE);
	 	
	 	
		wvalor = 0x000B; /* RTS */			memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_GDROM)),wvalor);
		wvalor = (short)0xFFFF; /* BIOS_HACK*/		memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_GDROM + 2)), wvalor);

		wvalor = 0x000B;					memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_SYSINFO)), wvalor);
		wvalor = 0x0009;					memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_SYSINFO + 2)),wvalor);

		wvalor = 0x000B;					memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_FLASHROM)),wvalor);
		wvalor = 0x0009;					memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_FLASHROM + 2)),wvalor);

		wvalor = 0x000B;					memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_UNKNOWN)),wvalor);
		wvalor = 0x0009;					memViewWord.put(_word_index(getMemoryAddress(HACK_BASE + HACK_UNKNOWN + 2)), wvalor);
	}
	
	
	public static void setUpMemoryZones(){
		int i;
		for(i = 0;i < biosSize >>> 16; i++)
			 mem_zone_hash[i + 0x0000] = biosArea;
		for(i = 0;i < biosSize >>> 16; i++)
			 mem_zone_hash[i + 0x8000] = biosArea;
		for(i = 0;i < biosSize >>> 16; i++)
			 mem_zone_hash[i + 0xA000] = biosArea;
		
		
		for(i = 0; i < flashSize >> 16;  i++)
			 mem_zone_hash[i + 0x0020] = flashArea;
		for(i = 0;i < flashSize >> 16; i++)
			 mem_zone_hash[i + 0xa020] = flashArea;
		
		
		for(i =0; i < ramSize >> 16; i++)
			  mem_zone_hash[i + 0x0c00] = ramArea;		
		for(i =0; i < ramSize >> 16; i++)
			  mem_zone_hash[i + 0xAc00] = ramArea;
		for(i =0; i < ramSize >> 16; i++)
			  mem_zone_hash[i + 0x8c00] = ramArea;
		for(i =0; i < ramSize >> 16; i++)
			  mem_zone_hash[i + 0x0e00] = ramArea;
		for(i =0; i < ramSize >> 16; i++)
			  mem_zone_hash[i + 0x7e00] = ramArea;
		for(i =0; i < ramSize >> 16; i++)
			  mem_zone_hash[i + 0x8e00] = ramArea;
		
		
		for(i = 0;i < soundSize >> 16; i++)
			 mem_zone_hash[i + 0x0080] = soundArea;
		for(i = 0;i < soundSize >> 16; i++)
			 mem_zone_hash[i + 0xA080] = soundArea;

		
		
		for (i=0; i < videoSize >>> 16; i++)
			 mem_zone_hash[i + 0x0400] = videoArea;
		for (i=0; i < videoSize >>> 16; i++)
			 mem_zone_hash[i + 0xa400] = videoArea;
		for (i=0; i < videoSize >>> 16; i++)
			 mem_zone_hash[i + 0x0500] = videoArea;
		for (i=0; i < videoSize >>> 16; i++) 
			mem_zone_hash[i + 0xa500] = videoArea;

		/*
		 * 
		 * A write to the SQs can be performed using a store instruction for addresses H'E000 0000 to
		 *	H'E3FF FFFC in the P4 area.
		 *	
		 *  From the sh4 manual page 202
		 * 
		 */
		
		for (i=0; i<0x0400; i++)
			 mem_zone_hash[i + 0xE000] = STOREQUEUES;

		for (i=0; i<0x0001; i++)
			 mem_zone_hash[i + 0x005F] = PVR;
		
		for (i=0; i<0x0001; i++) 
			mem_zone_hash[i + 0xA05F] = PVR;

		for (i=0; i<0x0001; i++) 
			mem_zone_hash[i + 0xa070] = AICA_REGS;

		for (i=0; i<0x0001; i++) 
			mem_zone_hash[i + 0xa071] = RTC;

		for (i=0; i< regmapSize >> 16; i++)
			 mem_zone_hash[i + 0x1F00] = regmapID;
		
		for (i=0; i< regmapSize >> 16; i++)
			 mem_zone_hash[i + 0xFF00] = regmapID;
	}
	

	/*
	 * Info from http://mc.pp.se/dc/memory.html 
	 * 
	 * Returns if we should check if the mmu is active
	 */
	
	/* cache
	 *  
	 *  P ALT NC
	 *  0  X  X  0x00000000-0x7FFFFFFF Address translation through MMU (if enabled). Can be accessed in both User and Privileged mode.  U0/P0
  	 *	1  0  0  0x80000000-0x9FFFFFFF  Privileged mode only.  P1
  	 *	1  0  1  0xA0000000-0xBFFFFFFF  Privileged mode only, no cache.  P2
  	 *	1  1  0  0xC0000000-0xDFFFFFFF  Privileged mode only. Address translation through MMU (if enabled).  P3
  	 *	1  1  1  0xE0000000-0xFFFFFFFF  Privileged mode only. Internal I/O register access. No cache.  P4
	 *  
	 *  
	 *  
	 */
	
	private static final boolean CheckMMU(int address){
		final int logical =  ((address >> 29) & 0x3);
		return (logical < 0x3 || logical == 0x6);
	}
	
	/*
	 *  Get address for memory areas
	 *  Its 0x00ffffff because we don't want the ID's of the areas..
	 *  but the address in a given area
	 */
	
	public static final int getMemoryAddress(int address){
		return address & 0x00ffffff;
	}
	
	/*
	 * the mapping of the sound mem when accessed from the G2 bus is different
	 * from the real address of the sound mem we should write to
	 */
	public static final int translateSoundAddress(int address){
		return address & 0x1ffff;
	}

	private static final int getMemoryZone(int x){
		return ((x >>> 16)); 
	}
	
	public static final void  sqWriteToMemory(int address,int s){
		final int addr = getMemoryAddress(address);
		final int area = getMemoryZone(address);
		if (s !=0){
			switch(area){
			case videoArea:
					video.position(addr);
					video.put(bSQ0);
				break;
			case soundArea:
					sound.position(addr);
					sound.put(bSQ0);
				break;
			default:
				mem.position(addr);
				mem.put(bSQ0);
			}
		}
		else{
			switch(area){
			case videoArea:
					video.position(addr);
					video.put(bSQ1);
				break;
			case soundArea:
					sound.position(addr);
					sound.put(bSQ1);
				break;
			default:
				mem.position(addr);
				mem.put(bSQ1);
			}		
		}
	}
	
	/*
	 * returns an unsigned byte
	 */
	
	public static void write8(int address,byte val) {
		switch(mem_zone_hash[getMemoryZone(address)]){
			case ramArea:
				mem.put(getMemoryAddress(address), val);
			break;
			case videoArea:
				video.put(getMemoryAddress(address), val);
			break;
			case flashArea:
				flash.put(getMemoryAddress(address), val);
			break;
			case soundArea:
				sound.put(getMemoryAddress(address), val);
			break;
			case regmapID:
				regmapWritehandle8(getMemoryAddress(address), val);
			break;
			case PVR:
				pvrWritehandle8(getMemoryAddress(address),val);
			break;
			default :
			int ccr = regmap.getInt(MMREG.CCR);
			// if we get here we're reading from the cache
			if((ccr & 0x20)!=0)
			{
				if((ccr & 0x80) !=0)
				{
					CacheArea.put(((address >> 13) & 0x1000) + (address & 0x0fff),val);
				}
				else
				{
					CacheArea.put(((address >> 1 ) & 0x1000) + (address & 0x0fff),val);
				}
			}
		}
	}

	@Override
	public int read32i(int i) {
		return read32(i);
	}

	@Override
	public void write16i(int register, int register1) {
		write16(register, register1);
	}

	@Override
	public void write32i(int register, int register1) {
		write32(register, register1);
	}

	@Override
	public int read8i(int register) {
		return read8(register);
	}

	@Override
	public int read16i(int register) {
		return read16(register);
	}

	@Override
	public void write8i(int register, byte register1) {
		write8(register, register1);
	}

	@Override
	public void sqWriteTomemoryInst(int addr, int i) {
		Memory.sqWriteToMemory(addr, i);
	}

	@Override
	public void regmapWritehandle32Inst(int tra, int i) {
		regmapWritehandle32(tra, i);
	}

	@Override
	public void read64i(int register, float[] fRm, int i) {
		read64(register, fRm, i);
	}

	@Override
	public void write64i(int i, float[] fRm, int i1) {
		write64(i, fRm, i1);
	}

	@Override
	public int regmapReadhandle32i(int qacr0) {
		return regmapReadhandle32(qacr0);
	}

	public static void write16(int address,int val) {
		//Logger.log(Logger.MEM,"WORD WRITE @"  + Integer.toHexString(address));
		switch(mem_zone_hash[getMemoryZone(address)]){
		case ramArea:
			memViewWord.put(_word_index(getMemoryAddress(address)),(short) val);
		break;
		case videoArea:
			videoViewWord.put(_word_index(getMemoryAddress(address)),(short) val );
		break;
		case flashArea:
			System.out.println("Writing to flash memory @" + Integer.toHexString(address));
			flashViewWord.put(_word_index(getMemoryAddress(address)), (short) val );
		break;
		case soundArea:
			soundViewWord.put(_word_index(translateSoundAddress(getMemoryAddress(address))),(short) val);
		break;
		case PVR:
			pvrWritehandle16(getMemoryAddress(address), val);
		break;
		case regmapID:
			regmapWritehandle16(getMemoryAddress(address), val);
		break;
		default:
		int ccr = regmap.getInt(MMREG.CCR);
		// if we get here we're reading from the cache
		if((ccr & 0x20)!=0)
		{
			if((ccr & 0x80) !=0)
			{
				CacheAreaWORD.put((((address >> 13) & 0x1000) + (address & 0x0fff)) >>> 1,(short)val);
			}
			else
			{
				CacheAreaWORD.put((((address >> 1 ) & 0x1000) + (address & 0x0fff)) >>> 1,(short)val);
			}
		}
	}
	}
	
	public static void write32(int address,int val) {
		//Logger.log(Logger.MEM,"DWORD WRITE @"  + Integer.toHexString(address));
			switch(mem_zone_hash[getMemoryZone(address)]){
			case ramArea:
				memViewDWORD.put(_dword_index(getMemoryAddress(address)), val);
			break;
			case videoArea:
				TextureMemory.put(_dword_index(getMemoryAddress(address)), val);
			break;
			case flashArea:
				flashViewDWORD.put(_dword_index(getMemoryAddress(address)),val);
			break;
			case biosArea:
				System.out.println("bios written 4 @ " + Integer.toHexString(address));
				biosViewDWORD.put(_dword_index(getMemoryAddress(address)),val);
			break;
			case soundArea:
				g2_fifo |= AICA_FIFO;
				SamplesMemory.put(_dword_index(translateSoundAddress(getMemoryAddress(address))),val);
			break;
			case PVR:
				pvrWritehandle32(getMemoryAddress(address) , val);
			break;
			case STOREQUEUES:
				sqWritehandle32(getMemoryAddress(address), val);
			break;
			case regmapID:
				regmapWritehandle32(getMemoryAddress(address), val);
			break;
			default:
			int ccr = regmap.getInt(MMREG.CCR);
			// if we get here we're reading from the cache
			if((ccr & 0x20)!=0)
			{
				if((ccr & 0x80) !=0)
				{
					CacheAreaDWORD.put((((address >> 13) & 0x1000) + (address & 0x0fff)) >>> 2,val);
				}
				else
				{
					CacheAreaDWORD.put((((address >> 1 ) & 0x1000) + (address & 0x0fff)) >>> 2,val);
				}
			}
		}
}
	

	public static void write64(int address,float [] src,int index) {
		//Logger.log(//Logger.MEM,"QWORD WRITE @"  + Integer.toHexString(address));
		switch(mem_zone_hash[getMemoryZone(address)]){
		case ramArea:
			//	ramWritehandle64(address, val);
			break;
		case videoArea:
			//	videoWritehandle64(address, val);
			break;
		case STOREQUEUES:
			//sqWritehandle64(address, val);
			break;
		}
	}
	
	public static void write64(int address,long val,int index){
		switch(mem_zone_hash[getMemoryZone(address)]){
		case ramArea:
			//	ramWritehandle64(address, val);
			break;
		case videoArea:
			//	videoWritehandle64(address, val);
			break;
		case STOREQUEUES:
			//sqWritehandle64(address, val);
			break;
		default:
			// if we get here we're reading from the cache
			System.out.println("Error @" + Integer.toHexString(address));
		}
	}
	
	
	public static int read8(int address) {
		//Logger.log(Logger.MEM,"READ BYTE @"  + Integer.toHexString(address));
		int area = mem_zone_hash[getMemoryZone(address)];
		switch(area){
		case ramArea:
			return (mem.get(getMemoryAddress(address)) & 0xFF);
		case videoArea:
			return (video.get(getMemoryAddress(address))& 0xFF);
		case PVR:
			return (pvrReadhandle8(getMemoryAddress(address))& 0xFF);
		case flashArea:
			return (flash.get(getMemoryAddress(address))& 0xFF);
		case biosArea:
			Logger.log(Logger.MEM,"READ BYTE @"  + Integer.toHexString(address) + " value " + Integer.toHexString(bios.get(getMemoryAddress(address)) & 0xFF) + "\n");
			return (int)(bios.get(getMemoryAddress(address)) & 0xFF);
		case soundArea:
			return (sound.get(getMemoryAddress(address))& 0xFF);
		case regmapID:
			return (regmapReadhandle8(getMemoryAddress(address))& 0xFF);
		default:
			System.out.println("read byte @" + Integer.toHexString(address));
			area  =0;
			int ccr = regmap.getInt(MMREG.CCR);
			// if we get here we're reading from the cache
			if((ccr & 0x20)!=0)
			{
				if((ccr & 0x80) !=0)
				{
					area = CacheArea.get(((address >> 13) & 0x1000) + (address & 0x0fff));
				}
				else
				{
					area = CacheArea.get(((address >> 1 ) & 0x1000) + (address & 0x0fff));
				}
			}
						
			return area;
		}
	}
	
	public static int read16(int address) {
		//Logger.log(Logger.MEM,"READ WORD @"  + Integer.toHexString(address));
		switch(mem_zone_hash[getMemoryZone(address)]){
		case ramArea:
			return	memViewWord.get(_word_index(getMemoryAddress(address))) & 0xFFFF;
		case videoArea:
			return videoViewWord.get(_word_index(getMemoryAddress(address))) & 0xFFFF;
		case PVR:
			return pvrReadhandle16(getMemoryAddress(address));
		case regmapID:
			return regmapReadhandle16(getMemoryAddress(address));
		case flashArea:
			return flashViewWord.get(_word_index(getMemoryAddress(address))) & 0xFFFF;
		case biosArea:
			System.out.println("BIOS READ WORD " + Integer.toHexString(address));
			return biosViewWord.get(_word_index(getMemoryAddress(address)))& 0xFFFF;
		case soundArea:
			return soundViewWord.get(_word_index(getMemoryAddress(address))) & 0xFFFF;
		default:
			// if we get here we're reading from the cache
			System.out.println("read word @" + Integer.toHexString(address));
			int area  =0;
			int ccr = regmap.getInt(MMREG.CCR);
			// if we get here we're reading from the cache
			if((ccr & 0x20)!=0)
			{
				if((ccr & 0x80) !=0)
				{
					area = CacheAreaWORD.get((((address >> 13) & 0x1000) + (address & 0x0fff)) >>> 1);
				}
				else
				{
					area = CacheAreaWORD.get((((address >> 1 ) & 0x1000) + (address & 0x0fff)) >>> 1);
				}
			}
			return area;
		}
	}
	
	public static int read32(int address) {
		//Logger.log(Logger.MEM,"READ DWORD @\n"  + Integer.toHexString(address));
		switch(mem_zone_hash[getMemoryZone(address)]){
		case ramArea:
			return memViewDWORD.get(_dword_index(getMemoryAddress(address)));
		case videoArea:
			return TextureMemory.get(_dword_index(getMemoryAddress(address)));
		case PVR:
			return pvrReadhandle32(getMemoryAddress(address));
		case regmapID:
			return regmapReadhandle32(getMemoryAddress(address));
		case flashArea:
			return flashViewDWORD.get(_dword_index(getMemoryAddress(address)));
		case biosArea:
			//System.out.println("BIOS READ DWORD " + "ADRESS: "+ Integer.toHexString(address) +" value " +  Integer.toHexString(biosViewDWORD.get(getMemoryAddress(address) >>> 2)));
			return biosViewDWORD.get(_dword_index(getMemoryAddress(address & 0x3FFFFF)));
		case soundArea:
			return SamplesMemory.get(_dword_index(translateSoundAddress(getMemoryAddress(address))));
		case RTC:
			switch(address & 0xf){
				case 0:
					return 0;
				case 4:
					return 0;
			}
		case AICA_REGS:
			int dir = address & 0xFFFF; 
			if(dir == 0x20c)
					return AicaRegsAreaDword.get(_dword_index(address & 0xFFFF)) | cable;
			else return AicaRegsAreaDword.get(_dword_index(dir));
		default :
			// if we get here we're reading from the cache
			
			// if we get here we're reading from the cache
			System.out.println("read dword @" + Integer.toHexString(address));
			int area  =0;
			int ccr = regmap.getInt(MMREG.CCR);
			System.out.println("CCR " + Integer.toHexString(ccr));
			// if we get here we're reading from the cache
			if((ccr & 0x20)!=0)
			{
				if((ccr & 0x80) !=0)
				{
					area = CacheAreaDWORD.get((((address >> 13) & 0x1000) + (address & 0x0fff)) >>> 2);
				}
				else
				{
					area = CacheAreaDWORD.get((((address >> 1 ) & 0x1000) + (address & 0x0fff)) >>> 2);
				}
			}
	
			return area;
			}	
		}
	
	public static long read64(int address) {
		//Logger.log(//Logger.MEM,"READ DWORD @"  + Integer.toHexString(address));
		switch(mem_zone_hash[getMemoryZone(address)]){
		case ramArea:
			return ramReadhandle64(getMemoryAddress(address) >>> 3);
		case videoArea:
			return videoReadhandle64(getMemoryAddress(address) >>> 3);
		case PVR:
			return pvrReadhandle64(getMemoryAddress(address) >>> 3);
		default:
			System.out.println("Error @" + Integer.toHexString(address));
			return 0;
	}
	}
	
	/*
	 * this one is a bit special.
	 * It is a specific implementation for FMOV of 64 bits
	 */
	
	public static final void read64(int address,float [] destination, int index){
		long v=0;
		v = Memory.read64(address);
		destination[index] = (float) (v  & 0xFFFFFFFF);
		destination[index+1] = (float) ((v >> 32)  & 0xFFFFFFFF);
		
	} 
	
	/* *************** Implementation of the memory write interfaces ************** */
	

		public static final void ramWritehandle64(int address,long val) {
			mem.putLong(address, val);
		}
	
	
	
	// done	
		public static final void regmapWritehandle16(int address,int val) {
			regmap.putShort(address,(short) val );
			if(address == 0xe80010){
				int SCFSR2_read = regmapReadhandle16(MMREG.SCFSR2);
				SCFSR2_read |= MMREG.SCFSR2_TDFE;
				SCFSR2_read |= MMREG.SCFSR2_TEND;
				regmap.putShort(0xe80010,(short) (SCFSR2_read & 0xFFFF));
			}
		}

		
		public static final void regmapWritehandle32(int address,int val) {
			regmap.putInt(address,val);			
		}

		
		public static  final void regmapWritehandle8(int address,byte val) {
			regmap.put(address, val);
			System.out.println("SERIAL:" + (char)(val & 0xff));
			if(address == MMREG.SCFTDR2){
				Logger.serialLog((char)(val & 0xff));
			}
		}

	
			
		public static final void sqWritehandle32(int address,int val) {
			int pos = (address >> 2) & 7;
			System.out.println("SQ Written at pos " + pos);
			if ((address & 0x20) !=0) // 0: SQ0, 1: SQ1
		    {
		    	SQ1.put(pos, val);
		    }
		    else
		    {
		    	SQ0.put(pos, val);
		    }
		}

		// we shift by 8 because bSQ[num] is index in bytes
		public static final void sqWritehandle64(int address,long val) {
			int pos = (address >> 2) & 7;
		    if ((address & 0x20) !=0) // 0: SQ0, 1: SQ1
		    {
		    	SQ1ViewQWORD.put(pos >> 3, val);
		    }
		    else
		    {
		    	SQ0ViewQWORD.put(pos >> 3, val);
		    }
		}
	
	
		private static final void pvrWritehandle16(int address,int val) {
			System.out.println("PVR WRITE 16");
			ControlRegsWord.put(_word_index((address & 0xFFFF)), (short) val);
		}

		
		private static final void pvrWritehandle32(int address,int val) {
			System.out.println("PVR WRITE: " + Integer.toHexString(address) + "val " + Integer.toHexString(val));
			if((address & 0x007fffff) >= 0x005f8000){
				PowerVR.gpuInputCommand(address & 0x1fff, val);
				return;
			}

			switch (address & 0x007fffff)
			{
				case 0x005f6900:	
					//System.out.println("Called to clear interrupt with val" + Integer.toHexString(val));
					ControlRegsDword.put(ASIC_ACK_A,ControlRegsDword.get(ASIC_ACK_A) & ~val);				
				return;
			    case 0x005f6904:
			    	ControlRegsDword.put(ASIC_ACK_B,ControlRegsDword.get(ASIC_ACK_C) & ~val);	
			    return;
				
				case 0x005f6908:	
					ControlRegsDword.put(ASIC_ACK_C,ControlRegsDword.get(ASIC_ACK_C) & ~val);	
				return;

				case 0x005f6808:	
					if(val == 1)
						DMA.PvrDma();
				return;

				// MAPLE State
				case 0x005f6c18:
					//System.out.println("MAPLE STATE WRITE PC " + Integer.toHexString(Emu.sh4cpu.PC));
					ControlRegsDword.put(MAPLE_STATE, val);
					if((val & 0x1) !=0){
						DMA.MapleDMA();
						ControlRegsDword.put(MAPLE_STATE, 0);
					}											
				return;

				case 0x005f7414:
				case 0x005f7418:	
					ControlRegsDword.put(_dword_index(address & 0xFFFF),val);
					DMA.GdRomDma();
				return;

				case 0x005f7814:
				case 0x005f7818:	
					ControlRegsDword.put(_dword_index(address & 0xFFFF),val);
					DMA.AicaDma(0);
				return;

				case 0x005f7834:
				case 0x005f7838:	
					ControlRegsDword.put(_dword_index(address & 0xFFFF),val);
					DMA.AicaDma(1);
				return;

				case 0x005f7854:
				case 0x005f7858:	
					ControlRegsDword.put(_dword_index(address & 0xFFFF),val);
					DMA.AicaDma(2);
				return;

				case 0x005f7874:
				case 0x005f7878:
					ControlRegsDword.put(_dword_index(address & 0xFFFF),val);
					DMA.AicaDma(3);
				return;

				default:
					ControlRegsDword.put(_dword_index(address & 0xFFFF),val);
				return;
			}
		}

		
		private static final void pvrWritehandle8(int address,byte val) {
			System.out.println("PVR WRITE 8");
			ControlRegs.put((address & 0xFFFF), val);
		}
	
	/* *************** Implementation of the memory read interfaces ************** */
	
	//done 
		
		
		private static final long videoReadhandle64(int address) {
			return video.getLong(address);
		}
		
		private static final long ramReadhandle64(int address) {
			return mem.getLong(address);
		}
	

	// done
		public static final int regmapReadhandle16(int address) {
			return (int)(regmap.getShort(address) & 0xFFFF);
		}

		
		public static final int regmapReadhandle32(int address) {
			return regmap.getInt(address);
		}

	@Override
	public IntBuffer getSQ0() {
		return SQ0;
	}

	@Override
	public IntBuffer getSQ1() {
		return SQ1;
	}


	public static final int regmapReadhandle8(int address) {
			return (int)regmap.get(address) & 0xFF;
		}

	
		private static final int pvrReadhandle16(int address) {
			return ControlRegsWord.get(_word_index(address & 0xFFFF)) & 0xFFFF;
		}

		
		private static final int pvrReadhandle32(int address) {
			int ret =0;
			if((address & 0x007fffff) >= 0x005f8000)
			{
				return PowerVR.gpuReadAcess(address & 0x1fff);
			}

			switch (address & 0x007fffff)
			{
				case 0x005f6880: return 0x8; 
				case 0x005f688c: 
					ret = g2_fifo;
					g2_fifo &= ~AICA_FIFO;
					return ret;
				case 0x005f689c: return 0xb;
				case 0x005f6c18:
						//System.out.println("MAPLE READ PC " + Integer.toHexString(Emu.sh4cpu.PC));
						return ControlRegsDword.get(MAPLE_STATE);
				default:
					 	return ControlRegsDword.get(_dword_index(address & 0xFFFF));
			}
		
		}

		
		private static final int pvrReadhandle8(int address) {
			return ControlRegs.get((address & 0xFFFF)) & 0xFF;
		}

		
		private static final long pvrReadhandle64(int address) {
			return 0;
		}
}
