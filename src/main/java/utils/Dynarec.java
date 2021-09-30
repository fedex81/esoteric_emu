package utils;

import java.util.ArrayList;

public class Dynarec {

	ArrayList<NativeOpcode> block;
	
	public Dynarec(){
		block = new ArrayList<NativeOpcode>(15);
	}
	
	public void executeBlock(){
		for(int i =0; i < block.size();i++)
			block.get(i).call();
	}
		
	public void addInstruction(){
		block.add(
				new NativeOpcode(){
					public void call(){
						System.out.println("Call 1");
					}
				}
		);
		//
		block.add(
				new NativeOpcode(){
					public void call(){
						System.out.println("Call 2");
					}
				}
		);
		//
		block.add(
				new NativeOpcode(){
					public void call(){
						System.out.println("Call 3");
					}
				}
		);
	}
}
