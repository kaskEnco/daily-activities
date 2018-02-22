package Lyrics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoGUI {

	JLabel lablePath, lableCount;
	JTextField fieldPath, fieldCount;
	static TextArea area;
	JScrollPane scroll;
	JFrame frame;

	public static Connection getConnection() throws Exception, IOException {
		File file = new File("config.properties");
		FileInputStream fileInput = new FileInputStream(file);
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

	static MongoClient mongo = new MongoClient();
	MongoCredential credential = MongoCredential.createCredential("kask", "practice", "kask4all".toCharArray());// (username,db
																												// name,password)
	static MongoDatabase database = mongo.getDatabase("practice");
	static MongoCollection<Document> collection = database.getCollection("akask");

	public MongoGUI() {
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
		area.setBounds(25, 200, 400, 400);

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
		frame.setSize(800, 700);
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
		String url = null;
		StringBuilder lyricContent;

		try {

			for (int i = 1; i <= count; i++) {
				lyricContent = new StringBuilder();
				fr = new FileReader(path + i + ".txt");
				br = new BufferedReader(fr);

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					System.out.println(sCurrentLine);
					
					if (sCurrentLine.contains("songName")) {
						song = sCurrentLine.split(":")[1];
					} else if (sCurrentLine.contains("movieAlbum")) {
						album = sCurrentLine.split(":")[1];
					} else if (sCurrentLine.contains("dateTime")) {
						releaseDate = sCurrentLine.split(":")[1];
						year = Integer.valueOf(releaseDate.split("-")[2]);
					} else if (sCurrentLine.contains("languageName")) {
						lang = sCurrentLine.split(":")[1];
					} else if (sCurrentLine.contains("writerName")) {
						writer = sCurrentLine.split(":")[1];
					} else if(sCurrentLine.contains("url")) {
						url = sCurrentLine.split(":")[1];
					}else {
						lyricContent.append(sCurrentLine + "#");
					}
				}
				WriteToDB(song, album, writer, lyricContent.toString(), lang, url, year);
			}
		} catch (IOException ex) {

			ex.printStackTrace();

		}
	}

	private static void WriteToDB(String song, String album, String writer, String lyricContent, String lang,String url, int year)
			throws IOException {
		int movieId = getMovieId(album);
		int id = getLyricId(song, movieId, writer);
		populateLyricContent(id, lyricContent, url);
	}

	private static int getMovieId(String album) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int id = 0;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select id from lyrics.l_movie where movie_name=?");
			pstmt.setString(1, album);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
			
			 area.append(album + " " + "populated\n");
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	private static int getLyricId(String song, int movieId, String writer) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int id = 0;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select id from lyrics.l_lyrics where lyric_title=? and writer_name=? and movie_id=?");
			pstmt.setString(1, song);
			pstmt.setString(2, writer);
			pstmt.setInt(3, movieId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
			
			area.append( song + " " + "populated\n");
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	private static void populateLyricContent(int id,String lyricContent, String url)
			throws IOException {
		int count = (int) collection.count(Filters.eq("_id", id));
		if(count == 0) {
		Document document1 = new Document("lyricContent", lyricContent).append("_id", id).append("url", url);
		collection.insertOne(document1);
		}else {
			System.out.println("existed");
		}
		
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
		new MongoGUI();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MongoGUI gui = new MongoGUI();
					gui.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
}
