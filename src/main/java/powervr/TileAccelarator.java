package powervr;

import gui.Emu;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

import sh4.Intc;

public class TileAccelarator {

    
	public static final int ASIC_EVT_PVR_OPAQUEDONE	=	0x0007;		/* Opaque list completed */
	public static final int ASIC_EVT_PVR_OPAQUEMODDONE=	0x0008;		/* Opaque modifiers completed */
	public static final int ASIC_EVT_PVR_TRANSDONE	=	0x0009;		/* Transparent list completed */
	public static final int ASIC_EVT_PVR_TRANSMODDONE	=0x000a;		/* Transparent modifiers completed */
	public static final int ASIC_EVT_PVR_PTDONE	=	0x0015;		/* Punch-thrus completed */
	
	
	public static int pvr_ta_isp_base;
	public static int pvr_ta_itp_current;
	public static int pvr_listtype;
	public static int pvr_registering = -1;
	public static int pvr_listdone = 0;

	
	private static final int depth_modes[] = { GL11.GL_NEVER, GL11.GL_GEQUAL, GL11.GL_EQUAL, GL11.GL_GREATER,
			GL11.GL_LEQUAL, GL11.GL_NOTEQUAL, GL11.GL_LESS};
	

	 /* The code below was taken from Swirly so THANK YOU :) */

	private static final int  blendFunc[] = { GL11.GL_ZERO, GL11.GL_ONE, GL11.GL_DST_COLOR, 
			GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_SRC_ALPHA,
			GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_DST_ALPHA,
			GL11.GL_ONE_MINUS_DST_ALPHA };

	private static final int texParam[] = { GL11.GL_REPEAT,GL11.GL_CLAMP };

	private static final int texEnv[] = { GL11.GL_REPLACE, GL11.GL_MODULATE, GL11.GL_DECAL, GL11.GL_MODULATE };
         // XXX: GL_MODULATEALPHA (C = Cs * Ct, A = As * At) 

	private static final int texSizeList[] = { 8, 16, 32, 64, 128, 256, 512, 1024 };
	
	private static final int pvr_lists [] = {ASIC_EVT_PVR_OPAQUEDONE,ASIC_EVT_PVR_OPAQUEMODDONE,ASIC_EVT_PVR_TRANSDONE,
			ASIC_EVT_PVR_TRANSMODDONE,ASIC_EVT_PVR_PTDONE};

	 /* 3 vertices
	 * 4 color (RGBA)
	 * 4 ext_color (RGBA)
	 * 2 texture coordinates
	 * shift by 2 to convert to bytes
	 */
	
	public static final int strideVBO = (3 + 4 + 4 + 2) << 2; 
	
	private static final int vert_x =0;
	private static final int vert_y =1;
	private static final int vert_z =2;
	private static final int color_r =3;
	private static final int color_g =4;
	private static final int color_b =5;
	private static final int color_a =6;
	private static final int extcolor_r =7;
	private static final int extcolor_g =8;
	private static final int extcolor_b =9;
	private static final int extcolor_a =10;
	private static final int tc_x =11;
	private static final int tc_y =12;

	// offset into the vertex buffer
	public int offsetVBO =0;
	private static Object global_parameter;
	private static int vertex_parameter;
	
	/*typedef union 
	{
		struct
		{
			u32 uvmode     : 1;
			u32 gouraud    : 1;
			u32 offset     : 1;
			u32 texture    : 1;
			u32 colortype  : 2;
			u32 volume     : 1;
			u32 shadow     : 1;
			u32 reserved0  : 8;
			 
			u32 userclip   : 2;
			u32 striplen   : 2;
			u32 reserved1  : 3;
			u32 groupen    : 1;
			 
			u32 listtype   : 3;
			u32 reserved2  : 1;
			u32 endofstrip : 1;
			u32 paratype   : 3;
		};

		u32 all;

	} PCW;
	*/
	
	  // para control
	public static final int pcw_list_type(int pcw){
		return (pcw >> 24) & 0x7;
	}
   	// group control
	  public static final int pcw_group_en(int pcw){
		  return (pcw >> 23) & 0x1;
	  }
	  
	public static final int pcw_strip_len(int pcw) {
		return (pcw >> 18) & 0x3;
	}
	public static final int pcw_user_clip(int pcw) { return (pcw >> 16) & 0x3;}
	// obj control
	 public static final int pcw_shadow(int pcw)		{ return (pcw >> 7) & 0x1;}
	public static final int pcw_volume(int pcw)		{ return (pcw >> 6) & 0x1;}
	public static final int pcw_col_type(int pcw)	{ return (pcw >> 4) & 0x3;}
	public static final int pcw_texture(int pcw)		{ return (pcw >> 3) & 0x1;}
	public static final int pcw_offset	(int pcw)	{ return (pcw >> 2) & 0x1;}
	public static final int pcw_gouraud(int pcw)		{ return (pcw >> 1) & 0x1;}
	public static final int pcw_16bit_UV(int pcw)	{ return (pcw >> 0) & 0x1;}
	public static final int pcw_para_type(int pcw) { return (pcw >> 29) & 0x7;}
	public static final int pcw_endofstrip(int pcw) { return 1 << 28;}
	

	/*typedef union
	{
		struct
		{
			u32 reserved    : 20;
			u32 dcalcctrl   : 1;
			u32 cachebypass : 1;
			u32 uvmode      : 1;
			u32 gouraud     : 1;
			u32 offset      : 1;
			u32 texture     : 1;
			u32 zwritedis   : 1;
			u32 cullmode    : 2;
			u32 depthmode   : 3;
		};

		u32 all;

	 } ISP;
	 */
	public static final int isp_depthmode (int isp){ return (isp >> 29) & 0x7;}
	public static final int isp_cullingmode (int isp){ return (isp >> 27) & 0x3;}
	public static final int isp_zwrite(int isp) { return (isp >> 26) & 0x1;}
	public static final int isp_texture(int isp) { return (isp >> 25) & 0x1;}
	public static final int isp_offset(int isp) { return (isp >> 24) & 0x1;}
	public static final int isp_gouraud(int isp) { return (isp >> 23) & 0x1;}
	public static final int isp_uv16bit(int isp) { return (isp >> 22) & 0x1;}
	public static final int isp_cachebypass(int isp) { return (isp >> 21) & 0x1; }
	public static final int isp_dcalcctrl(int isp) { return (isp >> 20) & 0x1; }
	  	 
	/*
	typedef union
	{
		struct
		{
			u32 texv        : 3;
			u32 texu        : 3;
			u32 shadinstr   : 2;
			u32 mipmapd     : 4;
			u32 supsample   : 1;
			u32 filtermode  : 2;
			u32 clampuv     : 2;
			u32 flipuv      : 2;
			u32 ignoretexa  : 1;
			u32 usealpha    : 1;
			u32 colorclamp  : 1;
			u32 fogctrl     : 2;
			u32 dstselect   : 1;
			u32 srcselect   : 1;
			u32 dstinstr    : 3;
			u32 srcinstr    : 3;
		};

		u32 all;

	} TSP;
	*/
	
	 public static final int tsp_texv(int tsp) { return (tsp & 0x7); };
	 public static final int tsp_texu(int tsp) { return (tsp >>> 3) & 0x7;};
	 public static final int tsp_shadinstr(int tsp) { return ((tsp >>> 6) & 0x3);};
	 public static final int tsp_mipmapd(int tsp) { return((tsp >>> 8) & 0xf); };
	 public static final int tsp_supsample(int tsp) { return((tsp >>> 12) & 0x1); };
	 public static final int tsp_filtermode(int tsp) { return((tsp >>> 13) & 0x3); };
	 public static final int tsp_clampuv(int tsp) { return((tsp >>> 15) & 0x3); };
	 public static final int tsp_flipuv(int tsp) { return ((tsp >>> 17) & 0x3);};
	 public static final int tsp_ignoretexa(int tsp) { return((tsp >>> 19) & 0x1); };
	 public static final int tsp_usealpha(int tsp) { return((tsp >>> 20) & 0x1); };
	 public static final int tsp_colorclamp(int tsp) { return((tsp >>> 21) & 0x1); };
	 public static final int tsp_fogctrl(int tsp) { return ((tsp >>> 22) & 0x3);};
	 public static final int tsp_dstselect(int tsp) { return ((tsp >>> 24) & 0x1);};
	 public static final int tsp_srcselect(int tsp) { return((tsp >>> 25) & 0x1); };
	 public static final int tsp_dstinstr(int tsp) { return ((tsp >>> 26) & 0x7);};
	 public static final int tsp_srcinstr(int tsp) { return ((tsp >>> 29) & 0x7);};
	
	/*
	typedef union
	{
		struct
		{
			u32 texaddr   : 21;
			u32 special   : 6;
			u32 pixelfmt  : 3;
			u32 vqcomp    : 1;
			u32 mipmapped : 1;
		};

		u32 all;

	} TCW;
	*/
	 public static final int tcw_mipmap(int tcw) { return (tcw >> 31) & 0x1;}
	 public static final int tcw_vq(int tcw) { return (tcw >> 30) & 0x1;}
	 public static final int tcw_pixelformat(int tcw) { return (tcw >> 27) & 0x7;}
	 public static final int tcw_texture_surface(int tcw) { return (tcw & 0xFFFFF) << 3;} // 20 bits
	 public static final int tcw_twiddled(int tcw) { return (tcw >> 26) & 0x1;}
	 public static final int tcw_stride(int tcw) { return (tcw >> 25) & 0x1;}
	 
		/*
		 * 
		 * type 0: non-textured, packed-colour:
		 * type 1: non-textured, floating colour:
		 * type 2: non-textured, intensity:
		 * type 3: textured, packed-colour:
		 * type 4: textured, packed-colour, 16bit UV:
		 * type 5: textured, floating colour:
		 * type 6: textured, floating colour, 16-bit UV
		 * type 7: textured, intensity
		 * type 8: textured, intensity, 16-bit UV
		 * type 9: non-textured, packed-color, affected by modifier volume
		 * type 10: non-textured, intensity, affected by modifier volume
		 * type 11: textured, packed-colour, affected by modifier volume:
		 * type 12: textured, packed-colour, 16-bit UV, affected by modifier volume
		 * type 13: textured, intensity, affected by modifier volume
		 * type 14: textured, intensity, 16-bit UV, affected by modifier volume
		 * type 15: non-textured sprite
		 * type 16: textured sprite
		 * type 17: shadow volume

		 */
		
		private static final int NonTextured_PC =0;
		private static final int NonTextured_FC = 1;
		private static final int NonTextured_Intensity = 2;
		private static final int Textured_PC = 3;
		private static final int Textured_PC_16UV = 4;
		private static final int Textured_FC = 5;
		private static final int Textured_FC_16UV = 6;
		private static final int Textured_Intensity = 7;
		private static final int Textured_Intensity_16UV = 8;
		private static final int NonTextured_PC_MV = 9;
		private static final int NonTextured_Intensity_MV = 10;
		private static final int Textured_PC_MV =11;
		private static final int Textured_PC_16UV_MV =12;
		private static final int Textured_Intensity_MV =13;
		private static final int Textured_Intensity_16UV_MV =14;
		private static final int NonTextured_sprite =15;
		private static final int Textured_sprite =16;
		private static final int ShadowVolume =17;
	
	 /*
	  * this is more than enough..the dreamcast has 8 megabytes of video ram..the same i'm allocating here
	  * I don't have any idea of how many polygons i can there will be so just to be safe..
	  */
	 
	private static final FloatBuffer VertexBufferData = BufferUtils.createFloatBuffer(8 * 1024 * 1024);
	
	public static final int ObjectBufferSize = 65500;
	
	/*
	 *  A strip as 4 for ints for:
	 *  	pcw,isp,tsp,tcw
	 *  We will also need to store info for base color,extended color,vstart(position in the vbo where it starts
	 *  ),vcount (number of vertices)
	 *  We multiply by 8 to say that we can ObjectBufferSize polygons as we save 8 elements of each.
	 */
	
	public static final int list_pcw = 0;
	public static final int list_isp= 1;
	public static final int list_tsp= 2;
	public static final int list_tcw= 3;
	public static final int list_basecolor= 4;
	public static final int list_extended_color= 5;
	public static final int list_vstart= 6;
	public static final int list_vcount= 7;
	
	public static final int [] TriangleListData = new int [ObjectBufferSize * 8];
	private static int allocatedObjects=0;
	private static int pvr_registered;
	private static boolean vertexstart;
	private static int TotalVertexCount;
	

	/*
	 * Some vertex types require more than the 8 dwords of a SQ  to provide all of its info.
	 * If we're dealing with PVR dma the info should be right after but in the case of SQ a new
	 * call must be efectuaded.
	 * The integer below if !(-1) will contain the vertex type in question
	 */
	private static int VertexNotFinished =-1;
	
	/*
	 * The Texture Cache
	 */
	
	private static final TextureCache textureCache = new TextureCache();
	
	private static int createVBOID() {
		  if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
		    IntBuffer buffer = BufferUtils.createIntBuffer(1);
		    ARBVertexBufferObject.glGenBuffersARB(buffer);
		    return buffer.get(0);
		  }
		  return 0;
		}

	public static void bufferData(int id, FloatBuffer buffer) {
		  if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
		    ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
		    ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, ARBVertexBufferObject.GL_STREAM_DRAW_ARB);
		  }
		}
	
	public static void init(){
		int id = createVBOID();
		bufferData(id, VertexBufferData);
	}
	
	public static void render(){
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		GL11.glEnableClientState(GL14.GL_SECONDARY_COLOR_ARRAY);
		
		GL11.glDepthMask(true);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		// vertices
		// 0 as its the first in the chunk, i.e. no offset. * 4 to convert to bytes.
		GL11.glVertexPointer(3, GL11.GL_FLOAT, strideVBO, 0);
		 
		//offset = 3 * 4; // 3 components is the initial offset from 0, then convert to bytes
		GL11.glColorPointer(4, GL11.GL_FLOAT, strideVBO, 12);
		
		GL14.glSecondaryColorPointer(4, GL11.GL_FLOAT, strideVBO, 28);	 
				 
		// texture coordinates
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, strideVBO, 44);
		
		for(int i =0,index=0; i < allocatedObjects; i++){
			// each object is 8 words in size
			index = i << 3; 
			setUpTriangleStrip(index);
			GL11.glDrawArrays(GL11.GL_TRIANGLES,TriangleListData[index+list_vstart],index+list_vcount);
		}
		allocatedObjects =0;
		try {
			Display.swapBuffers();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		 Emu.getInterruptController().addInterrupts(Intc.ASIC_EVT_PVR_RENDERDONE);
	}
	
	/*
	 * the following code was adapted from dcemu 
	 * and completed with Mr Lars Olson document ta_intro.txt
	 * which can be found at http://www.ludd.luth.se/~jlo/dc/
	 */ 
	private static void TileAccelaratorPolygonModifier(IntBuffer input, int pos)
	{
		final int base_pos = allocatedObjects << 3;
		
		
		// pcw,isp,tsp and tcw are now on our triangle list data
		for(int i =0; i < 4; i++){
			TriangleListData[base_pos+i] = input.get(pos+i);
		}
		
		final int pcw=  TriangleListData[base_pos];
		
		System.out.println( "pcw: Polygon or Modifier Volume\n");

		// what kind of data we're treating
		pvr_registering = pcw_list_type(pcw);

		// group control
		if (pcw_group_en(pcw) !=0)
		{
			System.out.println( "pcw: group_en\n");
			switch(pcw_strip_len(pcw))
			{
				case 0: System.out.println( "pcw: strip_len: 1\n");	break;
				case 1: System.out.println( "pcw: strip_len: 2\n");	break;
				case 2: System.out.println( "pcw: strip_len: 4\n");	break;
				case 3: System.out.println( "pcw: strip_len: 6\n");	break;
			}
			switch(pcw_user_clip(pcw))
			{
				case 0: System.out.println( "pcw: user_clip: disable\n");	break;
				case 1: System.out.println( "pcw: user_clip: reserved\n");	break;
				case 2: System.out.println( "pcw: user_clip: inside enable\n");	break;
				case 3: System.out.println( "pcw: user_clip: outside enable\n");	break;
			}
		}
			
		// obj control

		global_parameter = vertex_parameter = -1;

		if (pcw_texture(pcw) == 0)
		{
			
			if (pcw_volume(pcw) == 0)
			{
				switch(pcw_col_type(pcw))
				{
					case 0:
					global_parameter = 0;
					vertex_parameter = 0;
					break;
				
					case 1:
					global_parameter = 0;
					vertex_parameter = 1;
					break;
				
					case 2:
					global_parameter = 1;
					vertex_parameter = 2;
					break;
				
					case 3:
					global_parameter = 0;
					vertex_parameter = 2;
					break;
				}
			}
			else
			{
				switch (pcw_col_type(pcw))
				{
					case 0:
					global_parameter = 3;
					vertex_parameter = 9;
					break;
				
					case 2:
					global_parameter = 4;
					vertex_parameter = 10;
					break;
				
					case 3:
					global_parameter = 3;
					vertex_parameter = 10;
					break;
				}
			}
		}
		else // textured
		{
			if (pcw_volume(pcw) == 0)
			{
				switch(pcw_col_type(pcw))
				{
					case 0:
					if (pcw_16bit_UV(pcw) == 0)
					{
						global_parameter = 0;
						vertex_parameter = 3;
					}
					else
					{
						global_parameter = 0;
						vertex_parameter = 4;
					}
					break;
					
					case 1:
					if (pcw_16bit_UV(pcw) == 0)
					{
						global_parameter = 0;
						vertex_parameter = 5;
					}
					else
					{
						vertex_parameter = 6;
						global_parameter = 0;
					}
					break;
					
					case 2:
					if (pcw_16bit_UV(pcw) == 0)
					{
						if (pcw_offset(pcw) == 0)
						{
							global_parameter = 1;
							vertex_parameter = 7;
						}
						else
						{
							global_parameter = 2;
							vertex_parameter = 7;
						}
					}
					else
					{
						if (pcw_offset(pcw) == 0)
						{
							global_parameter = 1;
							vertex_parameter = 8;
						}
						else
						{
							global_parameter = 2;
							vertex_parameter = 8;
						}
					}
					break;
						
					case 3:
					if (pcw_16bit_UV(pcw) == 0)
					{
						global_parameter = 0;
						vertex_parameter = 7;
					}
					else
					{
						global_parameter = 0;
						vertex_parameter = 8;
					}
					break;
				}
			}
			else
			{
				switch(pcw_col_type(pcw))
				{
					case 0:
					if (pcw_16bit_UV(pcw) == 0)
					{
						global_parameter = 3;
						vertex_parameter = 11;
					}
					else
					{
						global_parameter = 3;
						vertex_parameter = 12;
					}
					break;
					
					case 2:
					if (pcw_16bit_UV(pcw) == 0)
					{
						global_parameter = 4;
						vertex_parameter = 13;
					}
				else
					{
						global_parameter = 4;
						vertex_parameter = 14;
					}
					break;
					
					case 3:
					if (pcw_16bit_UV(pcw) == 0)
					{
						global_parameter = 3;
						vertex_parameter = 13;
					}
					else
					{
						global_parameter = 3;
						vertex_parameter = 14;
					}
					break;
				}
			}
		}
	}
	
	private static void setUpTriangleStrip(int object){
		final int pcw = TriangleListData[object + list_pcw];
		final int isp = TriangleListData[object + list_isp];
		final int tsp = TriangleListData[object + list_tsp];
		final int tcw = TriangleListData[object + list_tcw];
		// from swirly
		GL11.glDepthFunc(pcw_list_type(TriangleListData[pcw])); 
	
		if(isp_cullingmode(TriangleListData[isp])!=0)
		{
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
		}
		else
		{
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
		
		if(isp_zwrite(TriangleListData[isp])!=0)
		{
			GL11.glDepthMask(false);
		}
		else
		{
			GL11.glDepthMask(true);
		}

		/*
		 * from swirly line 381 of the gpu.cpp file
		 */
		if(tsp_usealpha(TriangleListData[tsp])!=0)
		{
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
		else
		{
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		}

		GL11.glBlendFunc(blendFunc[tsp_srcinstr(TriangleListData[tsp])],blendFunc[tsp_dstinstr(TriangleListData[tsp])]);

		// there is a texture attached to this polygon
		if(pcw_texture(pcw) !=0)
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			if(tsp_filtermode(TriangleListData[tsp]) !=0)
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			}
			else
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			}

			textureCache.createTexture(tsp,tcw);
		}
		else
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	
	}
	
	public static void ppblocksize(int input)
	{
	  int punch_through = (input >> 16) & 0x3;
	  int transmod = (input >> 12) & 0x3;
	  int transpoly = (input >> 8) & 0x3;
	  int opaquemod = (input >> 4) & 0x3;
	  int opaquepoly = (input >> 0) & 0x3;
	  
	   
	  pvr_registered = 0;
	  
	  if (punch_through > 0)
		  pvr_registered |= 1 << 4; // enable 
	  else
		  pvr_registered &= ~(1 << 4);//disable
	  
	  if (transmod > 0)
		  pvr_registered |= 1 << 3;
	  else
		  pvr_registered &= ~(1 << 3);//disable
	  
	  if (transpoly > 0)
		  pvr_registered |= 1 << 2;
	  else
		  pvr_registered &= ~(1 << 2);//disable
	
	  if (opaquemod > 0)
		  pvr_registered |= 1 << 1;
	  else
		  pvr_registered &= ~(1 << 1);//disable
	   
	  if (opaquepoly > 0)
		  pvr_registered |= 0x1;
	  else
		  pvr_registered &= ~(0x1);//disable
	  }
	
	private static void TaListEnd()
	{
		 if (pvr_registering != -1)
		 {
			//pvr_listdone |= (1 << pvr_registering);
			Emu.getInterruptController().addInterrupts(pvr_lists[pvr_registering]);
		 	pvr_registering = -1;
		  }
	  }


	
	private static void TileAccelaratorVertexHandler(IntBuffer buffer,int pos){
		System.out.println("Vertex Handler");
		final int base_index = allocatedObjects << 3;
		final int pcw = buffer.get(pos);
		if(vertexstart == true)
		{
			TriangleListData[base_index + list_vstart] = TotalVertexCount+1;

			vertexstart = false;
		}

		final int base_colour = buffer.get(pos+6);
		switch (vertex_parameter)
		{
			case NonTextured_PC: // Non-Textured, Packed Color
			{
		
				TotalVertexCount++;

				VertexBufferData.put(TotalVertexCount,buffer.get(pos+1));

				VertexBufferData.put(TotalVertexCount + vert_y,buffer.get(pos+2));

				VertexBufferData.put(TotalVertexCount + vert_z , 1.0f / buffer.get(pos+3));
			
				VertexBufferData.put(TotalVertexCount + color_a , ((base_colour >> 24) & 0xFF) / 255.0f);

				VertexBufferData.put(TotalVertexCount + color_r ,((base_colour >> 16) & 0xFF) / 255.0f);

				VertexBufferData.put(TotalVertexCount + color_g ,  ((base_colour >> 8)  & 0xFF) / 255.0f);

				VertexBufferData.put(TotalVertexCount +color_b , ((base_colour >> 0)  & 0xFF) / 255.0f);
			}
			break;

			case NonTextured_FC:
			{			
				
				TotalVertexCount++;
			
				VertexBufferData.put(TotalVertexCount + vert_x , buffer.get(pos+1));

				VertexBufferData.put(TotalVertexCount + vert_y , buffer.get(pos+2));

				VertexBufferData.put(TotalVertexCount + vert_z , 1.0f / buffer.get(pos+3));

				VertexBufferData.put(TotalVertexCount + color_a , buffer.get(pos+4));

				VertexBufferData.put(TotalVertexCount + color_r , buffer.get(pos+5));

				VertexBufferData.put(TotalVertexCount + color_g , buffer.get(pos+6));

				VertexBufferData.put(TotalVertexCount + color_b ,buffer.get(pos+7));
			}
			break;
					
			case Textured_PC:
			{			
				TotalVertexCount++;

				VertexBufferData.put(TotalVertexCount + vert_x ,buffer.get(pos+1));

				VertexBufferData.put(TotalVertexCount + vert_y , buffer.get(pos+2));

				VertexBufferData.put(TotalVertexCount + vert_z , 1.0f / buffer.get(pos+3)); 

				VertexBufferData.put(TotalVertexCount + tc_x , buffer.get(pos+4));

				VertexBufferData.put(TotalVertexCount + tc_y , buffer.get(pos+5));		

				VertexBufferData.put(TotalVertexCount + color_a , ((base_colour >> 16) & 0xFF) / 255.0f);

				VertexBufferData.put(TotalVertexCount + color_r , ((base_colour >> 16) & 0xFF) / 255.0f);

				VertexBufferData.put(TotalVertexCount + color_g ,  ((base_colour >> 8)  & 0xFF) / 255.0f);

				VertexBufferData.put(TotalVertexCount + color_b , ((base_colour >> 0)  & 0xFF) / 255.0f);
			}
			break;
			case 4:
				

			break;
			
			case Textured_FC:
			{						
				TotalVertexCount++;	

				VertexBufferData.put(TotalVertexCount + vert_x , buffer.get(pos+1));

				VertexBufferData.put(TotalVertexCount + vert_y , buffer.get(pos+2));

				VertexBufferData.put(TotalVertexCount + vert_z , 1.0f /  buffer.get(pos+3));

				VertexBufferData.put(TotalVertexCount + tc_x , buffer.get(pos+4));

				VertexBufferData.put(TotalVertexCount + tc_y , buffer.get(pos+5));
				
				VertexNotFinished = Textured_FC;

			}
			break;
			
			default:
			{
				System.out.println("THE FOLLOWING VERTEX TYPE HAS NOT BEEN IMPLEMENTED YET: " + vertex_parameter);
			}
			break;
		}
		
		if (pcw_endofstrip(pcw) !=0)
		{
			TriangleListData[allocatedObjects+list_vcount] = ((TotalVertexCount+1) - TriangleListData[allocatedObjects+list_vstart]);
			
			allocatedObjects++;

			vertexstart = true;
		}
	}
	
	private static final void FinalizeVertex(IntBuffer buffer,int pos){
		switch (VertexNotFinished) {
		case Textured_FC:
			VertexBufferData.put(TotalVertexCount +color_a , buffer.get(pos));

			VertexBufferData.put(TotalVertexCount + color_r , buffer.get(pos+1)); 

			VertexBufferData.put(TotalVertexCount + color_g , buffer.get(pos+2));

			VertexBufferData.put(TotalVertexCount + color_b , buffer.get(pos+3));
			
			VertexBufferData.put(TotalVertexCount + extcolor_a , buffer.get(pos+4));

			VertexBufferData.put(TotalVertexCount + extcolor_r , buffer.get(pos+5)); 

			VertexBufferData.put(TotalVertexCount + extcolor_g , buffer.get(pos+6));

			VertexBufferData.put(TotalVertexCount + extcolor_b , buffer.get(pos+7));
			
			VertexNotFinished = -1;
			break;

		default:
			System.out.println("Not yet implemented Vertex finalizer");
			break;
		}
	}
	
	// from dcemu
	private static void doUserClip()
	{/*
		System.out.println( "pcw: User Tile Clip\n");
		System.out.println( "USER_CLIP: Xmin: %x\n", ta_address_pointer[4]);
		System.out.println( "USER_CLIP: Ymin: %x\n", ta_address_pointer[5]);
		System.out.println( "USER_CLIP: Xmax: %x\n", ta_address_pointer[6]);
		System.out.println( "USER_CLIP: Ymax: %x\n", ta_address_pointer[7]);
		System.out.println( "USER_CLIP: NO IMPLEMENTADO\n");*/
	}
	//from dcemu
	private static void objectListSet()
	{
		System.out.println("Object List set not implemented");
	}

	/*
	 * input is the buffer (SQ or main mem) contain the TA input info.
	 * size is the number of dwords we're meant to consider
	 */
	public static final void TaTransfer(IntBuffer input, int size){
		int control_word=0;
		int position = input.position();
		System.out.println("TA TRANSFER");
		for(int i = size; i  !=0;){
			if(VertexNotFinished != -1){
				/*
				 * position shall be 0 when handling with SQ and the base address in mem when with PVR-DMA
				 */
				FinalizeVertex(input,position);
			}
			else{
				control_word = pcw_para_type(input.get(position));
				System.out.print("Control Word " + control_word);
				switch (control_word) {
					case 0:	TaListEnd();break;	//END_OF_LIST
					case 1:	doUserClip	 ();	break;	//USER CLIP
					case 2:	objectListSet();	break;	//OBJECT_LIST_SET
					case 4:	TileAccelaratorPolygonModifier(input, position);break;	//POLYGON
					//case 5://SPRITE
					case 7:	TileAccelaratorVertexHandler(input, position);break;	//VERTEX
				}
				position += size;
				i -= size;
			}
		}
	}
}

