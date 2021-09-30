package powervr;

import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.ListIterator;

import memory.Memory;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTBgra;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

public final class TextureCache {

	LinkedList<Texture> TextureCache;
	ListIterator<Texture> pointer;
	
	private static int  lastBound=-1;

	private static int TextureRequiresConversion=0;
	
	private static final int CONVERT_YUV422=1;
	private static final int CONVERT_BUMPMAP=2;
	
	/*
	 * YUV422 to RGB code from 
	 * http://www.fourcc.org/source/YUV420P-OpenGL-GLSLang.c
	 * 
	 * twiddle.frag is a glsl program to detwiddle textures
	 * vq.frag is a glsl program to decompress vq textures
	 */
	
	/* texture twiddling
	 * 
	 * 
	 * 		 for(int x=0; x<1024; x++)
	 *		    twiddletab[x] = (x&1)|((x&2)<<1)|((x&4)<<2)|((x&8)<<3)|((x&16)<<4)|
	 *		      ((x&32)<<5)|((x&64)<<6)|((x&128)<<7)|((x&256)<<8)|((x&512)<<9);}
	 *
	 *	Algorithm to generate the table found on twiddle.frag
	 *	Thanks again to Ivan Toledo and Dan Potter
	 *
	 */
	public TextureCache(){
		TextureCache = new LinkedList<Texture>();
		pointer = TextureCache.listIterator();
	}
	
	// we add the elements to the head
	public void addTexture(Texture t){
		TextureCache.addFirst(t);
	}
	
	/* returns the texture name to use with bind texture */
	public int findTexture(int h,int v,int mem,int pf){
		Texture t;
		while(true){
			t = pointer.next();
			if(t == null)
				break;
			if(t.hsize == h && t.vsize == v && t.memorypos == mem && t.pixelformat == pf)
				return t.texture;
		}
		return 0;
	}
	
	/*
	 * based on the code from dcemu
	 */
	private final void fillTextureInfo(Texture ImageData,int pixelformat){
		switch (pixelformat)
		{
			case 0:
			System.out.println("texture: ARGB1555\n");
			ImageData.pixelformat = GL12.GL_BGRA;
			ImageData.NumberOfComponents = 4;
			ImageData.pixelpack = GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV;
			break;
	
			case 1:
			System.out.println("texture: RGB565\n");
			ImageData.pixelformat =GL11.GL_RGB;
			ImageData.NumberOfComponents =  3;
			ImageData.pixelpack  = GL12.GL_UNSIGNED_SHORT_5_6_5;
			break;
	
			case 2:
			System.out.println("texture: ARGB4444\n");
			ImageData.pixelformat= EXTBgra.GL_BGRA_EXT;	
			ImageData.pixelpack = GL12.GL_UNSIGNED_SHORT_4_4_4_4_REV;
	  		ImageData.NumberOfComponents = 4;
			break;
	
			case 3:
				System.out.println("texture: YUV422\n");
				ImageData.pixelformat =GL11.GL_RGB;
				ImageData.NumberOfComponents =  3;
				ImageData.pixelpack  = GL12.GL_UNSIGNED_SHORT_5_6_5;
				TextureRequiresConversion = CONVERT_YUV422;
			break;
			case 4:
				System.out.println("texture: BUMP\n");
				TextureRequiresConversion = CONVERT_BUMPMAP;
			break;
			case 5: System.out.println("texture: 4BPP_PALETTE\n");break;
			case 6: System.out.println("texture: 8BPP_PALETTE\n");break;
		}
	}
	
	/* handler of the creation of new textures */
	public void createTexture(int tsp,int tcw){
		int pos =0;
		final int usize = 0x8 << TileAccelarator.tsp_texu(tsp);
		final int vsize = 0x8 << TileAccelarator.tsp_texv(tsp);
		final int memorypos = TileAccelarator.tcw_texture_surface(tcw);
		final int pf = TileAccelarator.tcw_pixelformat(tcw);
		if((pos = findTexture(usize, vsize, memorypos, pf)) !=0){
			if(lastBound != TextureCache.get(pos).texture)
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,TextureCache.get(pos).texture);
				lastBound = TextureCache.get(pos).texture;
		}
		else
		{
			// if we get here this means the texture isn't in our cache so..
			Texture ImageData = new Texture(usize,vsize,memorypos);

			fillTextureInfo(ImageData,pf);

			int start = memorypos | 0xA5000000;

			Memory.TextureMemory.position(start);
			Memory.TextureMemory.limit(start + usize*vsize*4);

			IntBuffer texture = BufferUtils.createIntBuffer(1);
						
			GL11.glGenTextures(texture);

			ImageData.texture = texture.get(0);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D,ImageData.texture);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, ImageData.pixelformat, usize,vsize,0,ImageData.pixelformat,ImageData.pixelpack ,Memory.TextureMemory);

			addTexture(ImageData);
		}
	}
}
