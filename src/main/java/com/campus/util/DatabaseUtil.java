package com.campus.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database utility class for managing database connections using connection pooling.
 * Follows Singleton pattern to ensure a single connection pool instance.
 */
public class DatabaseUtil {
    
    private static final Logger logger = Logger.getLogger(DatabaseUtil.class);
    private static DatabaseUtil instance;
    private BasicDataSource dataSource;
    
    // Database configuration - Update these values as needed
private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_events?useSSL=false&serverTimezone=UTC";
private static final String DB_USERNAME = "root"; 
private static final String DB_PASSWORD = "password"; 
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Private constructor to enforce Singleton pattern
     */
    private DatabaseUtil() {
        initializeDataSource();
    }
    
    /**
     * Get the singleton instance of DatabaseUtil
     * @return DatabaseUtil instance
     */
    public static synchronized DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }
    
    /**
     * Initialize the connection pool
     */
    private void initializeDataSource() {
        try {
            Class.forName(DB_DRIVER);
            
            dataSource = new BasicDataSource();
            dataSource.setUrl(DB_URL);
            dataSource.setUsername(DB_USERNAME);
            dataSource.setPassword(DB_PASSWORD);
            dataSource.setDriverClassName(DB_DRIVER);
            
            // Connection pool configuration
            dataSource.setInitialSize(5);
            dataSource.setMaxTotal(20);
            dataSource.setMaxIdle(10);
            dataSource.setMinIdle(2);
            dataSource.setMaxWaitMillis(10000);
            dataSource.setValidationQuery("SELECT 1");
            dataSource.setTestOnBorrow(true);
            
            logger.info("Database connection pool initialized successfully");
        } catch (ClassNotFoundException e) {
            logger.error("MySQL JDBC Driver not found", e);
            throw new RuntimeException("Failed to initialize database connection pool", e);
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Failed to initialize database connection pool", e);
        }
    }
    
    /**
     * Get a connection from the pool
     * @return Connection object
     * @throws SQLException if connection cannot be obtained
     */
    public Connection getConnection() throws SQLException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to get database connection", e);
            throw e;
        }
    }
    
    /**
     * Close all resources (Connection, PreparedStatement, ResultSet)
     * @param conn Connection to close
     * @param pstmt PreparedStatement to close
     * @param rs ResultSet to close
     */
    public void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close(); // Returns connection to pool
            }
        } catch (SQLException e) {
            logger.error("Error closing database resources", e);
        }
    }
    
    /**
     * Close resources (Connection, PreparedStatement)
     * @param conn Connection to close
     * @param pstmt PreparedStatement to close
     */
    public void closeResources(Connection conn, PreparedStatement pstmt) {
        closeResources(conn, pstmt, null);
    }
    
    /**
     * Test the database connection
     * @return true if connection is successful
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            logger.error("Database connection test failed", e);
            return false;
        }
    }
}