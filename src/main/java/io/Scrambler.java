package io;

/*
 * This is a Java implementation of Marcus Comsted dreamcast binary disassembler
 */

public final class Scrambler {
	
	public static final int MAXCHUNK = 2048*1024;

	private static int seed;
	
	private static int idx[] = new int [MAXCHUNK/32];
	
	private byte [] buffer;
	
	private byte [] original;
	
	private int nextIndex=0;

	/* we increment this value by 32 each time we read from the original buffer */
	private int srcindex=0;
	
	/*short as in 16 bit rand*/
	void my_srand(int n)
	{
	  seed = n & 0xffff;
	}

	private static int my_rand()
	{
	  seed = (seed * 2109 + 9273) & 0x7fff;
	  return (seed + 0xc000) & 0xffff;
	}
	
	Scrambler(byte [] b){
		buffer = new byte [b.length];
		original = b;
	}
	
	private final void load(int index,int sz){
		System.arraycopy(original, srcindex, buffer, index, sz);
		srcindex += sz;
	}
	
	/*
	 * Arguments: the original buffer and the file size
	 * 
	 */
	
	private final void load_chunk(int sz)
	{
	  int i;

	  /* Convert chunk size to number of slices */
	  sz /= 32;

	  /* Initialize index table with unity,
	     so that each slice gets loaded exactly once */
	  for(i = 0; i < sz; i++)
	    idx[i] = i;

	  for(i = sz-1; i >= 0; --i)
	    {
	      /* Select a replacement index */
	      int x = (my_rand() * i) >> 16;

	      /* Swap */
	      int tmp = idx[i];
	      idx[i] = idx[x];
	      idx[x] = tmp;

	      /* Load resulting slice */
	      load(nextIndex+32*idx[i],32);
	    }
	}
	
	private void load_file(int filesz)
	{
	  int chunksz;

	  my_srand(filesz);

	  /* Descramble 2 meg blocks for as long as possible, then
	     gradually reduce the window down to 32 bytes (1 slice) */
	  for(chunksz = MAXCHUNK; chunksz >= 32; chunksz >>= 1)
	    while(filesz >= chunksz)
	      {
		load_chunk(chunksz);
		filesz -= chunksz;
		nextIndex += chunksz;
	    }

	  /* Load final incomplete slice */
	  if(filesz >0 )
	    load(nextIndex,filesz);
	}
	
	
	/*
	 * Receives as argument the buffer containing the binary data
	 */
	public byte [] descramble(){
		load_file(original.length);
		return buffer;
	}
}
