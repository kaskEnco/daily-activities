package com.lyrics.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class L_movie implements Serializable{

	private int movieId;
	private L_year year;
	
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public L_year getYear() {
		return year;
	}
	public void setYear(L_year i) {
		this.year = i;
	}
	public L_language getLanguage() {
		return language;
	}
	public void setLanguage(L_language language) {
		this.language = language;
	}
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public Timestamp getMovieReleaseDate() {
		return movieReleaseDate;
	}
	public void setMovieReleaseDate(Timestamp movieReleaseDate) {
		this.movieReleaseDate = movieReleaseDate;
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
	private L_language language;
	private String movieName;
	private Timestamp movieReleaseDate;
	private Timestamp creationDate;
	private Timestamp updationDate;


}
