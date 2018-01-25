package com.lyrics.model;

public class LyircsByMovie {

	private int lyricId;
	private String lyricTitle;
	private int movieId;
	private String writerName;

	public int getLyricId() {
		return lyricId;
	}

	public void setLyricId(int lyricId) {
		this.lyricId = lyricId;
	}

	public String getLyricTitle() {
		return lyricTitle;
	}

	public void setLyricTitle(String lyricTitle) {
		this.lyricTitle = lyricTitle;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}

}
