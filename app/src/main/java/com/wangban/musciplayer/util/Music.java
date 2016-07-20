package com.wangban.musciplayer.util;

public class Music {

	private long id;
	private String title;
	private String path;
	private int size;
	private int duration;
	private String album;
	private String artist;
	private String albumArtist;
	private boolean isplay=false;
	private int selectIndex=-1;

	public int getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}

	public boolean isplay() {
		return isplay;
	}

	public void setIsplay(boolean isplay) {
		this.isplay = isplay;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}

	@Override
	public String toString() {
		return "Music [id=" + id + ", title=" + title + ", path=" + path
				+ ", size=" + size + ", duration=" + duration + ", album="
				+ album + ", artist=" + artist + ", albumArtist=" + albumArtist
				+ "]";
	}

}
