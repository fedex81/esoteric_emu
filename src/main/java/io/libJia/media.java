package io.libJia;

import com.sun.jna.Structure;

public class media extends Structure{

	track TrackList []; // we will have max 2
	int num_tracks;
	int isMultisession;
	
	public static media gdImage;
}
