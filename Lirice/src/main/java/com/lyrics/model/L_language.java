package com.lyrics.model;

import java.io.Serializable;

public class L_language implements Serializable{

	private int langId;
	private String language;

	

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
