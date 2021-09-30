package scd;

import java.util.LinkedList;
import java.util.ListIterator;

import memory.Memory;

import org.lwjgl.opengl.GL11;

public final class TextureCache {

	LinkedList<Texture> TextureCache;
	ListIterator<Texture> pointer;
	
	private static int  lastBound=-1;

	// texture twiddling
	static int twiddletab[];
	// we init the twiddled table
	static {
		twiddletab	= new int[1024];
		 for(int x=0; x<1024; x++)
			    twiddletab[x] = (x&1)|((x&2)<<1)|((x&4)<<2)|((x&8)<<3)|((x&16)<<4)|
			      ((x&32)<<5)|((x&64)<<6)|((x&128)<<7)|((x&256)<<8)|((x&512)<<9);
	}
	
	public TextureCache(){
		TextureCache = new LinkedList<Texture>();
		pointer = TextureCache.listIterator();
	}
	
	// we add the elements to the head
	public void addTexture(Texture t){
		TextureCache.addFirst(t);
	}
	
	/* returns the texture name to use with bind texture */
	public int findTexture(int h,int v,int mem,int pf,int pp){
		Texture t;
		while(true){
			t = pointer.next();
			if(t == null)
				break;
			if(t.hsize == h && t.vsize == v && t.memorypos == mem && t.pixelformat == pf && t.pixelpack == pp)
				return t.texture;
		}
		return 0;
	}
	
	/* handler of the creation of new textures */
	public void createTexture(int usize, int vsize,int memorypos, int twiddled, int vq,int pf,int pp,int c){
		int pos =0;
		if((pos = findTexture(usize, vsize, memorypos, pf, pp)) !=0){
			if(lastBound != TextureCache.get(pos).texture)
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,TextureCache.get(pos).texture);
				lastBound = TextureCache.get(pos).texture;
		}
		else
		{
			// if we get here this means the texture isn't in our cache so..
			Texture ImageData = new Texture(usize,vsize,memorypos);

			ImageData.setPixelInfo(pf, pp, c);

			int start = memorypos | 0xA5000000;

			Memory.TextureMemory.position(start);
			Memory.TextureMemory.limit(start + usize*vsize*4);

			GL11.glGenTextures(Memory.TextureMemory);

			ImageData.texture =Memory.TextureMemory.get(0);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D,ImageData.texture);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pf, usize,vsize,0,pf, pp,Memory.TextureMemory);

			addTexture(ImageData);
		}
	}
}
