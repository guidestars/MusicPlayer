package com.xu.roboto.entity;


public class MusicList{
	
	private String songname;
	private String artistname;
	private String songid;
	
	public MusicList(String songname, String artistname, String songid) {
		this.songname = songname;
		this.artistname = artistname;
		this.songid = songid;
	}
	

	public String getSongname() {
		return songname;
	}

	public void setSongname(String songname) {
		this.songname = songname;
	}

	public String getArtistname() {
		return artistname;
	}

	public void setArtistname(String artistname) {
		this.artistname = artistname;
	}

	public String getSongid() {
		return songid;
	}

	public void setSongid(String songid) {
		this.songid = songid;
	}

	
}
