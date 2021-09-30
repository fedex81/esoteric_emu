package scd;

/*
 * http://mc.pp.se/dc/kbd.html
 */

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import io.Error;

/* Only Keyboard input is processed now */
public class KeyboardDC extends Maple{

	
	
	/*
	 * Condition structure for the keyboard
	 * 
	 *	int8 shift          ; shift keys pressed (bitmask)
	 *	int8 led            ; leds currently lit
	 *	int8 key[6]         ; normal keys pressed
	 */
	
	/*
	 * Bitfields from kos for the Keyboard (YOU GUYS/GIRLS FROM KOS ROCK!!!!)
	 */
	

	/* modifier keys */
	public static final int KBD_MOD_LCTRL 	=	(1<<0);
	public static final int KBD_MOD_LSHIFT	=	(1<<1);
	public static final int KBD_MOD_LALT	=	(1<<2);
	public static final int KBD_MOD_S1	=(1<<3);
	public static final int KBD_MOD_RCTRL	=	(1<<4);
	public static final int KBD_MOD_RSHIFT	=	(1<<5);
	public static final int KBD_MOD_RALT	=	(1<<6);
	public static final int KBD_MOD_S2	=	(1<<7);

	/* bits for leds : this is not comprensive (need for japanese kbds also) */
	public static final int KBD_LED_NUMLOCK	= (1<<0);
	public static final int KBD_LED_CAPSLOCK = (1<<1);
	public static final int KBD_LED_SCRLOCK	= (1<<2);

	/* defines for the keys (argh...) */
	public static final int KBD_KEY_NONE	=	0x00;
	public static final int KBD_KEY_ERROR=		0x01;
	public static final int KBD_KEY_A	=	0x04;
	public static final int KBD_KEY_B	=	0x05;
	public static final int KBD_KEY_C	=	0x06;
	public static final int KBD_KEY_D	=	0x07;
	public static final int KBD_KEY_E	=	0x08;
	public static final int KBD_KEY_F	=	0x09;
	public static final int KBD_KEY_G	=	0x0a;
	public static final int KBD_KEY_H	=	0x0b;
	public static final int KBD_KEY_I	=	0x0c;
	public static final int KBD_KEY_J	=	0x0d;
	public static final int KBD_KEY_K	=	0x0e;
	public static final int KBD_KEY_L	=	0x0f;
	public static final int KBD_KEY_M	=	0x10;
	public static final int KBD_KEY_N	=	0x11;
	public static final int KBD_KEY_O	=	0x12;
	public static final int KBD_KEY_P	=	0x13;
	public static final int KBD_KEY_Q	=	0x14;
	public static final int KBD_KEY_R	=	0x15;
	public static final int KBD_KEY_S	=	0x16;
	public static final int KBD_KEY_T	=	0x17;
	public static final int KBD_KEY_U	=	0x18;
	public static final int KBD_KEY_V	=	0x19;
	public static final int KBD_KEY_W	=	0x1a;
	public static final int KBD_KEY_X	=	0x1b;
	public static final int KBD_KEY_Y	=	0x1c;
	public static final int KBD_KEY_Z	=	0x1d;
	public static final int KBD_KEY_1	=	0x1e;
	public static final int KBD_KEY_2	=	0x1f;
	public static final int KBD_KEY_3	=	0x20;
	public static final int KBD_KEY_4	=	0x21;
	public static final int KBD_KEY_5	=	0x22;
	public static final int KBD_KEY_6	=	0x23;
	public static final int KBD_KEY_7	=	0x24;
	public static final int KBD_KEY_8	=	0x25;
	public static final int KBD_KEY_9	=	0x26;
	public static final int KBD_KEY_0	=	0x27;
	public static final int KBD_KEY_ENTER	=	0x28;
	public static final int KBD_KEY_ESCAPE	=	0x29;
	public static final int KBD_KEY_BACKSPACE =	0x2a;
	public static final int KBD_KEY_TAB	=	0x2b;
	public static final int KBD_KEY_SPACE	=	0x2c;
	public static final int KBD_KEY_MINUS	=	0x2d;
	public static final int KBD_KEY_PLUS	=	0x2e;
	public static final int KBD_KEY_LBRACKET=	0x2f;
	public static final int KBD_KEY_RBRACKET=	0x30;
	public static final int KBD_KEY_BACKSLASH=	0x31;
	public static final int KBD_KEY_SEMICOLON=	0x33;
	public static final int KBD_KEY_QUOTE	=	0x34;
	public static final int KBD_KEY_TILDE	=	0x35;
	public static final int KBD_KEY_COMMA	=	0x36;
	public static final int KBD_KEY_PERIOD	=	0x37;
	public static final int KBD_KEY_SLASH	=	0x38;
	public static final int KBD_KEY_CAPSLOCK	=0x39;
	public static final int KBD_KEY_F1	=	0x3a;
	public static final int KBD_KEY_F2	=	0x3b;
	public static final int KBD_KEY_F3	=	0x3c;
	public static final int KBD_KEY_F4	=	0x3d;
	public static final int KBD_KEY_F5	=	0x3e;
	public static final int KBD_KEY_F6	=	0x3f;
	public static final int KBD_KEY_F7	=	0x40;
	public static final int KBD_KEY_F8	=	0x41;
	public static final int KBD_KEY_F9	=	0x42;
	public static final int KBD_KEY_F10	=	0x43;
	public static final int KBD_KEY_F11	=	0x44;
	public static final int KBD_KEY_F12	=	0x45;
	public static final int KBD_KEY_PRINT=		0x46;
	public static final int KBD_KEY_SCRLOCK	=	0x47;
	public static final int KBD_KEY_PAUSE	=	0x48;
	public static final int KBD_KEY_INSERT	=	0x49;
	public static final int KBD_KEY_HOME	=	0x4a;
	public static final int KBD_KEY_PGUP	=	0x4b;
	public static final int KBD_KEY_DEL	=	0x4c;
	public static final int KBD_KEY_END	=	0x4d;
	public static final int KBD_KEY_PGDOWN	=	0x4e;
	public static final int KBD_KEY_RIGHT	=	0x4f;
	public static final int KBD_KEY_LEFT	=	0x50;
	public static final int KBD_KEY_DOWN	=	0x51;
	public static final int KBD_KEY_UP	=	0x52;
	public static final int KBD_KEY_PAD_NUMLOCK=	0x53;
	public static final int KBD_KEY_PAD_DIVIDE	=0x54;
	public static final int KBD_KEY_PAD_MULTIPLY=	0x55;
	public static final int KBD_KEY_PAD_MINUS=	0x56;
	public static final int KBD_KEY_PAD_PLUS=	0x57;
	public static final int KBD_KEY_PAD_ENTER=	0x58;
	public static final int KBD_KEY_PAD_1	=	0x59;
	public static final int KBD_KEY_PAD_2	=	0x5a;
	public static final int KBD_KEY_PAD_3	=	0x5b;
	public static final int KBD_KEY_PAD_4	=	0x5c;
	public static final int KBD_KEY_PAD_5	=	0x5d;
	public static final int KBD_KEY_PAD_6	=	0x5e;
	public static final int KBD_KEY_PAD_7	=	0x5f;
	public static final int KBD_KEY_PAD_8	=	0x60;
	public static final int KBD_KEY_PAD_9	=	0x61;
	public static final int KBD_KEY_PAD_0	=	0x62;
	public static final int KBD_KEY_PAD_PERIOD	= 0x63;
	public static final int KBD_KEY_S3	=	0x65;
	
	public KeyboardDC(){
		if(Maple.KeyboardInUse){
			Error.errornum = Error.ioError;
			return;
		}
		else{
			try {
				Keyboard.create();
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
			
	}
	
	public void writeDeviceInfo(int address){
		
	};
	
	public void getConditionInfo(int address){
		
	}

	@Override
	public int getConditionInfoSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDeviceInfoSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void ReadDeviceInput() {
		// TODO Auto-generated method stub
		
	};
}
