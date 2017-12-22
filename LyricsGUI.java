package Lyrics;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LyricsGUI {
	JLabel lablePath, lableCount;
	JTextField fieldPath, fieldCount;
	static TextArea area;
	JScrollPane scroll;
	JFrame frame;

	public static Connection getConnection() throws Exception, IOException {
		File file = new File("config.properties");
		FileInputStream fileInput = new FileInputStream(file);
		// InputStream in = new FileInputStream(System.getProperty("lyric.properties"));
		Properties property = new Properties();
		property.load(fileInput);

		String username = property.getProperty("username");
		String password = property.getProperty("password");
		String url = property.getProperty("url");
		String driver = property.getProperty("driver");

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

	public LyricsGUI() {
		frame = new JFrame("Lyrics");

		lablePath = new JLabel("File Path:");
		lablePath.setBounds(10, 50, 100, 30);

		fieldPath = new JTextField();
		fieldPath.setBounds(75, 50, 200, 30);

		lableCount = new JLabel("Count:");
		lableCount.setBounds(10, 100, 100, 30);

		fieldCount = new JTextField();
		fieldCount.setBounds(75, 100, 200, 30);

		JButton button = new JButton("Upload");
		button.setBounds(75, 150, 100, 30);

		area = new TextArea();
		area.setBounds(25, 250, 400, 400);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = fieldPath.getText();
				int count = Integer.parseInt(fieldCount.getText());
				pathName(path, count);
			}
		});

		frame.add(lablePath);
		frame.add(fieldPath);
		frame.add(lableCount);
		frame.add(fieldCount);
		frame.add(area);
		frame.add(button);
		frame.setSize(800, 500);
		frame.setLayout(null);
		browse();
	}

	protected void pathName(String path, int count) {
		BufferedReader br = null;
		FileReader fr = null;
		String song = null;
		String writer = null;
		String lang = null;
		String album = null;
		String releaseDate = null;
		int year = 0;
		StringBuilder lyricContent;

		try {

			for (int i = 1; i <= count; i++) {
				lyricContent = new StringBuilder();
				fr = new FileReader(path + i + ".txt");
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
		populateLyricContent(writer, lyricContent, album, song, getTimeStamp(releaseDate), area);
	}

	private static void populateLyricContent(String writer, String lyricContent, String album, String song,
			Timestamp releaseDate, TextArea area) {
		// TODO Auto-generated method stub
		boolean exist = lyricExist(writer, album, song, releaseDate);
		if (!exist) {
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
				StringBuilder builder = new StringBuilder();
				area.setText(album + "  " + song + " " + "populated");
				String output = area.getText();
				area.append(output);
				// builder.append(output);
				// area.setText(output);
				// conn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
		} else {
			System.out.println("song exist");
			area.setText(album + "  " + song + " " + "existed");
		}
	}

	private static boolean lyricExist(String writer, String album, String song, Timestamp releaseDate) {
		// TODO Auto-generated method stub
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int id = 0;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(
					"select id from lyrics.l_lyrics where writer_name=? and movie_id=? and lyric_title=?");
			pstmt.setString(1, writer);
			pstmt.setInt(2, getMovieId(album, releaseDate));
			pstmt.setString(3, song);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				id = (int) rs.getInt(1);
			}
			if (id > 0) {
				return true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
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
			// conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieId;

	}

	private static void populateMovie(int lang_id, int year_id, String album, Timestamp releaseDate) {
		// TODO Auto-generated method stub
		boolean exist = movieExist(album, releaseDate);
		if (!exist) {
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
				// conn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("existed");
		}
	}

	private static boolean movieExist(String album, Timestamp releaseDate) {
		// TODO Auto-generated method stub
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int id = 0;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select id from l_movie where movie_name=? and movie_release_date=?");
			pstmt.setString(1, album);
			pstmt.setTimestamp(2, releaseDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				id = (int) rs.getInt("id");
			}
			if (id > 0) {
				return true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	private static Timestamp getTimeStamp(String releaseDate) {
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
				// conn.close();
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
				// conn.close();
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return langId;
	}

	private void browse() {

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(fieldPath);
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(300, 50, 50, 20);
		frame.getContentPane().add(btnBrowse);

		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int openDialogBox = fileChooser.showOpenDialog(null);
				if (openDialogBox == JFileChooser.APPROVE_OPTION) {
					fieldPath.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});
	}

	public static void main(String[] args) {
		new LyricsGUI();
		// GUI.iteratePath(path, count);
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					LyricsGUI gui = new LyricsGUI();
					gui.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
}
