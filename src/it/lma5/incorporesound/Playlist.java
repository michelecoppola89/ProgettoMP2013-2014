package it.lma5.incorporesound;

import java.util.ArrayList;

public class Playlist {
	
	private String name;
	private ArrayList<Song> songList;
	private Integer round;
	private boolean is_random;
	private Integer fadeIn;
	
	
	public Playlist(String name, ArrayList<Song> songList, Integer round,
			boolean is_random, Integer fadeIn) {
		
		this.name = name;
		this.songList = songList;
		this.round = round;
		this.is_random = is_random;
		this.fadeIn = fadeIn;
	}

	

	public String getName() {
		
		return this.name;
	}

}
