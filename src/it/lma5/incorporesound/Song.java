package it.lma5.incorporesound;

import android.net.Uri;

public class Song {

	private String name;
	private String artist;
	private Uri path;

	private Integer beginTime;
	private Integer duration;
	private Integer userDuration;

	public Song(String name, Uri path, Integer beginTime, Integer userDuration,
			Integer duration, String artist) {
		if (name == null) {
			this.name = splitUri(path);
		} else {
			this.name = name;
		}
		this.path = path;
		this.beginTime = beginTime;
		this.duration = duration;
		this.userDuration = userDuration;

		if (artist == null) {
			this.artist = "Unknown artist";
		} else {
			this.artist = artist;
		}
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Integer getUserDuration() {
		return userDuration;
	}

	public void setUserDuration(Integer userDuration) {
		this.userDuration = userDuration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Uri getPath() {
		return path;
	}

	public void setPath(Uri path) {
		this.path = path;
	}

	public Integer getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Integer beginTime) {
		this.beginTime = beginTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	private String splitUri(Uri uri) {
		String uriToS = uri.toString();
		String ret = uriToS.substring(uriToS.lastIndexOf("/"));
		
		return ret;
		
	}

}
