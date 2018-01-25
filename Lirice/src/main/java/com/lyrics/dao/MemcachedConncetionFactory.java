package com.lyrics.dao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;

import net.spy.memcached.MemcachedClient;

public class MemcachedConncetionFactory {

	static MemcachedClient cache;
	private static MemcachedConncetionFactory memcacheConnection = null;

	static {
		try {
			cache = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static MemcachedConncetionFactory getInstance() {
		if (memcacheConnection == null) {
			synchronized (MemcachedConncetionFactory.class) {
				if (memcacheConnection == null) {
					memcacheConnection = new MemcachedConncetionFactory();
				}
			}
		}
		return memcacheConnection;
	}

	public MemcachedClient getConnection() throws SQLException {
		return cache;
	}

}
