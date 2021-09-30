package io;

import java.nio.MappedByteBuffer;
/*
 * Binary checker implementation in Java..don't quite remember the author of the original sources sorry (
 */

public final class BinChecker {
		
	private static final  byte [] abc1  = "abcdefghijklmnopqrstuvwxyz1234567890".getBytes();
	private static final byte [] abc2  = "abcdefghijklmnopqrstuvwxyz0123456789".getBytes();
	private static final byte [] abc3  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".getBytes();
	private static final  byte [] abc4  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes();
	private static final  byte [] abc5  = "1234567890abcdefghijklmnopqrstuvwxyz".getBytes();
	private static final byte [] abc6  = "0123456789abcdefghijklmnopqrstuvwxyz".getBytes();
	private static final byte [] abc7  = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes();
	private static final byte [] abc8  = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes();
	private static final byte [] temp  = "#...'...*...-.../...2...4...7...9...;...=...?...A...C...E...G...I...J...L...N...O...Q...R...T...U...W...X...Z...".getBytes();
	private static final byte [] temp2  = "0123456789abcdef....(null)..0123456789ABCDEF".getBytes();
	private static final byte [] bortmnt  = "0123456789ABCDEF....Inf.NaN.0123456789abcdef....(null)...".getBytes();
	private static final byte [] dreamsnes  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.0123456789-".getBytes();
	private static final byte [] tetris  = "abcdefghijklEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()".getBytes();
	private static final byte [] punch  = "PORTDEV INFOENBLSTATRADRTOUTDRQCFUNCEND".getBytes();
	private static final byte [] netbsd  = "$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~".getBytes();
	
	/*
	 * implements a rather naive
	 */
	private static final boolean naive_search(MappedByteBuffer text,byte [] pattern) {
		final int n = text.capacity();
		final int m = pattern.length;
		int i, j;
		   /* Searching */
		   for (j = 0; j < n - m; ++j) {
		      for (i = 0; i < m; ++i){
		      if (text.get(i+j) != pattern[i])
		         break;
		      }
		      if (i == m)
		    	  return true;
		   }
		   return false;
		}
	
	
	/*
	 * receives as input the byte array containing the the binary information
	 */
	public static boolean isUnscrambled(MappedByteBuffer buffer){
		return(naive_search(buffer,abc1)
		|| naive_search(buffer,abc2) 
		||naive_search(buffer,abc3)
		||naive_search(buffer,abc4)
		||naive_search(buffer,abc5)
		||naive_search(buffer,abc6)
		||naive_search(buffer,abc7)
		||naive_search(buffer,abc8)
		||naive_search(buffer,temp)
		||naive_search(buffer,temp2)
		||naive_search(buffer,bortmnt)
		||naive_search(buffer,dreamsnes)
		||naive_search(buffer,tetris)
		||naive_search(buffer,punch)
		||naive_search(buffer,netbsd));
	}
}