package it.lma5.incorporesound;

public class Song {

	private String name;
	private String path;
	
	private Integer beginTime;
	private Integer duration;
	
	public Song(String name, String path, Integer beginTime, Integer duration) {
		
		this.name = name;
		this.path = path;
		this.beginTime = beginTime;
		this.duration = duration;
	}
	
	
}
