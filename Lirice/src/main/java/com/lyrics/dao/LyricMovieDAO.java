package com.lyrics.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.lyrics.Constants;
import com.lyrics.model.L_movie;
import com.lyrics.model.L_year;
import com.lyrics.model.MoviesByWriter;
import com.lyrics.model.MoviesByYear;
import com.lyrics.model.MoviesLatest;

public class LyricMovieDAO extends BaseDAO {

	public L_movie findById(int id) {
		L_movie movie = new L_movie() ;
		List<L_movie> movies = findAllMovies();
		
		if(movies != null) {
			for (L_movie list : movies) {
				if (list.getMovieId() == id) {
					/*movie = new L_movie();
					movie.setMovieName(list.getMovieName());
					movie.setLanguage(list.getLanguage());
					movie.setYear(list.getYear());
					movie.setMovieName(list.getMovieName());
					movie.setMovieReleaseDate(list.getMovieReleaseDate());
					movie.setCreationDate(list.getCreationDate());
					movie.setUpdationDate(list.getUpdationDate());*/
					//movieObj = new ArrayList<L_movie>();
					return list;
					
					
				}
			}
		}
		
		return  movie;
			/*movie = new L_movie();
			LyricLanguageDAO langDAO = new LyricLanguageDAO();
			LyricYearDAO yearDAO = new LyricYearDAO();
			PreparedStatement ptmt = null;
			ResultSet resultSet = null;
			String queryString = "SELECT * FROM l_movie WHERE (id = ? )";
			try {
				connection = getConnection();
				ptmt = connection.prepareStatement(queryString);
				ptmt.setInt(1, id);
				resultSet = ptmt.executeQuery();
				while (resultSet.next()) {
					movie.setMovieId(resultSet.getInt("id"));
					movie.setLanguage(langDAO.findById(resultSet.getInt("lang_id")));
					movie.setYear(yearDAO.findById(resultSet.getInt("lang_year")));
					movie.setMovieName(resultSet.getString("movie_name"));
					movie.setMovieReleaseDate(resultSet.getTimestamp("movie_release_date"));
					movie.setCreationDate(resultSet.getTimestamp("creation_time"));
					movie.setUpdationDate(resultSet.getTimestamp("updation_time"));
					movies.add(movie);
				}
				// cache.add(movieList,1200, movies);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				closeResultset(resultSet);
				closePtmt(ptmt);
				closeConnection();
			}
		

		return movie;  */
	}

	public List<L_movie> findLatest() {
	    List<L_movie> movies= null;
     
		List<L_movie> moviesLatest = findAllLatest();
		if (moviesLatest !=null  || moviesLatest.size() > 0) {
			int i = 0;
			movies = new ArrayList<L_movie>();
			for (L_movie list : moviesLatest) {
				if(i < Constants.LATEST_MOVIES_LIMIT) {
					movies.add(list);
				    i++;
				    continue;
				}
				break;
			}
		}
			
		return movies;
		// Note : No Need To write Query
		/*PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "SELECT * FROM lyrics.l_movie ORDER BY movie_release_date DESC LIMIT 15";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				latest = new L_movie();
				int movieId = resultSet.getInt("id");
				latest.setMovieId(movieId);
				latest.setMovieName(resultSet.getString("movie_name"));
				latest.setMovieReleaseDate(resultSet.getTimestamp("movie_release_date"));
				moviesLatest.add(latest);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}
        */
		
	}

	public List<L_movie> findByYear(L_year year) {
		List<L_movie> moviesByYear = new ArrayList<L_movie>();
		
		List<L_movie> movieYears=findAllMovies();
		
		if(movieYears != null) { 
			for(L_movie list : movieYears) {
				if(year.getLyircYear() == list.getYear().getLyircYear()) {
					moviesByYear.add(list);
				}
			}
		}
		 return moviesByYear;
		
	/*	PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "SELECT * FROM lyrics.l_movie where movie_year_id = ?";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setInt(1, year.getLyircYear());
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				movieYear = new L_movie();
				// int movieId = resultSet.getInt("id");
				movieYear.setMovieId(resultSet.getInt("id"));
				movieYear.setMovieName(resultSet.getString("movie_name"));
				movieYear.setYear(year);
				movieYears.add(movieYear);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}

		return movieYears;*/
	}

	private int getYear(Timestamp timestamp) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<MoviesByWriter> getMoviesByWriter(String writerName) {
		// TODO Auto-generated method stub
		MoviesByWriter movie;
		List<Integer> movieIds = null;
		List<MoviesByWriter> movies = new ArrayList<MoviesByWriter>();
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		movieIds = new LyricContentDAO().getMovieIdsByWriter(writerName);
		if (movieIds != null && movieIds.size() > 0) {
			for (int i = 0; i < movieIds.size(); i++) {

				String queryString = "SELECT movie_name,id FROM lyrics.l_movie where id = ? ";
				try {
					connection = getConnection();
					ptmt = connection.prepareStatement(queryString);
					ptmt.setInt(1, movieIds.get(i));
					resultSet = ptmt.executeQuery();
					while (resultSet.next()) {
						movie = new MoviesByWriter();
						movie.setMovieId(resultSet.getInt("id"));
						movie.setMovieName(resultSet.getString("movie_name"));
						movie.setWriterName(writerName);
						movies.add(movie);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					closeResultset(resultSet);
					closePtmt(ptmt);
					closeConnection();
				}
			}

		}

		return movies;
	}

	public List<L_movie> findAllLatest() {

		L_movie latest;
		List<L_movie> moviesLatest;
        
		if(Constants.USE_MEMCACHED) {
			cache = getMemcacheConnection();
			moviesLatest = (List<L_movie>) cache.get(Constants.LATEST_MOVIES_KEY);
		}
		
		
		if (moviesLatest != null)
			 return moviesLatest; 

		moviesLatest = new ArrayList<L_movie>();
		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		String queryString = "SELECT * FROM lyrics.l_movie ORDER BY movie_release_date DESC";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();
			while (resultSet.next()) {
				latest = new L_movie();
				int movieId = resultSet.getInt("id");
				latest.setMovieId(movieId);
				latest.setMovieName(resultSet.getString("movie_name"));
				latest.setMovieReleaseDate(resultSet.getTimestamp("movie_release_date"));
				moviesLatest.add(latest);
			}
			cache.add(Constants.LATEST_MOVIES_KEY, 1200, moviesLatest);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeResultset(resultSet);
			closePtmt(ptmt);
			closeConnection();
		}

		return moviesLatest;

	}
}
