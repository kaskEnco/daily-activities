package com.lyrics.dao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.lyrics.Constants;
import com.lyrics.model.L_lyrics;
import com.lyrics.model.L_movie;
import com.lyrics.model.LyircsByMovie;
import com.lyrics.model.LyricContent;
import com.lyrics.model.MoviesByWriter;
import com.lyrics.model.TrendingMovies;

import net.spy.memcached.MemcachedClient;

public class LyricContentDAO extends BaseDAO {

	public L_lyrics findById(int Id) {
		L_lyrics lyric = new L_lyrics();
		LyricMovieDAO movieDAO = new LyricMovieDAO();
		String queryString = "SELECT * FROM l_lyrics where id = ?  ";
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setInt(1, Id);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				lyric.setLyricId(resultSet.getInt("id"));
				lyric.setLyricTitle(resultSet.getString("lyric_title"));
				lyric.setLyricContent(resultSet.getString("lyric_content"));
				lyric.setMovie(movieDAO.findById(resultSet.getInt("movie_id")));
				lyric.setWriterName(resultSet.getString("writer_name"));
				lyric.setLyricViews(resultSet.getInt("lyric_views"));
				lyric.setCreationDate(resultSet.getTimestamp("creation_time"));
				lyric.setUpdationDate(resultSet.getTimestamp("updation_time"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}

		return lyric;
	}

	public List<LyircsByMovie> getLyricsByMovie(int movieId) {
		LyircsByMovie lyric;
		List<LyircsByMovie> allLyrics =null;
		
		List<L_lyrics> moviesList = findAllLyrics();
		if( moviesList != null) {
			allLyrics =  new ArrayList<LyircsByMovie>() ;
			for(L_lyrics list : moviesList) {
				if( movieId == list.getMovie().getMovieId()) {
					lyric = new LyircsByMovie();
					lyric.setLyricId(list.getLyricId());
					lyric.setLyricTitle(list.getLyricTitle());
					lyric.setMovieId(list.getMovie().getMovieId());
					lyric.setWriterName(list.getWriterName());
					allLyrics.add(lyric);
					break;
				}
					  
			}
		}
		 
		return allLyrics;
		/*LyircsByMovie lyric;
		List<LyircsByMovie> allLyrics = new ArrayList<LyircsByMovie>();
		String queryString = "Select * from l_lyrics where movie_id = ?";
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setInt(1, movieId);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				lyric = new LyircsByMovie();
				lyric.setLyricId(resultSet.getInt("id"));
				lyric.setLyricTitle(resultSet.getString("lyric_title"));
				lyric.setMovieId(resultSet.getInt("movie_id"));
				lyric.setWriterName(resultSet.getString("writer_name"));
				allLyrics.add(lyric);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}

		return allLyrics;
          */
	}

	public List<TrendingMovies> getTrendingLyrics() {
		List<TrendingMovies> lyrics= null;
		List<TrendingMovies> trendingLyrics = getAllTrendingMovies();
		
		if( trendingLyrics != null ) {
			int i=0;
			lyrics = new ArrayList<TrendingMovies>();
			for(TrendingMovies list : trendingLyrics) {
				if( i < Constants.LATEST_TRENDING_LIMIT) {
					lyrics.add(list);
					i++;
					continue;
				}
				break;
			}
		}
		return lyrics;
		
		/*LyricMovieDAO movieDAO = new LyricMovieDAO();
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "select * from l_lyrics order by lyric_views DESC limit 15 ";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				lyric = new TrendingMovies();
				lyric.setLyric_name(resultSet.getString("lyric_title"));
				L_movie movie = movieDAO.findById(resultSet.getInt("movie_id"));
				lyric.setMovieId(movie.getMovieId());
				lyric.setMovieName(movie.getMovieName());
				lyric.setWriterName(resultSet.getString("writer_name"));
				lyric.setLyricViews(resultSet.getInt("lyric_views"));
				trendingLyrics.add(lyric);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}
         
		return trendingLyrics; */
	}

	public Set<String> getWriter() {
		// TODO Auto-generated method stub

		TreeSet<String> writerSet = new TreeSet<String>();
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String query = "SELECT writer_name FROM lyrics.l_lyrics order by writer_name ASC";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(query);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				writerSet.add(resultSet.getString("writer_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}
		return writerSet;
	}

	public List<LyricContent> getLyrics(int lyricId) {
		// TODO Auto-generated method stub
		LyricContent content;
		List<LyricContent> lyrics = null;
		List<L_lyrics> allLyrics =  findAllLyrics();
		
		if( allLyrics != null ) {
			lyrics = new ArrayList<LyricContent>();
			for(L_lyrics list : allLyrics) {
				if( lyricId == list.getLyricId()) {
					content = new LyricContent();
					content.setLyricContent(list.getLyricContent());
					lyrics.add(content);
					break;
				}
			}
		}
		return lyrics;
		/*PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "Select lyric_content from l_lyrics where id = ?";

		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setInt(1, lyricId);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				content = new LyricContent();
				content.setLyricContent(resultSet.getString("lyric_content"));
				lyrics.add(content);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lyrics; */
	}

	public List<TrendingMovies> getAllTrendingMovies() {
		List<TrendingMovies> trendingLyrics;
		
		if(Constants.USE_MEMCACHED) {
			cache = getMemcacheConnection();
			trendingLyrics = (List<TrendingMovies>) cache.get(Constants.LATEST_TRENDING_KEY);
		}
		
		if (trendingLyrics != null)
			return trendingLyrics;

		TrendingMovies lyric;
		trendingLyrics = new ArrayList<TrendingMovies>();
		LyricMovieDAO movieDAO = new LyricMovieDAO();
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "select * from l_lyrics order by lyric_views DESC ";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				lyric = new TrendingMovies();
				lyric.setLyric_name(resultSet.getString("lyric_title"));
				L_movie movie = movieDAO.findById(resultSet.getInt("movie_id"));
				lyric.setMovieId(movie.getMovieId());
				lyric.setMovieName(movie.getMovieName());
				lyric.setWriterName(resultSet.getString("writer_name"));
				lyric.setLyricViews(resultSet.getInt("lyric_views"));
				trendingLyrics.add(lyric);
			}
			cache.add(Constants.LATEST_TRENDING_KEY, 1200, trendingLyrics);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}

		return trendingLyrics;
	}

	public List<LyircsByMovie> getLyricsByWriterMovie(int movieId, String writerName) {
		LyircsByMovie lyric;
		List<LyircsByMovie> allLyrics = new ArrayList<LyircsByMovie>();
		List<L_lyrics> allLyricsList =  findAllLyrics();
		
		if( allLyricsList != null) {
			for(L_lyrics list : allLyricsList) {
				if(movieId == list.getMovie().getMovieId() && writerName.equalsIgnoreCase(list.getWriterName())) {
					lyric = new LyircsByMovie();
					lyric.setLyricId(list.getLyricId());
					lyric.setLyricTitle(list.getLyricTitle());
					lyric.setMovieId(movieId);
					lyric.setWriterName(writerName);
					allLyrics.add(lyric);
				}
			}
		}
		return allLyrics ;
		/*
		String queryString = "Select id,lyric_title from l_lyrics where movie_id = ? and writer_name = ?";
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setInt(1, movieId);
			ptmt.setString(2, writerName);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				lyric = new LyircsByMovie();
				lyric.setLyricId(resultSet.getInt("id"));
				lyric.setLyricTitle(resultSet.getString("lyric_title"));
				lyric.setMovieId(movieId);
				lyric.setWriterName(writerName);
				allLyrics.add(lyric);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}

		return allLyrics;
        */
	}

	public List<Integer> getMovieIdsByWriter(String writerName) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		int movieId;
		List<Integer> movies = new ArrayList<Integer>();
		List<L_lyrics> lyrics = findAllLyrics();
		
		if( lyrics != null ) {
			for(L_lyrics list : lyrics) {
				if( list.getWriterName().equalsIgnoreCase(writerName) ) {
					movies.add(list.getMovie().getMovieId());
				}
			}
		}
		return movies;
		/*PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "Select movie_id from l_lyrics where writer_name = ?";

		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, writerName);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				movieId = resultSet.getInt("movie_id");
				movies.add(movieId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return movies;*/

	}

}
