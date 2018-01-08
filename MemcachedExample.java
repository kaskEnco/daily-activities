package Memcached;

import java.sql.Statement;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.io.Serializable;
import net.spy.memcached.MemcachedClient;

class MemcacheImplements implements Serializable {
	String m_name = "a";
	String writer;
	Connection conn = null;
	Statement stmt = null;

	MemcacheImplements() {
		try {
			MemcachedClient cached = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
			if (cached.gets(m_name) != null) {
				System.out.println(cached.get(m_name));
				/*
				 * Connection con=DriverManager.getConnection(
				 * "jdbc:mysql://localhost/memcached","root","root");
				 * 
				 * Statement stmt=con.createStatement(); ResultSet
				 * rs=stmt.executeQuery("select * from memcached.mem where m_name='a'");
				 * while(rs.next()) { m_name=rs.getString(1); writer=rs.getString(2); //
				 * System.out.println(m_name); System.out.println("1:"+writer); }
				 */
			} else {
				Class.forName("com.mysql.jdbc.Driver");

				conn = DriverManager.getConnection("jdbc:mysql://localhost/memcached", "root", "root");

				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select * from memcached.mem where m_name='a'");
				while (rs.next()) {
					m_name = rs.getString(1);
					writer = rs.getString(2);
					System.out.println(m_name);
					System.out.println(writer);
					cached.set(m_name, 100, writer);

				}
				conn.close();
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}
}

public class MemcachedExample {
	public static void main(String[] args) {
		new MemcacheImplements();

	}
}
