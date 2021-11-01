package sh4;

import gui.Emu;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import powervr.TileAccelarator;

import memory.Memory;

public class CodeBlock {

	public static CodeBlock CurrentCodeBlock;
	
	private ArrayList<NativeOpcode> block;
	private boolean finished = false;
	private int cycles =0;
	int numberOfInstructions=0;
	
	public CodeBlock() {
		// blocks with more than 15 instructions are not captured
		block = new ArrayList<NativeOpcode>(15);
		CurrentCodeBlock = this;
	}
	
	public void executeBlock(){
		for(int i =0; i < block.size();i++)
			block.get(i).call();
	}
		
	public void addInstruction(int instruction, int first_arg, int second_arg, int third_arg){
		final int n = first_arg;
		final int m = second_arg;
		final int t = third_arg;
		final Sh4Context sh4cpu = Emu.getSh4cpu();
		if(!finished){
			switch (instruction) {
			case  NativeOpcode.MOVI : 
				block.add(new NativeOpcode(){
					public void call() {
						if ((m&0x80)==0) sh4cpu.registers[n]=(0x000000FF & m);
						else sh4cpu.registers[n] =(0xFFFFFF00 | m);				
					}				
				});
			cycles += 1;	
			break;
			case NativeOpcode.MOVWI :
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read16(m);
						
						if ((sh4cpu.registers[n]&0x8000)==0) sh4cpu.registers[n] &= 0x0000FFFF;
						else sh4cpu.registers[n] |= 0xFFFF0000;
					}
					
				});
				
			break;
			case  NativeOpcode.MOVLI:
				block.add(new NativeOpcode(){
					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read32(m);
					}
				
				});
				cycles += 1;
			break;
			case  NativeOpcode.MOV:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.registers[m];
					}
				
				});
				cycles += 1;
				
			break;
			case  NativeOpcode.MOVBS:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write8(sh4cpu.registers[n],(byte) (sh4cpu.registers[m] & 0xFF));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWS:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write16(sh4cpu.registers[n], (sh4cpu.registers[m] & 0xFFFF));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLS:			
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write32(sh4cpu.registers[n], sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVBL:		
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read8(sh4cpu.registers[m]);

						if ((sh4cpu.registers[n]&0x80)==0) sh4cpu.registers[n]&=0x000000FF;
						else sh4cpu.registers[n]|=0xFFFFFF00;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWL:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read16(sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLL:				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read32(sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVBM:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 1;
						Memory.write8(sh4cpu.registers[n],(byte) sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWM:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 2;

						Memory.write16(sh4cpu.registers[n], sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLM:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						
						Memory.write32(sh4cpu.registers[n], sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVBP:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read8(sh4cpu.registers[m]);
						if(n != m) sh4cpu.registers[m] += 1;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWP:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read16(sh4cpu.registers[m]);
						if(n != m) sh4cpu.registers[m] += 2;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLP:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read32(sh4cpu.registers[m]);
						if(n != m) sh4cpu.registers[m] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVBS4:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write8(sh4cpu.registers[n] + m ,(byte) sh4cpu.registers[0]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWS4:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write16(sh4cpu.registers[n] + (m << 1), sh4cpu.registers[0]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLS4:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write32(sh4cpu.registers[n] + (m << 2), sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVBL4:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[0] = Memory.read8(sh4cpu.registers[n] + (m << 0));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWL4:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[0] = Memory.read16(sh4cpu.registers[n] + (m << 1));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLL4:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read32(sh4cpu.registers[m] + (t << 2));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVBS0:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write8(sh4cpu.registers[n] + sh4cpu.registers[0],(byte) sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWS0:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write16(sh4cpu.registers[n] + sh4cpu.registers[0], sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLS0:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write32(sh4cpu.registers[n] + sh4cpu.registers[0], sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVBL0:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read8(sh4cpu.registers[m] + sh4cpu.registers[0]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWL0:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read16(sh4cpu.registers[m] + sh4cpu.registers[0]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLL0:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = Memory.read32(sh4cpu.registers[m] + sh4cpu.registers[0]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVBSG:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write8(sh4cpu.GBR + n ,(byte) sh4cpu.registers[0]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWSG:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write16(sh4cpu.GBR + (n << 1), sh4cpu.registers[0]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLSG:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write32(sh4cpu.GBR + (n << 2), sh4cpu.registers[0]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVBLG:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[0] = Memory.read8(sh4cpu.GBR + n);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVWLG:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[0] = Memory.read16(sh4cpu.GBR + (n << 1));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVLLG:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[0] = Memory.read32(sh4cpu.GBR + (n << 2));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVCAL:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						Memory.write32(sh4cpu.registers[n], sh4cpu.registers[0]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVA:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[0] = n;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MOVT:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = (sh4cpu.SR & sh4cpu.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SWAPB:
					block.add(new NativeOpcode(){

					public void call() {
						int temp0,temp1;
						temp0=sh4cpu.registers[m]&0xFFFF0000;
						temp1=(sh4cpu.registers[m]&0x000000FF)<<8;
						sh4cpu.registers[n]=(sh4cpu.registers[m]&0x0000FF00)>>8;
						sh4cpu.registers[n]=sh4cpu.registers[n]|temp1|temp0;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SWAPW:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						int temp=0;
						temp=(sh4cpu.registers[m]>>16)&0x0000FFFF;
						sh4cpu.registers[n]=sh4cpu.registers[m]<<16;
						sh4cpu.registers[n]|=temp;
					}
				
				});
				cycles += 1; break;
			case NativeOpcode.XTRCT:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = ((sh4cpu.registers[n] & 0xffff0000) >>> 16) |
						   ((sh4cpu.registers[m] & 0x0000ffff) << 16);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ADD:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] += sh4cpu.registers[m]; 
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ADDI:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if ((m&0x80)==0)
							sh4cpu.registers[n]+=(0x000000FF & m);
						else sh4cpu.registers[n]+=(0xFFFFFF00 | m);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ADDC:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						int tmp0 = sh4cpu.registers[n];
						int tmp1 = sh4cpu.registers[n] + sh4cpu.registers[m]; 
						sh4cpu.registers[n] = tmp1 + (sh4cpu.SR & Sh4Context.flagT);
						if(tmp0 > tmp1)
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
						if(tmp1 > sh4cpu.registers[n]) sh4cpu.SR |= Sh4Context.flagT;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ADDV:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						int ans=0;
						int dest = (sh4cpu.registers[n] >> 31) & 1;
						int src  = (sh4cpu.registers[m] >> 31) & 1;
					  
						src += dest;
						sh4cpu.registers[n] += sh4cpu.registers[m];

						ans = (sh4cpu.registers[n] >> 31) & 1;
						ans += dest;
					  
						if ((src == 0) || (src == 2))
							sh4cpu.SR |= ans;
						else
							sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CMPIM:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if(sh4cpu.registers[0] == n)
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CMPEQ:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if(sh4cpu.registers[n] == sh4cpu.registers[m])
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CMPHS:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if((long)sh4cpu.registers[n] >= (long)sh4cpu.registers[m])
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CMPGE:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if(sh4cpu.registers[n] >= sh4cpu.registers[m])
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CMPHI:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if (((long)( sh4cpu.registers[n] & 0xFFFFFFFF)) > ((long)( sh4cpu.registers[m] & 0xFFFFFFFF)))
							 sh4cpu.SR |= Sh4Context.flagT;
						else  sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CMPGT:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if(sh4cpu.registers[n] > sh4cpu.registers[m])
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CMPPZ:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if(sh4cpu.registers[n] >= 0)
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CMPPL:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if(sh4cpu.registers[n] > 0)
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);

					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CMPSTR:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						int tmp = sh4cpu.registers[n] ^ sh4cpu.registers[m];
						int HH  = (tmp >>> 24) & 0x000000FF;
						int HL  = (tmp >>> 16) & 0x000000FF;
						int LH  = (tmp >>>  8) & 0x000000FF;
						int LL  = (tmp >>>  0) & 0x000000FF;

						sh4cpu.SR  |= ((HH & HL & LH & LL) & 0);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.DIV1:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.DIV0S:				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.DIV0U:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						 sh4cpu.SR &= (~Sh4Context.flagQ);
						 sh4cpu.SR &= (~Sh4Context.flagM);
						 sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.DMULS:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						long mult = (long)sh4cpu.registers[n] * (long)sh4cpu.registers[m];

						sh4cpu.MACL = (int)(mult & 0xffffffff);
						sh4cpu.MACH = (int)((mult >>> 32) & 0xffffffff);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.DMULU:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						long mult = (long)(sh4cpu.registers[n] & 0xffffffff) * (long)(sh4cpu.registers[m] & 0xffffffff);

						sh4cpu.MACL = (int)(mult & 0xffffffff);
						sh4cpu.MACH = (int)((mult >>> 32) & 0xffffffff);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.DT:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n]--;
						if(sh4cpu.registers[n] == 0){
							sh4cpu.SR |= Sh4Context.flagT;
						}
						else{
							sh4cpu.SR &= (~Sh4Context.flagT);
						}
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.EXTSB:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.registers[m];
						if ((sh4cpu.registers[m] & 0x00000080)==0) sh4cpu.registers[n] &= 0x000000FF;
						else sh4cpu.registers[n] |= 0xFFFFFF00;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.EXTSW:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.registers[m];
						if ((sh4cpu.registers[m] & 0x00008000)==0) sh4cpu.registers[n] &= 0x0000FFFF;
						else sh4cpu.registers[n] |= 0xFFFF0000;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.EXTUB:				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.registers[m];
						sh4cpu.registers[n] &= 0x000000FF;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.EXTUW:				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.registers[m];
						sh4cpu.registers[n] &= 0x0000FFFF;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MACL:				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.MACL = sh4cpu.registers[n] * sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MACW:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MULL:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.MACL = sh4cpu.registers[n] * sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MULSW:			
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.MACL = (short)sh4cpu.registers[n] * (short)sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.MULSU:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.MACL = (((int)sh4cpu.registers[n] & 0xFFFF) * ((int)sh4cpu.registers[m] & 0xFFFF));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.NEG:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = 0 - sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.NEGC:
			block.add(new NativeOpcode(){

					@Override
					public void call() {
						int tmp  = 0 - sh4cpu.registers[m];
						sh4cpu.registers[n] = tmp - (sh4cpu.SR & Sh4Context.flagT);
						if(0 < tmp)
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
						if(tmp < sh4cpu.registers[n]) 
							sh4cpu.SR |= Sh4Context.flagT;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SUB:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SUBC:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int tmp0 = sh4cpu.registers[n];
						int tmp1 = sh4cpu.registers[n] - sh4cpu.registers[m];
						sh4cpu.registers[n] = tmp1 - (sh4cpu.SR & Sh4Context.flagT);
						if(tmp0 < tmp1)
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
						
						if (tmp1 < sh4cpu.registers[n])
							sh4cpu.SR |= Sh4Context.flagT;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SUBV:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int ans;
						int dest = (sh4cpu.registers[n] >> 31) & 1;
						int src  = (sh4cpu.registers[m] >> 31) & 1;
					  
						src += dest;
						sh4cpu.registers[n] -= sh4cpu.registers[m];

						ans = (sh4cpu.registers[n] >> 31) & 1;
						ans += dest;
					  
						if (src == 1)
					       if (ans == 1)
					    	   sh4cpu.SR |= Sh4Context.flagT;
					       else sh4cpu.SR &= (~Sh4Context.flagT);
						else
							sh4cpu.SR &= (~Sh4Context.flagT);

					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.AND:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] &= sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ANDI:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[0] &= n; 
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ANDM:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int value = (byte) Memory.read8(sh4cpu.GBR + sh4cpu.registers[0]);
						Memory.write8(sh4cpu.GBR + sh4cpu.registers[0],((byte)(value & n)));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.NOT:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = ~sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.OR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] |= sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ORI:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[0] |= n; 
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ORM:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int value = Memory.read8(sh4cpu.GBR + sh4cpu.registers[0]);
						Memory.write8(sh4cpu.GBR + sh4cpu.registers[0],((byte)(value | n)));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.TAS:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						byte value = (byte)Memory.read8(sh4cpu.registers[n]);
						if (value == 0)
							sh4cpu.SR |=0x1;
						else sh4cpu.SR &=~0x1;
						Memory.write8(sh4cpu.registers[n],((byte)(value | 0x80)));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.TST:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						if((sh4cpu.registers[n] & sh4cpu.registers[m]) == 0)
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.TSTI:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						if((sh4cpu.registers[0] & n) !=0)
							sh4cpu.SR |= Sh4Context.flagT;
						else  sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.TSTM:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int value = Memory.read8(sh4cpu.GBR + sh4cpu.registers[0]);
						if((value & n) == 0)
							sh4cpu.SR |= Sh4Context.flagT;
						else sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.XOR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] ^= sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.XORI:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[0] ^= n; 
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.XORM:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int value = Memory.read8(sh4cpu.GBR + sh4cpu.registers[0]);
						Memory.write8(sh4cpu.GBR + sh4cpu.registers[0],((byte)(value ^ n)));
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ROTR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SR = (sh4cpu.registers[n] >> 0) & 1;

						sh4cpu.registers[n] = ((sh4cpu.registers[n] & 0x00000001) << 31) |
								   ((sh4cpu.registers[n] & 0xfffffffe) >>> 1);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ROTCR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int t = sh4cpu.registers[n] & 0x1;

						sh4cpu.registers[n] = ((sh4cpu.SR & Sh4Context.flagT) << 31) |
								   ((sh4cpu.registers[n] & 0xfffffffe) >>> 1);

						sh4cpu.SR |= t;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ROTL:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SR |= (sh4cpu.registers[n] >>> 31) & 1;

						sh4cpu.registers[n] = ((sh4cpu.registers[n] & 0x7fffffff) << 1) |
								   ((sh4cpu.registers[n] & 0x80000000) >>> 31);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.ROTCL:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int t = (sh4cpu.registers[n] >>> 31) & 1;

						sh4cpu.registers[n] = ((sh4cpu.registers[n] & 0x7fffffff) << 1) |
								   ((sh4cpu.SR & Sh4Context.flagT) << 0);

						sh4cpu.SR |= t;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHAD:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						if((sh4cpu.registers[m] & 0x80000000) == 0)
						{
							sh4cpu.registers[n] <<= (sh4cpu.registers[m] & 0x1f);
						}
						else if((sh4cpu.registers[m] & 0x1f) == 0)
						{
							sh4cpu.registers[n] >>>= 31;
						}
						else
						{
							sh4cpu.registers[n] >>>= ((~sh4cpu.registers[m] & 0x1f) + 1);
						}
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHAR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int temp=0;
						if ((sh4cpu.registers[n]&0x00000001)==0) sh4cpu.SR &= (~Sh4Context.flagT);
						else sh4cpu.SR |=Sh4Context.flagT;
						if ((sh4cpu.registers[n]&0x80000000)==0) temp=0;
						else temp=1;
						sh4cpu.registers[n]>>=1;
						if (temp==1) sh4cpu.registers[n]|=0x80000000;
						else sh4cpu.registers[n]&=0x7FFFFFFF;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHLD:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						if((sh4cpu.registers[m] & 0x80000000) == 0)
						{
							sh4cpu.registers[n] <<= (sh4cpu.registers[m] & 0x1f);
						}
						else if((sh4cpu.registers[m] & 0x1f) == 0)
						{
							sh4cpu.registers[n] = 0;
						}
						else
						{
							sh4cpu.registers[n] >>>= ((~sh4cpu.registers[m] & 0x1f) + 1);
						}

					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHAL:
				block.add(new NativeOpcode(){

					@Override
					public void call() {

						sh4cpu.SR |= (sh4cpu.registers[n] >>> 31) & 1;
						sh4cpu.registers[n] <<= 1;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHLL:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SR = (sh4cpu.SR & ~Sh4Context.flagT) | ((sh4cpu.registers[n] >>> 31) & 1);
						sh4cpu.registers[n] <<= 1;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHLR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SR = (sh4cpu.SR & ~Sh4Context.flagT) | (sh4cpu.registers[n] & 1);
						sh4cpu.registers[n] >>>= 1;
						sh4cpu.registers[n]&=0x7FFFFFFF;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHLL2:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] <<= 2;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHLR2:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n]>>>=2;
						sh4cpu.registers[n]&=0x3FFFFFFF;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHLL8:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] <<= 8;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHLR8:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] >>>= 8;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHLL16:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] <<= 16;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SHLR16:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] >>>= 16;
						
						sh4cpu.registers[n]&=0x0000FFFF;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.BF:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PC = n;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.BFS:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PC = n;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.BT:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PC = n;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.BTS:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PC = n;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.BRA:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PC = n;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.BSR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PR = sh4cpu.PC + 4;
						sh4cpu.PC = n;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.BRAF:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PC =  sh4cpu.PC  + sh4cpu.registers[n] + 4;;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.BSRF:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PR = sh4cpu.PC + 4;
						sh4cpu.PC =  sh4cpu.PC  + sh4cpu.registers[n] + 4;;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.JMP:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PC = sh4cpu.registers[n];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.JSR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PR = sh4cpu.PC + 4;
						sh4cpu.PC = sh4cpu.registers[n];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.RTS:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PC = sh4cpu.PR;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.RTE:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int rb = (sh4cpu.SR & Sh4Context.flagsRB);
						sh4cpu.SR =sh4cpu.SSR & 0x700083f3;

						if((sh4cpu.SR & Sh4Context.flagsRB) != rb)
						{
							sh4cpu.switch_gpr_banks();
						}
						sh4cpu.PC = sh4cpu.SPC;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CLRMAC:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.MACL = sh4cpu.MACH = 0;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CLRS:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SR &= (~Sh4Context.flagS);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.CLRT:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SR &= (~Sh4Context.flagT);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCSR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int rb = (sh4cpu.SR & Sh4Context.flagsRB);

						sh4cpu.SR = sh4cpu.registers[n] & 0x700083f3;

						if(((sh4cpu.SR & Sh4Context.flagsRB) != rb) && ((sh4cpu.SR & Sh4Context.flagMD)!=0))
						{
							sh4cpu.switch_gpr_banks();
						}
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCGBR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.GBR = sh4cpu.registers[n];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCVBR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.VBR = sh4cpu.registers[n];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCSSR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SSR = sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCSPC:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SPC = sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCDBR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.DBR = sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCRBANK:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[m] = sh4cpu.registers[n];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCMSR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int rb = (sh4cpu.SR & Sh4Context.flagsRB);

						sh4cpu.SR = Memory.read32(sh4cpu.registers[n]) & 0x700083f3;

						if(((sh4cpu.SR & Sh4Context.flagsRB) != rb) && ((sh4cpu.SR & Sh4Context.flagMD)!=0))
						{
							sh4cpu.switch_gpr_banks();
						}

						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCMGBR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.GBR = Memory.read32(sh4cpu.registers[n]);
						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCMVBR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.VBR = Memory.read32(sh4cpu.registers[n]);
						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCMSSR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SSR = Memory.read32(sh4cpu.registers[n]);
						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCMSPC:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SPC = Memory.read32(sh4cpu.registers[n]);
						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCMDBR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.DBR = Memory.read32(sh4cpu.registers[n]);
						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDCMRBANK:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[m] = Memory.read32(sh4cpu.registers[n]);
						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSMACH:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.MACH = sh4cpu.registers[n];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSMACL:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.MACL = sh4cpu.registers[n];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSPR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PR = sh4cpu.registers[n];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSMMACH:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.MACH = Memory.read32(sh4cpu.registers[n]);
						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSMMACL:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.MACL = Memory.read32(sh4cpu.registers[n]);
						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSMPR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.PR = Memory.read32(sh4cpu.registers[n]);
						sh4cpu.registers[n] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDTLB:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.NOP:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.OCBI:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.OCBP:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.OCBWB:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.PREF:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						int QACR0 = Memory.regmapReadhandle32(MMREG.QACR0);
						int QACR1 = Memory.regmapReadhandle32(MMREG.QACR1);
						int addr;
						IntBuffer src;
						if (sh4cpu.registers[n] >= 0xe0000000 && sh4cpu.registers[n] <= 0xeffffffc)
						{
						    addr = (sh4cpu.registers[n] & 0x03FFFFC0) | ((( (((sh4cpu.registers[n] & 0x20)!=0) ? QACR1 : QACR0)  >> 2) & 0x07) << 26);

						    if ((sh4cpu.registers[n] & 0x20)!=0)
						    {
						    	src = Memory.SQ0;
						    	addr |= 0x20;
							}
							else
								src = Memory.SQ1;
						
							if ((addr >= 0x10000000) && (addr < 0x10800000))
							{
								System.out.println("null");
								TileAccelarator.TaTransfer(src,8);
							}
							else
							{
								Memory.sqWriteToMemory(addr,(sh4cpu.registers[n] & 0x20));
							}
						}
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SETS:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SR |= Sh4Context.flagS;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SETT:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SR |= Sh4Context.flagT;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.SLEEP:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCSR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.SR;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCGBR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.GBR;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCVBR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.VBR;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCSSR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.SSR;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCSPC:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.SPC;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCSGR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.SGR;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCDBR:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.DBR;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCRBANK:				
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.registers[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCMSR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.SR);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCMGBR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.GBR);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCMVBR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.VBR);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCMSSR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.SSR);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCMSPC:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.SPC);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCMSGR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.SGR);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCMDBR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.DBR);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STCMRBANK:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.registers[m]);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSMACH:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.MACH;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSMACL:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.MACL;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSPR:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.PR;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSMMACH:	
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.MACH);

					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSMMACL:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.MACL);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSMPR:			
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.PR);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.TRAPA:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.SSR=sh4cpu.SR;
						sh4cpu.SPC=sh4cpu.PC+2;
						sh4cpu.SGR=sh4cpu.registers[15];
						sh4cpu.SR |= sh4cpu.flagMD;
						sh4cpu.SR |= sh4cpu.flagBL;
						sh4cpu.SR |= sh4cpu.flagsRB;
						//EXPEVT=0x00000160;
						sh4cpu.PC=sh4cpu.VBR+0x00000100;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSFPSCR:
					block.add(new NativeOpcode(){

					@Override
					public void call() {

						int fr = (sh4cpu.FPSCR & Sh4Context.flagFR);

						sh4cpu.FPSCR = sh4cpu.registers[n] & 0x003fffff;

						if((sh4cpu.FPSCR & Sh4Context.flagFR) != fr)
						{
							sh4cpu.swith_fp_banks();
					 	}
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSFPUL:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.FPUL = sh4cpu.registers[n];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSMFPSCR:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						int fr = (sh4cpu.FPSCR & Sh4Context.flagFR);

						sh4cpu.FPSCR = Memory.read32(sh4cpu.registers[n]) & 0x003fffff;
						sh4cpu.registers[m] += 4;

						if((sh4cpu.FPSCR & Sh4Context.flagFR) != fr)
						{
							sh4cpu.swith_fp_banks();
					 	}
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.LDSMFPUL:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.FPUL = Memory.read32(sh4cpu.registers[m]);
						sh4cpu.registers[m] += 4;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSFPSCR:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.FPSCR;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSFPUL:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] = sh4cpu.FPUL;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSMFPSCR:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.FPSCR);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.STSMFPUL:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.registers[n] -= 4;
						Memory.write32(sh4cpu.registers[n], sh4cpu.FPUL);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FLDI0:
					block.add(new NativeOpcode(){

					@Override
					public void call() {

						sh4cpu.FRm[n] = 0x00000000;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FLDI1:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.FRm[n] = 0x3f800000;
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FMOV:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FMOV_LOAD:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FMOV_INDEX_LOAD:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FMOV_RESTORE:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FMOV_SAVE:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FMOV_STORE:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FMOV_INDEX_STORE	:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FLDS:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.FPUL = (int) sh4cpu.FRm[m];
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FSTS:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.FRm[n] =Float.intBitsToFloat(sh4cpu.FPUL);
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FABS:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FNEG:
				block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FCNVDS:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if ((sh4cpu.FPSCR & Sh4Context.flagPR)!=0)
						{
							
							double v = sh4cpu.getDR(n);
							
							sh4cpu.FPUL =(int) Double.doubleToLongBits(v);
						}
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FCNVSD:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						if ((sh4cpu.FPSCR & Sh4Context.flagPR)!=0)
						{										
							sh4cpu.setDR(n, Double.longBitsToDouble((long)sh4cpu.FPUL));
						}
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FTRC:
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FLOAT1:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FCMPEQ:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FCMPGT:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FMAC:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FDIV:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FMUL:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FSQRT:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FSRRA:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FADD:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FSUB:				
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FIPR:		
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FTRV:		
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FSCA:			
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FRCHG:	
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.FPSCR ^= Sh4Context.flagFR;

						sh4cpu.swith_fp_banks();
					}
				
				});
				cycles += 1; break;
			case  NativeOpcode.FSCHG:		
					block.add(new NativeOpcode(){

					@Override
					public void call() {
						sh4cpu.FPSCR ^= Sh4Context.flagSZ;
					}
				
				});
				cycles += 1; break;
			}
		}
		numberOfInstructions++;
	}
	
	public void setFinished(boolean end){
		finished = true;
	}
	
	public void setCycles(int c){
		cycles = c;
	}
}
