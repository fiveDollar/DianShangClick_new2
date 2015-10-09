package com.windoor.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

//import org.traffic.rank4.Rank;

public class Databaselinux {
//	 public static String jdbcDriver = "com.mysql.jdbc.Driver";
//	 public static String jdbcurl = "jdbc:mysql://222.92.117.87/Tmall";
//	 public static String userName = "remote";
//	 public static String password = "Iknowthat";
	 
	 //http://222.92.117.87/
	public static String jdbcDriver = "com.mysql.jdbc.Driver";
	public static String jdbcurl = "jdbc:mysql://localhost/Tmall?user=root&password=&useUnicode=true&&characterEncoding=utf-8&autoReconnect = true";
	public static String userName = "root";
	public static String password = "Iknowthat";

	public static Connection createConnection() {
		Connection conn = null;
		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcurl, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void close(Statement st, Connection conn) {
		try {
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(ResultSet rs, Statement st, Connection conn) {
		try {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * return the article ID.
	 * 
	 * @TODO 
	 *       锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷诨锟矫伙拷锟斤拷锟斤拷锟斤拷锟侥拷锟斤拷锟紺rawlerParameter
	 *       .ARTICLE_CATEGORY_ID
	 * @param title
	 * @param fulltext
	 * @param introText
	 * @return
	 */

	private String getDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 锟斤拷锟斤拷锟斤拷锟节革拷式
		return df.format(new Date());
	}
	public ArrayList<Object[]> selectall(String sql) {
		ArrayList<Object[]> data = new ArrayList<>();
		Connection conn = createConnection();
		Statement statment = null;
		try {
			statment = conn.createStatement();
			ResultSet rst = statment.executeQuery(sql);
			ResultSetMetaData rstmd = rst.getMetaData();
			// 列数
			int count = rstmd.getColumnCount();
			// System.out.println(count);
			while (rst.next()) {
				Object[] row = new Object[count + 1];

				for (int i = 0; i < count; i++) {
					row[i] = rst.getObject(i + 1);
				}
				data.add(row);
			}
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		} finally {
			close(statment, conn);
		}
		return data;
	}

	public void inserAll(String SQLtemp) {
		// keywords.get(0);
		Connection conn = createConnection();
		Statement statment = null;
		try {
			statment = conn.createStatement();
			statment.executeUpdate(SQLtemp);
		} catch (SQLException e) {
			System.out.println(SQLtemp);
			e.printStackTrace();
		} finally {
			close(statment, conn);
		}
	}

	public void inserA(int id, int count, String keywords) {
		// keywords.get(0);
		String SQLtemp = "update A set `" + keywords + "`='" + count
				+ "' where `id`='" + id + "';";
		Connection conn = createConnection();
		try {
			Statement statment = conn.createStatement();
			statment.executeUpdate(SQLtemp);
			close(statment, conn);
		} catch (SQLException e) {
			System.out.println(SQLtemp);
			e.printStackTrace();
		}
	}

	public static void executeBatch(List<String> sqlList) throws SQLException {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = createConnection();
			st = con.createStatement();
			for (String s : sqlList) {
				st.addBatch(s);
			}
			st.executeBatch();
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs, st, con);
		}
	}
}
