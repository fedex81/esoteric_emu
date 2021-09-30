package sh4;
import memory.Memory;
/*
 *  Memory mapped regs present on the dreamcast
 *  This class contains the handlers for the TMU and INTC
 */
public final class MMREG {

	// taken from mame  not used yet
	private static final int tcnt_div [] = { 4, 16, 64, 256, 1024, 1, 1, 1 };
/*
	PTEL	= (DWORD *) &regmem[0x000004];	*PTEL = 0;
	CCR		= (DWORD *)	&regmem[0x00001C];	*CCR = 0;
	EXPEVT	= (DWORD *)	&regmem[0x000024];	*EXPEVT = 0;
	INTEVT	= (DWORD *) &regmem[0x000028];	*INTEVT = 0;
	TRA		= (DWORD *) &regmem[0x000020];
	QACR0	= (DWORD *)	&regmem[0x000038];	*QACR0 = 0;
	QACR1   = (DWORD *) &regmem[0x00003C];  *QACR1 = 0;


	PCTRA	= (DWORD *)	&regmem[0x80002C];	*PCTRA = 0;
	PDTRA	= (WORD *)	&regmem[0x800030];	*PDTRA = 0;
	PCTRB	= (DWORD *)	&regmem[0x800040];	*PCTRB = 0;
	PDTRB	= (WORD *)	&regmem[0x800044];	*PDTRB = 0;


	ICR		= (WORD *)	&regmem[0xD00000];		*ICR = 0;
	IPRA	= (WORD *)	&regmem[0xD00004];		*IPRA = 0;
	IPRB	= (WORD *)	&regmem[0xD00008];		*IPRB = 0;
	IPRC	= (WORD *)	&regmem[0xD0000C];		*IPRC = 0;

	TOCR	= (BYTE *)	&regmem[0xD80000];		*TOCR = 0;
	TSTR	= (BYTE *)	&regmem[0xD80004];		*TSTR = 0;
	TCOR0	= (DWORD *)	&regmem[0xD80008];		*TCOR0 = 0xFFFFFFFF;
	TCNT0	= (DWORD *)	&regmem[0xD8000C];		*TCNT0 = 0xFFFFFFFF;
	TCR0	= (WORD *)	&regmem[0xD80010];		*TCR0 = 0;
	TCOR1	= (DWORD *)	&regmem[0xD80014];		*TCOR1 = 0xFFFFFFFF;
	TCNT1	= (DWORD *)	&regmem[0xD80018];		*TCNT1 = 0xFFFFFFFF;
	TCR1	= (WORD *)	&regmem[0xD8001C];		*TCR1 = 0;
	TCOR2	= (DWORD *)	&regmem[0xD80020];		*TCOR2 = 0xFFFFFFFF;
	TCNT2	= (DWORD *)	&regmem[0xD80024];		*TCNT2 = 0xFFFFFFFF;
	TCR2	= (WORD *)	&regmem[0xD80028];		*TCR2 = 0;
	TCPR2	= (DWORD *)	&regmem[0xD8002C];
	
	SCSMR2	= (WORD *)	&regmem[0xE80000];		*SCSMR2 = 0;
	SCBRR2	= (BYTE *)	&regmem[0xE80004];		*SCBRR2 = 0xFF;
	SCSCR2	= (WORD *)	&regmem[0xE80008];		*SCSCR2 = 0;
	SCFTDR2	= (BYTE *)	&regmem[0xE8000C];		*SCFTDR2 = 0;
	SCFSR2	= (WORD *)	&regmem[0xE80010];		*SCFSR2 = 0x60;
	SCFCR2	= (WORD *)	&regmem[0xE80018];		*SCFCR2 = 0;
	SCSPTR2	= (WORD *)	&regmem[0xE80020];		*SCSPTR2 = 0;
	SCLSR2	= (WORD *)	&regmem[0xE80024];		*SCLSR2 = 0;
*/
	// DWORD
	public static final int 	PTEL=0x000004;		// Page Table Entry Low register
	
	public static final int 	MMUCR=0x000010;		// Cache Control Register
	public static final int 	CCR=0x00001C;		// Cache Control Register
	public static final int 	QACR0=0x000038;		// Queue Address Control Register 0
	public static final int 	QACR1=0x00003C;		// Queue Address Control Register 0
	public static final int 	INTEVT=0x000028;		// Interrupt Event Register
	public static final int 	EXPEVT=0x000024;		// Exception Event Register
	public static final int 	TRA=0x000020;		// TRAPA exception register
		
	// WORD
	public static final int 	ICR=0xD00000;		// Interrupt Control Register
	public static final int 	IPRA=0xD00004;		// Interrupt Priority Register A
	public static final int 	IPRB=0xD00008;		// Interrupt Priority Register B
	public static final int 	IPRC=0xD0000C;		// Interrupt Priority Register C
	// end
	
	public static final int 	PCTRA=0x80002C;		// Port Control Register A
	public static final int 	PDTRA=0x800030;		// Port Data Register A
	public static final int 	PCTRB=0x800040;		// Port Control Register B
	public static final int 	PDTRB=0x800044;		// Port data Register B
	
	/*** DMA - all DWORD in size***/
	public static final int 	SAR0=0xA00000;		// DMA source address register 0
	public static final int 	DAR0=0xA00004;		// DMA destination address register 0
	public static final int 	DMATCR0=0xA00008;	// DMA transfer count register 0
	public static final int 	CHCR0=0xA0000C;		// DMA channel control register 0
	
	public static final int 	SAR1=0xA00010;		// DMA source address register 1
	public static final int 	DAR1=0xA00014;		// DMA destination address register 1
	public static final int 	DMATCR1=0xA00018;	// DMA transfer count register 1
	public static final int 	CHCR1=0xA0001C;		// DMA channel control register 1
	
	public static final int 	SAR2=0xA00020;		// DMA source address register 2
	public static final int 	DAR2=0xA00024;		// DMA destination address register 2
	public static final int 	DMATCR2=0xA00028;	// DMA transfer count register 2
	public static final int 	CHCR2=0xA0002C;		// DMA channel control register 2
	
	public static final int 	SAR3=0xA00030;		// DMA source address register 0
	public static final int 	DAR3=0xA00034;		// DMA destination address register 0
	public static final int 	DMATCR3=0xA00038;	// DMA transfer count register 0
	public static final int 	CHCR3=0xA0003C;		// DMA channel control register 0
	
	public static final int 	DMAOR=0xA00040;		// DMA operation register

	public static final int  DME	=	1;
	public static final int  DE	 =	1;
	/*** FIN DMA ***/

	/*** TMU ***/
	public static final int 	TOCR = 0xD80000;
	public static final int 	TSTR =0xD80004;
	public static final int 	TCOR0=0xD80008;
	public static final int 	TCNT0=0xD8000C;
	public static final int 	TCR0=0xD80010;
	public static final int 	TCOR1=0xD80014;
	public static final int 	TCNT1=0xD80018;
	public static final int 	TCR1=0xD8001C;
	public static final int 	TCOR2=0xD80020;
	public static final int 	TCNT2=0xD80024;
	public static final int 	TCR2=0xD80028;
	public static final int 	TCPR2=0xD8002C;

	public static final int SCSMR2	= 0xE80000;
	public static final int SCBRR2	= 0xE80004;
	public static final int SCSCR2	= 0xE80008;
	public static final int SCFTDR2	= 0xE8000C;
	public static final int SCFSR2	= 0xE80010;
	public static final int SCFCR2	= 0xE80018;
	public static final int SCSPTR2	= 0xE80020;
	public static final int SCLSR2	= 0xE80024;
	
	// BITFIELDS
	// SCSMR2
	public static final int CHR	=	(0x0040);
	public static final int PE	=	(0x0020);
	public static final int PM	=	(0x0010);
	public static final int STOP=	(0x0008);
	public static final int CKS1=	(0x0002);
	public static final int CKS0	=(0x0000);

	// SCSCR2
	public static final int TIE	=	(0x0080);
	public static final int RIE	=	(0x0040);
	public static final int TE	=	(0x0020);
	public static final int RE	=	(0x0010);
	public static final int REIE=	(0x0008);
	public static final int CKE1=	(0x0002);

	// SCFSR2
	public static final int SCFSR2_PER3	=(0x8000);
	public static final int SCFSR2_PER2	=(0x4000);
	public static final int SCFSR2_PER1=	(0x2000);
	public static final int SCFSR2_PER0	=(0x1000);
	public static final int SCFSR2_FER3	=(0x0800);
	public static final int SCFSR2_FER2	=(0x0400);
	public static final int SCFSR2_FER1	=(0x0200);
	public static final int SCFSR2_FER0	=(0x0100);
	public static final int SCFSR2_ER	=(0x0080);
	public static final int SCFSR2_TEND	=(0x0040);
	public static final int SCFSR2_TDFE	=(0x0020);
	public static final int SCFSR2_BRK	=(0x0010);
	public static final int SCFSR2_FER=	(0x0008);
	public static final int SCFSR2_PER	=(0x0004);
	public static final int SCFSR2_RDF=	(0x0002);
	public static final int SCFSR2_DR	=(0x0001);
	
	private Intc interruptController;
	
	static{
		Memory.regmapWritehandle32(TCOR0,0xFFFFFFFF);
		Memory.regmapWritehandle32(TCNT0,0xFFFFFFFF);
		Memory.regmapWritehandle32(TCOR1,0xFFFFFFFF);
		Memory.regmapWritehandle32(TCNT1,0xFFFFFFFF);
		Memory.regmapWritehandle32(TCOR2,0xFFFFFFFF);
		Memory.regmapWritehandle32(TCNT2,0xFFFFFFFF);
		
		// the other values are zero
		Memory.regmapWritehandle8(SCBRR2,(byte)0xff);
		Memory.regmapWritehandle16(SCFSR2, 0x60);
		
	}
	
	private static final int TMUCLOCK = 50*1000*1000; // 50 Mhz in hertz 
	
	private static int [] time_remaining;
	
	public MMREG(Intc interrupts){
		interruptController = interrupts;
	}
	
	static{
		time_remaining = new int[3];
	}
	
	
	
	public static final int  TMU_TCR_UNF	=	(1 << 8);	// 0x0100
	public static final int  TMU_TCR_UNIE	= (1 << 5);
	
	public void TMU(int cycles){
		// tmu control channels
		int tcnt=0;
		int tcr=0;
		
		int base_count=0;
		final int tstr = Memory.regmapReadhandle8(TSTR);
		//channel 2
	    if ((tstr & 4)!=0)
	    {
	    	base_count = tcnt = Memory.regmapReadhandle32(TCNT2);
	    	System.out.println(tcnt);
	    	
	    	/*
	    	 * The logic is if the clock on the dc is 200mhz and the tmu 50Mhz
	    	 * that means that a cycle of the sh4 maps to 4 on the tmu, this is,
	    	 * if the sh4 completes one cycle the tmu will have done a quarter of a cycle.
	    	 */
	    	tcr = Memory.regmapReadhandle16(TCR2);  	
	  
	    	tcnt = (cycles + time_remaining[2]) / (tcnt_div[tcr & 7]);
	    	// the same thing as above but we are interested in knowing how many cycles we will have to consider next time
	    	time_remaining[2] = (cycles + time_remaining[2]) % (tcnt_div[tcr & 7]);
	    	
	        if (base_count < tcnt) // underflow
	        {	
	        		System.out.println("TMU UNDERFLOW 2");
	                tcnt = Memory.regmapReadhandle32(TCOR2);
	                tcr |= TMU_TCR_UNF;
				//	if ((tcr & TMU_TCR_UNIE)!=0)
					//	interruptController.processSource(Intc.SOURCE_TMU2_UND);
					Memory.regmapWritehandle32(TCNT2,tcnt);
					Memory.regmapWritehandle32(TCR2,tcr);
	   	     }
   		     else
   		     {
					Memory.regmapWritehandle32(TCNT2,base_count-tcnt);
					 System.out.println("TCNT2 " + (base_count-tcnt));
			}
	   	 }
	   
	    // 1
	    if ((tstr & 2)!=0)
	    {
	    	base_count = tcnt =Memory.regmapReadhandle32(TCNT1);
	    	
	    	tcr = Memory.regmapReadhandle16(TCR1); 
	    	
	    	tcnt = (cycles + time_remaining[1]) / (tcnt_div[tcr & 7]);
	    	// the same thing as above but we are interested in knowing how many cycles we will have to consider next time
	    	time_remaining[1] = (cycles + time_remaining[1]) % (tcnt_div[tcr & 7]);
	    	
	        if (base_count < tcnt) // underflow
	        { 
	        	System.out.println("TMU UNDERFLOW 1");
	                tcnt =  Memory.regmapReadhandle32(TCOR1);
	                tcr |= TMU_TCR_UNF;
				//if ((tcr & TMU_TCR_UNIE)!=0)
					//interruptController.processSource(Intc.SOURCE_TMU1_UND);
				Memory.regmapWritehandle32(TCNT1,tcnt);
				Memory.regmapWritehandle32(TCR1,tcr);
	        }
	        else
   		     {
	        	Memory.regmapWritehandle32(TCNT1,base_count-tcnt);
	        	System.out.println("TCNT1 " + (base_count-tcnt));
			}
	    }
	    
	    //0
	    if ((tstr & 1) !=0)
	    {
	    	tcnt = Memory.regmapReadhandle32(TCNT0);
	    		    	
	    	base_count = Memory.regmapReadhandle32(TCNT0);
	    	
	    	//System.out.println("TMU 0 -> tcnt before " + base_count);
	    		    	
			tcr = Memory.regmapReadhandle16(TCR0);  
	    	tcnt = (cycles + time_remaining[0]) / (tcnt_div[tcr & 7]);
	    	
	    	//System.out.println("cycles " + cycles + " div " + tcnt_div[tcr & 7]);
	    	
	    	// the same thing as above but we are interested in knowing how many cycles we will have to consider next time
	    	time_remaining[0] = (cycles + time_remaining[0]) % (tcnt_div[tcr & 7]);
	    	
	    	//System.out.println("BaseCount " + base_count + " TCNT " + tcnt);
	        if (base_count < tcnt) // underflow
	        {
	                tcnt =  Memory.regmapReadhandle32(TCOR0);
	                tcr |= TMU_TCR_UNF;
				//if ((tcr & TMU_TCR_UNIE)!=0)
					//interruptController.processSource(Intc.SOURCE_TMU0_UND);
				Memory.regmapWritehandle32(TCNT0,tcnt);
				Memory.regmapWritehandle32(TCR0,tcr);
	        }
	        else
   		     {
	        	Memory.regmapWritehandle32(TCNT0,base_count-tcnt);
	        	// System.out.println("TCNT0 " + (base_count-tcnt));
			}
	    }
	   
	}
}
