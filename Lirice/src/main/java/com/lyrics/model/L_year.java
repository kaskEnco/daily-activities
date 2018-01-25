package com.lyrics.model;

import java.io.Serializable;

public class L_year implements Serializable{
	// @Id
	// private Long id;
	//
	// @Column(name = "lyric_year")
	// private Long lyircYear;

	private int yearId;
	private int lyircYear;

	
	public int getYearId() {
		return yearId;
	}

	public void setYearId(int yearId) {
		this.yearId = yearId;
	}

	public int getLyircYear() {
		return lyircYear;
	}

	public void setLyircYear(int lyircYear) {
		this.lyircYear = lyircYear;
	}

}
