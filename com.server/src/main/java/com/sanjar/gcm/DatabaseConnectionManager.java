package com.sanjar.gcm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseConnectionManager {
	
	
	 // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://db4free.net:3306/myowndatabase";

    //  Database credentials
    static final String USER = "customdatabase";
    static final String PASS = "customdatabase";

	private static DatabaseConnectionManager INSTANCE;
	private Logger LOGGER = Logger.getLogger(DatabaseConnectionManager.class.getName());
	private Connection conn;
	
	private DatabaseConnectionManager() {
		LOGGER.info("Inside Constructor of DatabaseConnectionManager");
		initializeDriver();
	}
	
	public static DatabaseConnectionManager getInstance(){
		if(null==INSTANCE){
			INSTANCE = new DatabaseConnectionManager();
		}
		return INSTANCE;
		
	}
	private void initializeDriver() {
		try {
	    	// Driver driver = new Driver();
			Class.forName(JDBC_DRIVER);
			LOGGER.info("Connecting to database...");
		    this.conn = DriverManager.getConnection(DB_URL,USER,PASS);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		return conn;
	}
	
}
