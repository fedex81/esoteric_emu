package scd;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import memory.Memory;
import sh4.Sh4Context;

public final class Controllers extends Maple {
	
    /*
     * In turn i got this from swirly. Thank you :)
     *  
	    This struct copied almost verbatim from http://mc.pp.se/dc/maplebus.html
	    Thank you Marcus Comstedt!
	    struct DeviceInfo {
		Dword suppFuncs;         // func codes supported - big endian
		Dword funcData[3];       // info about func codes - big endian
		Byte region;             // peripheral's region code
		Byte connectorDirection; // ?? that's odd
		char productName[30];
		char productLicense[60];
		Word standbyPower;       // standby power consumption - little endian
		Word maxPower;           // maximum power consumption - little endian
	    };
    */

	private static final ByteBuffer DeviceinfoController = ByteBuffer.allocate(112).order(ByteOrder.LITTLE_ENDIAN); // 112 bytes
	
	private static final ByteBuffer ConditionInfoController = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
	
	public static final int KEYBOARD_INPUT = 1;
	
	public static final int JOYSTICK_INPUT = 2;
	
	
	public Controllers(){
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		if(Keyboard.isCreated())
			System.out.println("Keyboard created successfully");
		DeviceinfoController.putInt(MAPLE_CONTROLLER);
		// func codes supported
		DeviceinfoController.putInt(0);
		DeviceinfoController.putInt(0);
		DeviceinfoController.putInt(0);
		DeviceinfoController.put((byte)0); // region
		DeviceinfoController.put((byte)0); // connector direction	
		DeviceinfoController.put(Maple.product_name_data);
		DeviceinfoController.put(Maple.product_license_data);
		// from swirly
		//standby power consumption
		DeviceinfoController.putShort((short)0x01ae);
		// // maximum power consumption 
		DeviceinfoController.putShort((short)0x01f4);
		System.out.println(DeviceinfoController.position());
		DeviceinfoController.rewind();
		ConditionInfoController.putShort(_buttons,(short)0xFFFF);
		Maple.KeyboardInUse = true;
	}
	
	public void writeDeviceInfo(int address) {
		Memory.mem.position(address);
		Memory.mem.put(DeviceinfoController);
	}

	public void getConditionInfo(int address) {
		//readKeyboard();
		Memory.mem.position(address);
		Memory.mem.put(ConditionInfoController);
	}
	
	public void printConditionInfo(){
		System.out.println("Condition Info Controller");
		for(int i =0; i < ConditionInfoController.limit(); i++){
			System.out.print(ConditionInfoController.get(i));
		}
	}
	
	public int getDeviceInfoSize(){
		return DeviceinfoController.limit();
	}
	
	public int getConditionInfoSize(){
		return ConditionInfoController.limit(); // 8 bytes
	}
	
	// condition fields
	/*
	int buttons;		buttons bitfield  this is only 16 bits
	byte rtrig;			right trigger
	byte ltrig;			left trigger 
	byte joyx;			joystick X 
	byte joyy;			joystick Y 
	byte joy2x;			second joystick X 
	byte joy2y;			 second joystick Y 
	*/
	
	private static final int _buttons=0;
	private static final int _rtrig=2;
	private static final int _ltrig=3;
	private static final int _joyx = 4;
	private static final int _joyy = 5;
	private static final int _joy2x = 6;
	private static final int _joy2y = 7;
	
	private static final void readKeyboard(){
		short buttons = ConditionInfoController.getShort(_buttons);
		while(Keyboard.next()){
		if(Keyboard.getEventKeyState())
		{
				switch(Keyboard.getEventKey())
				{
					case  Keyboard.KEY_LEFT:
						ConditionInfoController.putShort(_buttons,((short)(buttons & (~CONT_DPAD_LEFT))));
						break;
	
					case Keyboard.KEY_RIGHT:
						ConditionInfoController.putShort(_buttons,((short)(buttons & (~CONT_DPAD_RIGHT))));
						break;
	
					case Keyboard.KEY_UP:
						ConditionInfoController.putShort(_buttons,((short)(buttons & (~CONT_DPAD_UP))));
						break;
	
					case Keyboard.KEY_DOWN:
						System.out.println("PRESSED DOWN");
						ConditionInfoController.putShort(_buttons,((short)(buttons & (~CONT_DPAD_DOWN))));
						System.out.println("Buttons after " +  Integer.toBinaryString(buttons));
					break;
						
					case Keyboard.KEY_A: // BOTON X
						ConditionInfoController.putShort(_buttons,((short)(buttons & (~CONT_X))));
						break;
	
					case Keyboard.KEY_S: // BOTON A
						ConditionInfoController.putShort(_buttons,((short)(buttons & (~CONT_A))));
						break;
	
					case Keyboard.KEY_D: // BOTON B
						ConditionInfoController.putShort(_buttons,((short)(buttons & (~CONT_B))));
						break;
	
					case Keyboard.KEY_W: // BOTON W
						ConditionInfoController.putShort(_buttons,((short)(buttons & (~CONT_Y))));
						break;
	
					case Keyboard.KEY_Z: // START
						ConditionInfoController.putShort(_buttons,((short)(buttons & (~CONT_START))));
						break;
	
					case Keyboard.KEY_Q: // LEFT
						ConditionInfoController.put(_ltrig,TRIGGER_ON);
					break;
	
					case Keyboard.KEY_E: // RIGHT
						ConditionInfoController.put(_rtrig,TRIGGER_ON);
					break;
	
					case Keyboard.KEY_Y: // buttons up
						ConditionInfoController.put(_joyy,JOYSTICK_UP);
					break;
					
					case Keyboard.KEY_H: // buttons down
						ConditionInfoController.put(_joyy,JOYSTICK_DOWN);
					break;
					
					case Keyboard.KEY_G: // buttons left
						ConditionInfoController.put(_joyx,JOYSTICK_LEFT);
					break;
					
					case Keyboard.KEY_J: // buttons right
						ConditionInfoController.put(_joyx,JOYSTICK_RIGHT);
					break;				
				}
		}
		else{
				switch(Keyboard.getEventKey())
				{
				case Keyboard.KEY_LEFT:
					ConditionInfoController.putShort(_buttons,(short) (buttons | CONT_DPAD_LEFT));
				break;

				case Keyboard.KEY_RIGHT:
					ConditionInfoController.putShort(_buttons,(short)(buttons | CONT_DPAD_RIGHT));
				break;

				case Keyboard.KEY_UP:
					ConditionInfoController.putShort(_buttons,(short)(buttons | CONT_DPAD_UP));
				break;

				case Keyboard.KEY_DOWN:
					ConditionInfoController.putShort(_buttons,(short)(buttons | CONT_DPAD_DOWN));
				break;
				
				case Keyboard.KEY_A: // BOTON X
					ConditionInfoController.putShort(_buttons,(short)(buttons | CONT_X));
				break;
				
				case Keyboard.KEY_S: // BOTON A
					ConditionInfoController.putShort(_buttons,(short)(buttons | CONT_A));
				break;
				
				case Keyboard.KEY_D: // BOTON B
					ConditionInfoController.putShort(_buttons,(short)(buttons | CONT_B));
				break;
				
				case Keyboard.KEY_W: // BOTON W
					ConditionInfoController.putShort(_buttons,(short)(buttons | CONT_Y));
				break;

				case Keyboard.KEY_Z: // START
					ConditionInfoController.putShort(_buttons,(short)(buttons | CONT_START));
				break;

				case Keyboard.KEY_Q: // LEFT
					ConditionInfoController.put(_ltrig,TRIGGER_OFF);
				break;

				case Keyboard.KEY_E: // RIGHT
					ConditionInfoController.put(_rtrig,TRIGGER_OFF);
				break;
				
				case Keyboard.KEY_Y: // buttons up
				case Keyboard.KEY_H: // buttons down
					ConditionInfoController.put(_joyy, JOYSTICK_NEUTRAL);
				break;
				
				case Keyboard.KEY_G: // buttons left
				case Keyboard.KEY_J: // buttons right
					ConditionInfoController.put(_joyx, JOYSTICK_NEUTRAL);
				break;
			}
		}
	  }
	}

	public void ReadDeviceInput() {
		readKeyboard();
	}
}
