package powervr;

public class Texture {

	public int hsize;
	public int vsize;	
	int memorypos;	// position in memory of the texture
	int	texture;
 	boolean	twiddled;
 	boolean	vq;
 	int pixelformat;
 	int pixelpack;
 	int NumberOfComponents;
 	
 	public Texture(int h,int v,int mem,int name,boolean vq,boolean t){
 		hsize = h; vsize = v; memorypos = mem;texture = name;
 		twiddled = t; this.vq = vq;
 	}
 	
 	public Texture(int h,int v,int mem){
 		hsize = h; vsize = v; memorypos = mem;
 	}
 	
 	public void setPixelInfo(int PixelFormat,int PixelPack,int Components){
 		pixelformat = PixelFormat;
 		pixelpack = PixelPack;
 		NumberOfComponents = Components;
 	}
}
