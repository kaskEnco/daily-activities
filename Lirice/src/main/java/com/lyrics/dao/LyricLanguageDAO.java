package com.lyrics.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.lyrics.model.L_language;

public class LyricLanguageDAO extends BaseDAO {

	public L_language findById(int langId) {
		List<L_language> lang = findAllLanguges();
		L_language language = new L_language() ;
		if(lang != null) {
			for (L_language list : lang) {
				if (list.getLangId() == langId) {
					 return list;
				}
			}
		}
		
		
		/*if (lang == null) {
			language = new L_language();
			PreparedStatement ptmt = null;
			ResultSet resultSet = null;
			String queryString = "SELECT * FROM l_language where id = ?  ";
			try {
				connection = getConnection();
				ptmt = connection.prepareStatement(queryString);
				ptmt.setInt(1, langId);
				resultSet = ptmt.executeQuery();
				while (resultSet.next()) {
					language.setLangId(resultSet.getInt("id"));
					language.setLanguage(resultSet.getString("lang_name"));

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				closeResultset(resultSet);
				closePtmt(ptmt);
				closeConnection();
			}
		} */
		return language;
	}
}
