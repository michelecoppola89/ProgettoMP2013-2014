package it.lma5.incorporesound;

import java.io.Serializable;

import android.net.Uri;

public class Song implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String artist;
	private Uri path;

	private Integer beginTime;     //mSec
	private Integer duration;      //mSEc
	private Integer userDuration;  //sec

	private Integer id;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getLastTimeMillis() {
		return beginTime+userDuration*1000;
	}


}
