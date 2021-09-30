package scd;

import java.nio.ByteBuffer;

/*
 * Contains the Maple(dreamcast bus definition) 
 */
public abstract class Maple {

	public static boolean KeyboardInUse = false;
	
	public static final byte TRIGGER_ON	=	(byte)0xFF;
	public static final byte TRIGGER_OFF	=	0x00;
	public static final byte JOYSTICK_UP	=	0x00;
	public static final byte JOYSTICK_DOWN =(byte)	0xFF;
	public static final byte JOYSTICK_LEFT =	0x00;
	public static final byte JOYSTICK_RIGHT =(byte)	0xFF;
	public static final byte JOYSTICK_NEUTRAL =	(byte)128;
	public static final short CONT_C = 		(1<<0);
	public static final short CONT_B  =		(1<<1);
	public static final short CONT_A  	=	(1<<2);
	public static final short CONT_START =		(1<<3);
	public static final short CONT_DPAD_UP =	(1<<4);
	public static final short CONT_DPAD_DOWN =   (1<<5);
	public static final short CONT_DPAD_LEFT  = (1<<6);
	public static final short CONT_DPAD_RIGHT = (1<<7);
	public static final short CONT_Z  	=	   (1<<8);
	public static final short CONT_Y  	=	   (1<<9);
	public static final short CONT_X  	=	   (1<<10);
	public static final short CONT_D  	=	   (1<<11);
	public static final short CONT_DPAD2_UP =		   (1<<12);
	public static final short CONT_DPAD2_DOWN = 	   (1<<13);
	public static final short CONT_DPAD2_LEFT 	=   (1<<14);
	public static final short CONT_DPAD2_RIGHT	 =(short)  (1<<15);

	// peripherals
    public static final int  MAPLE_CONTROLLER =	1 << 24;
    public static final int  MAPLE_MEMCARD =	Integer.reverseBytes(0x002);
    public static final int  MAPLE_LCD =		Integer.reverseBytes(0x004);
    public static final int  MAPLE_CLOCK =		Integer.reverseBytes(0x008);
    public static final int  MAPLE_MICROPHONE =	Integer.reverseBytes(0x010);
    public static final int  MAPLE_ARGUN	=	Integer.reverseBytes(0x020);
    public static final int  MAPLE_KEYBOARD =	Integer.reverseBytes(0x040);
    public static final int  MAPLE_LIGHTGUN	=	Integer.reverseBytes(0x080);
    public static final int  MAPLE_PURUPURU	=	Integer.reverseBytes(0x100);
    public static final int  MAPLE_MOUSE	=	Integer.reverseBytes(0x200);
	
	
	// "Dreamcast Controller"
	static final byte product_name_data[] =
	{
		0x44,0x72,0x65,0x61,0x6d,0x63,0x61,0x73,0x74,0x20,0x43,0x6f,0x6e,0x74,0x72,0x6f,0x6c,0x6c,0x65,0x72,
		0,0,0,0,0,0,0,0,0,0
	};

		// "Sega"
	public static final byte  product_license_data[] =
	{
		0x53,0x65,0x67,0x61,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,0,
	};
	
	public abstract void writeDeviceInfo(int address);
	
	public abstract void getConditionInfo(int address);
	
	public abstract int getDeviceInfoSize();
	
	public abstract int getConditionInfoSize();
	
	public abstract void ReadDeviceInput();
	// device info fields
	

	/*
		int		func; // 4
		int		function_data[]; // 3 positions -> 12
		byte		area_code;
		byte		connector_direction;
		byte		product_name; // 30 caracters
		byte		product_license; // 60 caracters
		short		standby_power;
		short		max_power;
		*/

}
