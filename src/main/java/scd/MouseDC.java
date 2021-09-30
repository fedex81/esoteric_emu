package scd;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

public class MouseDC extends Maple {

	public static final int MOUSE_RIGHTBUTTON =	(1<<1);
	public static final int MOUSE_LEFTBUTTON =	(1<<2);
	public static final int MOUSE_SIDEBUTTON =	(1<<3);

	public static final int MOUSE_DELTA_CENTER =  0x200;
	
	public MouseDC(){
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public void writeDeviceInfo(ByteBuffer mem,int address){};
	
	public void getConditionInfo(ByteBuffer mem,int address){}

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
	public void getConditionInfo(int address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeDeviceInfo(int address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ReadDeviceInput() {
		// TODO Auto-generated method stub
		
	};
}
