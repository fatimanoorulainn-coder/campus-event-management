package com.campus.dao;

import com.campus.model.Registration;
import com.campus.util.DatabaseUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Registration entity.
 * Handles all database operations related to event registrations.
 */
public class RegistrationDAO {
    
    private static final Logger logger = Logger.getLogger(RegistrationDAO.class);
    private final DatabaseUtil dbUtil;
    
    public RegistrationDAO() {
        this.dbUtil = DatabaseUtil.getInstance();
    }
    
    /**
     * Register a user for an event
     * @param userId The user ID
     * @param eventId The event ID
     * @return true if registration was successful
     */
    public boolean registerUserForEvent(int userId, int eventId) {
        // Check if already registered
        if (isUserRegistered(userId, eventId)) {
            logger.warn("User " + userId + " already registered for event " + eventId);
            return false;
        }
        
        // Check if event has capacity
        EventDAO eventDAO = new EventDAO();
        var event = eventDAO.findById(eventId);
        if (event == null || event.isFull()) {
            logger.warn("Event " + eventId + " is full or does not exist");
            return false;
        }
        
        String sql = "INSERT INTO registrations (user_id, event_id, status) VALUES (?, ?, 'registered')";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, eventId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("User " + userId + " registered for event " + eventId);
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            logger.error("Error registering user " + userId + " for event " + eventId, e);
            return false;
        } finally {
            dbUtil.closeResources(conn, pstmt);
        }
    }
    
    /**
     * Cancel a user's registration for an event
     * @param userId The user ID
     * @param eventId The event ID
     * @return true if cancellation was successful
     */
    public boolean cancelRegistration(int userId, int eventId) {
        String sql = "UPDATE registrations SET status = 'cancelled' WHERE user_id = ? AND event_id = ? AND status = 'registered'";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, eventId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("User " + userId + " cancelled registration for event " + eventId);
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            logger.error("Error cancelling registration for user " + userId + " and event " + eventId, e);
            return false;
        } finally {
            dbUtil.closeResources(conn, pstmt);
        }
    }
    
    /**
     * Check if a user is registered for an event
     * @param userId The user ID
     * @param eventId The event ID
     * @return true if user is registered
     */
    public boolean isUserRegistered(int userId, int eventId) {
        String sql = "SELECT COUNT(*) FROM registrations WHERE user_id = ? AND event_id = ? AND status = 'registered'";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, eventId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking registration status", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return false;
    }
    
    /**
     * Get all registrations for a user
     * @param userId The user ID
     * @return List of registrations with event details
     */
    public List<Registration> getRegistrationsByUser(int userId) {
        String sql = "SELECT r.*, e.title as event_title, e.event_date, e.location as event_location, " +
                    "u.full_name as user_name " +
                    "FROM registrations r " +
                    "JOIN events e ON r.event_id = e.event_id " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.user_id = ? " +
                    "ORDER BY r.registration_date DESC";
        
        return executeQueryForList(sql, new Object[]{userId});
    }
    
    /**
     * Get all active registrations for a user
     * @param userId The user ID
     * @return List of active registrations
     */
    public List<Registration> getActiveRegistrationsByUser(int userId) {
        String sql = "SELECT r.*, e.title as event_title, e.event_date, e.location as event_location, " +
                    "u.full_name as user_name " +
                    "FROM registrations r " +
                    "JOIN events e ON r.event_id = e.event_id " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.user_id = ? AND r.status = 'registered' " +
                    "ORDER BY r.registration_date DESC";
        
        return executeQueryForList(sql, new Object[]{userId});
    }
    
    /**
     * Get all registrations for an event
     * @param eventId The event ID
     * @return List of registrations with user details
     */
    public List<Registration> getRegistrationsByEvent(int eventId) {
        String sql = "SELECT r.*, e.title as event_title, e.event_date, e.location as event_location, " +
                    "u.full_name as user_name " +
                    "FROM registrations r " +
                    "JOIN events e ON r.event_id = e.event_id " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.event_id = ? AND r.status = 'registered' " +
                    "ORDER BY r.registration_date DESC";
        
        return executeQueryForList(sql, new Object[]{eventId});
    }
    
    /**
     * Get registration count for an event
     * @param eventId The event ID
     * @return Number of registrations
     */
    public int getRegistrationCountForEvent(int eventId) {
        String sql = "SELECT COUNT(*) FROM registrations WHERE event_id = ? AND status = 'registered'";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, eventId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error getting registration count for event " + eventId, e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return 0;
    }
    
    /**
     * Get all registrations
     * @return List of all registrations
     */
    public List<Registration> getAllRegistrations() {
        String sql = "SELECT r.*, e.title as event_title, e.event_date, e.location as event_location, " +
                    "u.full_name as user_name " +
                    "FROM registrations r " +
                    "JOIN events e ON r.event_id = e.event_id " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "ORDER BY r.registration_date DESC";
        
        return executeQueryForList(sql, new Object[0]);
    }
    
    /**
     * Check if a user has any active registrations
     * @param userId The user ID
     * @return true if user has active registrations
     */
    public boolean hasActiveRegistrations(int userId) {
        String sql = "SELECT COUNT(*) FROM registrations WHERE user_id = ? AND status = 'registered'";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking active registrations for user " + userId, e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to Registration object
     * @param rs ResultSet from database query
     * @return Registration object
     * @throws SQLException if mapping fails
     */
    private Registration mapResultSetToRegistration(ResultSet rs) throws SQLException {
        Registration registration = new Registration();
        registration.setRegistrationId(rs.getInt("registration_id"));
        registration.setUserId(rs.getInt("user_id"));
        registration.setEventId(rs.getInt("event_id"));
        registration.setRegistrationDate(rs.getTimestamp("registration_date"));
        registration.setStatus(rs.getString("status"));
        registration.setAttended(rs.getBoolean("attended"));
        
        // Set additional fields if available
        try {
            registration.setEventTitle(rs.getString("event_title"));
            registration.setUserName(rs.getString("user_name"));
            registration.setEventLocation(rs.getString("event_location"));
            
            Date eventDate = rs.getDate("event_date");
            if (eventDate != null) {
                registration.setEventDate(eventDate.toLocalDate());
            }
        } catch (SQLException e) {
            // These columns might not be in all queries
        }
        
        return registration;
    }
    
    /**
     * Execute a query and return a list of registrations
     * @param sql SQL query
     * @param params Parameters for the prepared statement
     * @return List of registrations
     */
    private List<Registration> executeQueryForList(String sql, Object[] params) {
        List<Registration> registrations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                registrations.add(mapResultSetToRegistration(rs));
            }
        } catch (SQLException e) {
            logger.error("Error executing query for registration list", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return registrations;
    }
}