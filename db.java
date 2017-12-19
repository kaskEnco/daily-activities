import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class db {
	
	static String path;
	static int count;



		public static Connection getConnection() throws Exception {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost/lyrics";
			String username = "root";
			String password = "root";

			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			return conn;
		}
	
	 
		public db() {
			JFrame f= new JFrame("Lyrics");  

		 JLabel l1=new JLabel("File Path:");  
		    l1.setBounds(10,50, 100,30);  
		    
		    JTextField t1=new JTextField();  
		    t1.setBounds(75,50, 200,30); 
		    
		    JLabel l2=new JLabel("Count:");  
		    l2.setBounds(10,100, 100,30);  
		    
		    JTextField t2=new JTextField();  
		    t2.setBounds(75,100, 200,30);  
		
		    JButton b=new JButton("Upload");  
		    b.setBounds(75,150,100,30);  
		    
		    JTextArea area=new JTextArea(); 
		    area.setBounds(25,200, 500,200);  
		    
		  
		    b.addActionListener(new ActionListener(){  
		    	public void actionPerformed(ActionEvent e){  
		    	         String path=t1.getText();  
		    	            int count=Integer.parseInt(t2.getText());
		    	            area.setText(path+" "+count);
		    	            db db=new db();
		    	            db.pathName(path, count);
		    	           
		    	            }
		    				});  
		   
		    f.add(l1);
		    f.add(t1); 
		    f.add(l2); 
		    f.add(t2); 
	        f.add(area);	   
		    f.add(b); 
		    f.setSize(800,500);  
		    f.setLayout(null);
		    f.setVisible(true);
		    }
		protected void pathName(String path,int count) {
			BufferedReader br = null;
    		FileReader fr = null;
    		String song = null;
    		String writer = null;
    		String lang = null;
    		String album = null;
    		String releaseDate = null;
    		int year = 0;// lang_id = 0, year_id = 0, movie_id = 0;
    		StringBuilder lyricContent = new StringBuilder();

    		try {
    			for (int i = 1; i <= count; i++) {
    				fr = new FileReader(path+ i + ".txt");
    				br = new BufferedReader(fr);

    				String sCurrentLine;

    				while ((sCurrentLine = br.readLine()) != null) {
    					System.out.println(sCurrentLine);

    					if (sCurrentLine.contains("Song")) {
    						song = sCurrentLine.split(":")[1];
    					} else if (sCurrentLine.contains("Album")) {
    						album = sCurrentLine.split(":")[1];
    					} else if (sCurrentLine.contains("Date")) {
    						releaseDate = sCurrentLine.split(":")[1];

    						year = Integer.valueOf(releaseDate.split("-")[2]);
    					} else if (sCurrentLine.contains("Language")) {
    						lang = sCurrentLine.split(":")[1];
    					} else if (sCurrentLine.contains("Writer")) {
    						writer = sCurrentLine.split(":")[1];
    					} else {
    						lyricContent.append(sCurrentLine + "#");
    					}

    				}
    				WriteToDB(song, album, lang, releaseDate, year, writer, lyricContent.toString());
    			}
    		} catch (IOException ex) {

    			ex.printStackTrace();

    		}

    		
		}
	private static void WriteToDB(String song, String album, String lang, String releaseDate, int year, String writer,
    			String lyricContent) {
    		// TODO Auto-generated method stub
    		int lang_id = getLanguageId(lang);
    		int year_id = getYearId(year);
    		populateMovie(lang_id, year_id, album, getTimeStamp(releaseDate));
    		populateLyricContent(writer, lyricContent, album, song, getTimeStamp(releaseDate));
    	}
		private static void populateLyricContent(String writer, String lyricContent, String album, String song,
    			Timestamp releaseDate) {
    		// TODO Auto-generated method stub
    		PreparedStatement pstmt = null;
    		Connection conn = null;
    		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    		try {
    			conn = getConnection();
    			pstmt = conn.prepareStatement(
    					"insert into lyrics.l_lyrics(id,lyric_title,lyric_content,movie_id,writer_name,creation_time,updation_time)"
    							+ " values(null,?,?,?,?,?,?)");
    			pstmt.setString(1, song);
    			pstmt.setString(2, lyricContent);
    			pstmt.setInt(3, getMovieId(album, releaseDate));
    			pstmt.setString(4, writer);
    			pstmt.setTimestamp(5, timestamp);
    			pstmt.setTimestamp(6, timestamp);
    			pstmt.executeUpdate();
    			System.out.println("Lyrics Table populated");
    		} catch (Exception e) {
    			// TODO Auto-generated catch block

    			e.printStackTrace();
    		}

    	}

		private static int getMovieId(String album, Timestamp releaseDate) {
    		// TODO Auto-generated method stub
    		PreparedStatement pstmt = null;
    		Connection conn = null;
    		ResultSet rs = null;
    		int movieId = 0;
    		try {
    			conn = getConnection();
    			pstmt = conn.prepareStatement("select id from lyrics.l_movie where movie_name =? and movie_release_date=?");
    			pstmt.setString(1, album);
    			pstmt.setTimestamp(2, releaseDate);
    			rs = pstmt.executeQuery();
    			while (rs.next()) {
    				movieId = (int) rs.getInt(1);
    			}
    			System.out.println("Movie Table populated");
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return movieId;

    	}

    	

		private static void populateMovie(int lang_id, int year_id, String album, Timestamp releaseDate) {
    		// TODO Auto-generated method stub
    		PreparedStatement pstmt = null;
    		Connection conn = null;
    		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    		try {
    			conn = getConnection();
    			pstmt = conn.prepareStatement(
    					"insert into lyrics.l_movie(id,lang_id,movie_year_id,creation_time,updation_time,movie_name,movie_release_date) "
    							+ "values(null,?,?,?,?,?,?)");
    			pstmt.setInt(1, lang_id);
    			pstmt.setInt(2, year_id);
    			pstmt.setTimestamp(3, timestamp);
    			pstmt.setTimestamp(4, timestamp);
    			pstmt.setString(5, album);
    			pstmt.setTimestamp(6, releaseDate);
    			pstmt.executeUpdate();
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}


		private static Timestamp getTimeStamp(String releaseDate) {
    		// TODO Auto-generated method stub
    		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    		Timestamp tm = null;

    		try {

    			Date date = formatter.parse(releaseDate);
    			tm = new Timestamp(date.getTime());

    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    		return tm;
    	}


		private static int getYearId(int year) {
    		// TODO Auto-generated method stub

    		PreparedStatement pstmt = null;
    		Connection conn = null;
    		ResultSet rs = null;
    		int yearId = 0;

    		try {
    			conn = getConnection();
    			pstmt = conn.prepareStatement("select id from lyrics.l_year where lyric_year =?");
    			pstmt.setInt(1, year);
    			rs = pstmt.executeQuery();
    			while (rs.next()) {
    				yearId = (int) rs.getInt("id");
    			}
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		return yearId;
    	}

		private static int getLanguageId(String lang) {
	    	

    		PreparedStatement pstmt = null;
    		Connection conn = null;
    		ResultSet rs = null;
    		int langId = 0;

    		try {
    			conn = getConnection();
    			pstmt = conn.prepareStatement("select id from lyrics.l_language where lang_name =?");
    			pstmt.setString(1, lang);
    			rs = pstmt.executeQuery();
    			while (rs.next()) {
    				langId = (int) rs.getInt("id");
    			}
    		} catch (Exception e) {
    			
    			e.printStackTrace();
    		}
    		return langId;
    	}


	public static void main(String[] args)
	 {
		
		 new db();
		 
		
 }
}


