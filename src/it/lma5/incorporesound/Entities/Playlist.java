package it.lma5.incorporesound.Entities;

import java.io.Serializable;
import java.util.ArrayList;




/**
 * This class represents Playlist object.
 * 
 * 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 *
 
 */
public class Playlist implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Song> songList;
	private Integer round;
	private boolean is_random;
	private Integer fadeIn;       //(seconds)   
	
	/**
	 * @param name Playlist's name
	 * @param songList List of playlist's songs
	 * @param round number of playlist repetition, 0 stands for infinite loop
	 * @param is_random boolean value, Random playing mode
	 * @param fadeIn fade in and fade out are set to fadeIn/2 
	 */
	public Playlist(String name, ArrayList<Song> songList, Integer round,
			boolean is_random, Integer fadeIn) {
		
		this.name = name;
		this.songList = songList;
		this.round = round;
		this.is_random = is_random;
		this.fadeIn = fadeIn;
	}
	

	public Playlist(){
		
	}

	public ArrayList<Song> getSongList() {
		return songList;
	}



	public void setSongList(ArrayList<Song> songList) {
		this.songList = songList;
	}



	public Integer getRound() {
		return round;
	}



	public void setRound(Integer round) {
		this.round = round;
	}



	public boolean is_random() {
		return is_random;
	}



	public void setIs_random(boolean is_random) {
		this.is_random = is_random;
	}



	public Integer getFadeIn() {
		return fadeIn;
	}



	public void setFadeIn(Integer fadeIn) {
		this.fadeIn = fadeIn;
	}



	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		
		return this.name;
	}

}
