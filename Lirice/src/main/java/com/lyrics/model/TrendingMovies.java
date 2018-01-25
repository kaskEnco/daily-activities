package com.lyrics.model;

import java.io.Serializable;

public class TrendingMovies implements Serializable{

	private int movieId;
	private String movieName;
	private int lyricViews;
	private String lyric_name;
	private String writerName;

	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public int getLyricViews() {
		return lyricViews;
	}

	public void setLyricViews(int lyricViews) {
		this.lyricViews = lyricViews;
	}

	public String getLyric_name() {
		return lyric_name;
	}

	public void setLyric_name(String lyric_name) {
		this.lyric_name = lyric_name;
	}
}
