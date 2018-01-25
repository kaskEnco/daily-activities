package com.lyrics.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class L_lyrics implements Serializable{

	private int lyricId;
	private String lyricTitle;
	private String lyricContent;
	private L_movie movie;
	private String writerName;
	private int lyricViews;
	private Timestamp creationDate;
	private Timestamp updationDate;

	

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

	public String getLyricContent() {
		return lyricContent;
	}

	public void setLyricContent(String lyricContent) {
		this.lyricContent = lyricContent;
	}

	public L_movie getMovie() {
		return movie;
	}

	public void setMovie(L_movie movie) {
		this.movie = movie;
	}

	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}

	public int getLyricViews() {
		return lyricViews;
	}

	public void setLyricViews(int lyricViews) {
		this.lyricViews = lyricViews;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getUpdationDate() {
		return updationDate;
	}

	public void setUpdationDate(Timestamp updationDate) {
		this.updationDate = updationDate;
	}

}
