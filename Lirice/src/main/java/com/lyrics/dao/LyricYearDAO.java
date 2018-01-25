package com.lyrics.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyrics.model.L_year;

public class LyricYearDAO extends BaseDAO {

	public L_year findById(int Id) {
		List<L_year> years = findAllYears();
		L_year yearObj = null;

			for (L_year list : years) {
				if (list.getYearId() == Id) {
					yearObj = list;
				}
			}

		return yearObj;
	
	}

	public L_year findByYear(int year) {
		 L_year yearsObj = null ;
		 List<L_year> years = findAllYears();
		 if (years != null) {
			 for (L_year list : years) {
			 if (list.getLyircYear() == year) {
			   yearsObj = list;
			 }
		 }
		 
		 }
		return yearsObj;
	}

}
