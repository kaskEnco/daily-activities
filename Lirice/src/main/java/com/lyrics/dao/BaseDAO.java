package com.lyrics.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.lyrics.Constants;
import com.lyrics.model.L_language;
import com.lyrics.model.L_lyrics;
import com.lyrics.model.L_movie;
import com.lyrics.model.L_year;

import net.spy.memcached.MemcachedClient;

public class BaseDAO {

	Connection connection = null;
	MemcachedClient cache = null;

	public Connection getConnection() throws SQLException {
		if (connection == null) {
			// System.out.println("creating new connection");
			connection = LyricConnectionFactory.getInstance().getConnection();
		}
		return connection;
	}

	public MemcachedClient getMemcacheConnection() {
		try {
			if (cache == null) {
				cache = MemcachedConncetionFactory.getInstance().getConnection();
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return cache;
	}

	public BaseDAO() {

	}

	public Timestamp getTimestamp(Timestamp fromDb) {
		Date date = new Date(Long.valueOf(fromDb.toString()));
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatted = format.format(date);
		Timestamp timeStamp = Timestamp.valueOf(formatted);
		return timeStamp;
	}

	public void closePtmt(PreparedStatement ptmt) {

		try {
			if (ptmt != null)
				ptmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void closeConnection() {

		// System.out.println("local connection : " + connection1);
		// System.out.println("global connection : " + connection);
		try {

			if (connection != null)
				connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection = null;
		}
	}

	public static void closeResultset(ResultSet resultSet) {

		try {
			if (resultSet != null)
				resultSet.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String yearsList = "years";

	public List<L_year> findAllYears() {
		List<L_year> years = null;
		if(Constants.USE_MEMCACHED) {
			cache = getMemcacheConnection();
			years = (List<L_year>) cache.get(Constants.YEARS_KEY);
		}
		
		if (years != null)
			return years;

		years = new ArrayList<L_year>();
		L_year year = null;
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "SELECT * FROM l_year ";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				year = new L_year();
				year.setYearId(resultSet.getInt("id"));
				year.setLyircYear(resultSet.getInt("lyric_year"));
				years.add(year);

			}
			cache.add(Constants.YEARS_KEY, 1200, years);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}

		return years;

	}

	String langList = "languages";

	@SuppressWarnings({ "unchecked", "null" })
	public List<L_language> findAllLanguges() {
		List<L_language> languages = null;
		L_language lang;
		if (Constants.USE_MEMCACHED) {
			cache = getMemcacheConnection();
			languages = (List<L_language>) cache.get(Constants.LANG_LIST);
		}

		if (languages != null)
			return languages;

		lang = new L_language();
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "SELECT * FROM l_language ";
		try {

			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			languages = new ArrayList<L_language>();
			while (resultSet.next()) {
				lang.setLangId(resultSet.getInt("id"));
				lang.setLanguage(resultSet.getString("lang_name"));
				languages.add(lang);
			}
			cache.add(Constants.LANG_LIST, 1200, languages);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return languages;
	}

	public List<L_lyrics> findAllLyrics() {
		List<L_lyrics> lyrics;
		if (Constants.USE_MEMCACHED) {
			cache = getMemcacheConnection();
			lyrics = (List<L_lyrics>) cache.get(Constants.LYRIC_CONTENT);
		}

		if (lyrics != null)
			return lyrics;

		lyrics = new ArrayList<L_lyrics>();
		LyricMovieDAO movieDAO = new LyricMovieDAO();
		L_lyrics lyric = null;
		String queryString = "SELECT * FROM l_lyrics ";
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				lyric = new L_lyrics();
				lyric.setLyricId(resultSet.getInt("id"));
				lyric.setLyricTitle(resultSet.getString("lyric_title"));
				lyric.setLyricContent(resultSet.getString("lyric_content"));
				lyric.setMovie(movieDAO.findById(resultSet.getInt("movie_id")));
				lyric.setWriterName(resultSet.getString("writer_name"));
				lyric.setLyricViews(resultSet.getInt("lyric_views"));
				lyric.setCreationDate(resultSet.getTimestamp("creation_time"));
				lyric.setUpdationDate(resultSet.getTimestamp("updation_time"));
				lyrics.add(lyric);
			}
			cache.add(Constants.LYRIC_CONTENT, 1200, lyrics);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}

		return lyrics;
	}

	
	public List<L_movie> findAllMovies() {
		List<L_movie> movies;
		if (Constants.USE_MEMCACHED) {
			cache = getMemcacheConnection();
			movies = (List<L_movie>) cache.get(Constants.MOVIES_LIST);
		}

		if (movies != null)
			return movies;

		
		movies = new ArrayList<L_movie>();
		LyricLanguageDAO langDAO = new LyricLanguageDAO();
		LyricYearDAO yearDAO = new LyricYearDAO();
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "SELECT * FROM l_movie ";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery(); 
			while (resultSet.next()) {
				L_movie movie = new L_movie();
				movie.setMovieId(resultSet.getInt("id"));
				movie.setLanguage(langDAO.findById(resultSet.getInt("lang_id")));
			    movie.setYear(yearDAO.findById(resultSet.getInt("movie_year_id")));
				movie.setMovieName(resultSet.getString("movie_name"));
				movie.setMovieReleaseDate(resultSet.getTimestamp("movie_release_date"));
				movie.setCreationDate(resultSet.getTimestamp("creation_time"));
				movie.setUpdationDate(resultSet.getTimestamp("updation_time"));
				movies.add(movie);
			}
			cache.add(Constants.MOVIES_LIST, 1200, movies);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}

		return movies;
	}

}
