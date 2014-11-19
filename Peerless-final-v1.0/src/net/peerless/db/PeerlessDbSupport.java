/**
 * Copyright (c) 2009 Hee Won Lee <knowpd@gmail.com>
 *
 * This file is part of Peerless's P2P Application.
 *
 * Peerless's P2P Application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Peerless's P2P Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.peerless.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.peerless.hashtable.FileSignature;
import net.peerless.message.ResultSignatureMessage;

public class PeerlessDbSupport {

	private final String framework = "embedded";
	private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private final String protocol = "jdbc:derby:";
	private String dbName;
	private Connection conn;

	public PeerlessDbSupport(String dbName) {
		super();
		this.dbName = dbName;
	}

	public Connection connect() throws SQLException {
		loadDriver();
		conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		conn.setAutoCommit(false);
		return conn;
	}

	public void dropTable(String tableName) throws SQLException {
		Statement s = conn.createStatement();
		if (s.execute("drop table " + tableName)) {
			System.out.println("table is droped");
		} else {
			System.out.println("FAIL: table is droped");
		}
		conn.commit();
	}

	public static void main(String[] args) throws SQLException {
		System.out.println("createSchema() Test by hwlee");
		PeerlessDbSupport peerlessDbSupport = new PeerlessDbSupport(
				"peerlessdb");
		peerlessDbSupport.connect();
		peerlessDbSupport.createSchema();
	}

	public void createSchema() {
		String createActivePeers = "CREATE TABLE ACTIVEPEERS("
				+ "PvtIP varchar(30) NOT NULL,"
				+ "ipAddress	varchar(30) NOT NULL,"
				+ "userId varchar(20) NOT NULL,"
				+ "userType varchar(10) NOT NULL)";
		String alterActivePeers = "ALTER TABLE ACTIVEPEERS "
				+ "ADD CONSTRAINT ACTIVEPEERS_PK Primary Key (ipAddress)";

		String createFileLocation = "CREATE TABLE FILELOCATION("
				+ "fileName 			varchar(50),"
				+ "fileSignature		varchar(1024) NOT NULL,"
				+ "ipAddress			varchar(30) NOT NULL,"
				+ "fileAbstract		varchar(128) NOT NULL,"
				+ "PvtIPAdd			varchar(30) NOT NULL)";
				

		Statement s1;
		Statement s2;
		Statement s;

		
		try {
			s1 = conn.createStatement();
			s1.execute("drop table ACTIVEPEERS");
			System.out.println("Table ACTIVEPEERS dropped!!! by hwlee");
		} catch (SQLException e1) {
			System.out.println("Dropping table ACTIVEPEERS failed. It may exist!!! by hwlee");
			//e1.printStackTrace();
		}
		
		try {
			s2 = conn.createStatement();
			s2.execute("drop table FILELOCATION");
			System.out.println("Table FILELOCATION dropped!!! by hwlee");
		} catch (SQLException e1) {
			System.out.println("Dropping table FILELOCATION failed. It may exist!!! by hwlee");
			//e1.printStackTrace();
		}
		
		try {
			
			s = conn.createStatement();
			s.execute(createActivePeers);
			s.execute(alterActivePeers);
			System.out.println("Table ACTIVEPEERS was created!!! by hwlee");
			s.execute(createFileLocation);
			System.out.println("Table FILELOCATION was created!!! by hwlee");
			conn.commit();
		} catch (SQLException e) {
			System.out.println("Table ACTIVEPEERS may exist!!! by hwlee");
			// e.printStackTrace();
		}
	}

	public void setUpSchema(String schemaFile) throws FileNotFoundException {
		File file = new File(schemaFile);
		FileReader fileReader = new FileReader(file);
		ScriptRunner scriptRunner = new ScriptRunner(conn, false, true);
		try {
			scriptRunner.runScript(fileReader);
			conn.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (SQLException e) {
		}
	}

	public int insertFiles(String clientip, String filesignature,
			String fileabstract, String PvtIP, String Filename)
			throws SQLException {

		System.out.println("Insert Files");
		System.out.println(filesignature);
		PreparedStatement ps = null;

		ps = conn
				.prepareStatement("insert into FILELOCATION (fileSignature, ipAddress, fileAbstract, PvtIPAdd, fileName) values (?,?,?,?,?)");

		ps.setString(1, filesignature);
		ps.setString(2, clientip);
		ps.setString(3, fileabstract);
		ps.setString(4, PvtIP);
		ps.setString(5, Filename);

		int j = ps.executeUpdate();
		conn.commit();
		return j;
	}

	public void deleteFiles(String clientip) throws SQLException {
		PreparedStatement ps = null;
		System.out.println("IP Whose FILES will be Deleted: " + clientip);
		ps = conn
				.prepareStatement("delete from FILELOCATION where ipAddress = '"
						+ clientip + "'");
		int delete = ps.executeUpdate();
		if (delete == 1)
			System.out.println("FILES for " + clientip + " deleted");
		conn.commit();
	}

	public int insertUser(String userName, String userId, String passWord,
			int reminderQuestion, String reminderAnswer) throws SQLException {

		PreparedStatement ps = null;

		ps = conn
				.prepareStatement("insert into users (userName, userId, passWord, reminderQuestion, reminderAnswer) values (?,?,?,?,?)");

		ps.setString(1, userName);
		ps.setString(2, userId);
		ps.setString(3, passWord);
		ps.setInt(4, reminderQuestion);
		ps.setString(5, reminderAnswer);
		int numrows = ps.executeUpdate();
		conn.commit();
		return numrows;
	}

	public int insertActivepeers(String UserID, String IP, String Type,
			String PvtIP) throws SQLException {

		PreparedStatement ps = null;

		ps = conn
				.prepareStatement("insert into ACTIVEPEERS (PvtIP,ipAddress, userId, userType) values (?,?,?,?)");

		ps.setString(1, PvtIP);
		ps.setString(2, IP);
		ps.setString(3, UserID);
		ps.setString(4, Type);
		int j = ps.executeUpdate();
		conn.commit();
		return j;
	}

	public void deleteUsers(String UserID) throws SQLException {

		PreparedStatement ps = null;
		System.out.println("ID Received for Deletion: " + UserID);
		ps = conn.prepareStatement("delete from ACTIVEPEERS where userID = '"
				+ UserID + "'");
		int delete = ps.executeUpdate();
		if (delete == 1)
			System.out.println("Entry for " + UserID + " deleted");
		conn.commit();
	}

	public void UpdateSuper(String UserID) throws SQLException {
		PreparedStatement ps = null;
		ps = conn
				.prepareStatement("UPDATE ACTIVEPEERS SET userType = 'SUPER' WHERE userID = '"
						+ UserID + "'");
		int super1 = ps.executeUpdate();
		if (super1 == 1)
			System.out.println("The New Super User is " + UserID);
		conn.commit();
	}

	public ResultSet getSuperPeer() throws SQLException {
		PreparedStatement ps = null;
		ps = conn
				.prepareStatement("select ipAddress, PvtIP from ACTIVEPEERS where userType = 'SUPER'");
		ResultSet res = ps.executeQuery();
		conn.commit();
		return res;
	}

	public ResultSet normaluser() throws SQLException {
		PreparedStatement ps = null;
		ps = conn
				.prepareStatement("select userID from ACTIVEPEERS where userType = 'NORMAL'");
		ResultSet res = ps.executeQuery();
		conn.commit();
		return res;
	}

	public List readData(FileSignature signature) throws SQLException {

		Statement res = conn.createStatement();
		String ipAddr = null;
		List<String> ipAddressList = Collections
				.synchronizedList(new ArrayList());
		List<ResultSignatureMessage> searchResult = Collections
				.synchronizedList(new ArrayList());

		int i = 0;
		byte[] byteArray = new byte[1024];
		int count = 0;

		byteArray = signature.getFileSignature();
		String fileSignatureString = new String(byteArray);
		// String fileSignatureString = "This is test";
		// System.out.println("[PeerlessDbSupport] fileSignatureString " +
		// fileSignatureString);

		// testing data begins here
		PreparedStatement ps = null;
		FileSignature fileSignature;
		/*
		 * try { fileSignature = new FileSignature("test data"); String
		 * testDataString = new String(fileSignature.getFileSignature());
		 * 
		 * 
		 * ps =conn.prepareStatement(
		 * "insert into FILELOCATION (fileName,fileSignature, ipAddress, fileAbstract) values (?,?,?,?)"
		 * );
		 * 
		 * ps.setString (1, "file1"); ps.setString (2, testDataString);
		 * ps.setString (3, "192.16.0.8"); ps.setString (4,
		 * "This is test Abstract From DB"); int j = ps.executeUpdate();
		 * conn.commit();
		 * 
		 * ps =conn.prepareStatement(
		 * "insert into FILELOCATION (fileName,fileSignature, ipAddress, fileAbstract) values (?,?,?,?)"
		 * );
		 * 
		 * ps.setString (1, "file2"); ps.setString (2, testDataString);
		 * ps.setString (3, "192.255.0.96"); ps.setString (4,
		 * "This is test Abstract From DB part 2"); int n = ps.executeUpdate();
		 * conn.commit();
		 * 
		 * 
		 * 
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		// testing data ends
		/*
		 * if (res.execute("SELECT * FROM FILELOCATION WHERE fileSignature = '"
		 * +fileSignatureString+ "'")) { ResultSet ipSet = res.getResultSet();
		 * //System.out.println("res not null"); } else
		 * {System.out.println("res null error");}
		 */

		ResultSet ipSet = res
				.executeQuery("SELECT * FROM FILELOCATION WHERE fileSignature = '"
						+ fileSignatureString + "'");

		searchResult.clear();
		while (ipSet.next()) {
			ResultSignatureMessage resultObject = new ResultSignatureMessage();
			resultObject.setIpAddress(ipSet.getString("ipAddress"));
			resultObject.setPvtIpAddress(ipSet.getString("PvtIPAdd"));
			resultObject.setFileAbstract(ipSet.getString("fileAbstract"));
			resultObject.setFileSignature(signature);
			resultObject.setFileName(ipSet.getString("fileName"));
			searchResult.add(resultObject);
			count++;
			// fireTableDataChanged();
			// ipAddressList.add(ipSet.getString("ipAddress"));
			// System.out.println("ipAddress    " +
			// ipSet.getString("ipAddress"));
			// System.out.println(searchResult);
			// System.out.println(ipAddressList);

		}
		/*
		 * ResultSignatureMessage resultObject = new ResultSignatureMessage();
		 * resultObject.setIpAddress ("192.8.75.90");
		 * resultObject.setFileAbstract("This is the Abstract of Test File");
		 * resultObject.setFileSignature(signature);
		 * searchResult.add(resultObject);
		 */
		System.out.println("[PeerlessDbSupport] count" + count);
		return searchResult;
		// return ipAddressList;

	}

	private void loadDriver() {
		/*
		 * The JDBC driver is loaded by loading its class. If you are using JDBC
		 * 4.0 (Java SE 6) or newer, JDBC drivers may be automatically loaded,
		 * making this code optional.
		 * 
		 * In an embedded environment, this will also start up the Derby engine
		 * (though not any databases), since it is not already running. In a
		 * client environment, the Derby engine is being run by the network
		 * server framework.
		 * 
		 * In an embedded environment, any static Derby system properties must
		 * be set before loading the driver to take effect.
		 */
		try {
			Class.forName(driver).newInstance();
			System.out.println("Loaded the appropriate driver");
		} catch (ClassNotFoundException cnfe) {
			System.err.println("\nUnable to load the JDBC driver " + driver);
			System.err.println("Please check your CLASSPATH.");
			cnfe.printStackTrace(System.err);
		} catch (InstantiationException ie) {
			System.err.println("\nUnable to instantiate the JDBC driver "
					+ driver);
			ie.printStackTrace(System.err);
		} catch (IllegalAccessException iae) {
			System.err.println("\nNot allowed to access the JDBC driver "
					+ driver);
			iae.printStackTrace(System.err);
		}
	}

}
