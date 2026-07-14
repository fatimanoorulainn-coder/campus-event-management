package com.campus.dao;

import com.campus.model.Event;
import com.campus.util.DatabaseUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Event entity.
 * Handles all database operations related to events.
 */
public class EventDAO {
    
    private static final Logger logger = Logger.getLogger(EventDAO.class);
    private final DatabaseUtil dbUtil;
    
    public EventDAO() {
        this.dbUtil = DatabaseUtil.getInstance();
    }
    
    /**
     * Create a new event
     * @param event Event object to create
     * @return true if event was created successfully
     */
    public boolean createEvent(Event event) {
        String sql = "INSERT INTO events (title, description, event_date, event_time, location, " +
                    "capacity, category, image_url, organizer, contact_email, contact_phone, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setDate(3, Date.valueOf(event.getEventDate()));
            pstmt.setTime(4, Time.valueOf(event.getEventTime()));
            pstmt.setString(5, event.getLocation());
            pstmt.setInt(6, event.getCapacity());
            pstmt.setString(7, event.getCategory());
            pstmt.setString(8, event.getImageUrl());
            pstmt.setString(9, event.getOrganizer());
            pstmt.setString(10, event.getContactEmail());
            pstmt.setString(11, event.getContactPhone());
            pstmt.setInt(12, event.getCreatedBy());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        event.setEventId(rs.getInt(1));
                    }
                }
                logger.info("Event created successfully: " + event.getTitle());
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            logger.error("Error creating event: " + event.getTitle(), e);
            return false;
        } finally {
            dbUtil.closeResources(conn, pstmt);
        }
    }
    
    /**
     * Find event by ID
     * @param eventId The event ID
     * @return Event object if found, null otherwise
     */
    public Event findById(int eventId) {
        String sql = "SELECT e.*, " +
                    "(SELECT COUNT(*) FROM registrations r WHERE r.event_id = e.event_id AND r.status = 'registered') as registered_count " +
                    "FROM events e WHERE e.event_id = ? AND e.is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, eventId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEvent(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding event by ID: " + eventId, e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    /**
     * Get all active events
     * @return List of all active events
     */
    public List<Event> getAllEvents() {
        String sql = "SELECT e.*, " +
                    "(SELECT COUNT(*) FROM registrations r WHERE r.event_id = e.event_id AND r.status = 'registered') as registered_count " +
                    "FROM events e WHERE e.is_active = TRUE " +
                    "ORDER BY e.event_date DESC, e.event_time DESC";
        
        return executeQueryForList(sql, new Object[0]);
    }
    
    /**
     * Get all active events with a specific user's registration status
     * @param userId The user ID to check registration status
     * @return List of events with registration status
     */
    public List<Event> getAllEventsWithUserStatus(int userId) {
        String sql = "SELECT e.*, " +
                    "(SELECT COUNT(*) FROM registrations r WHERE r.event_id = e.event_id AND r.status = 'registered') as registered_count, " +
                    "EXISTS (SELECT 1 FROM registrations r WHERE r.event_id = e.event_id AND r.user_id = ? AND r.status = 'registered') as user_registered " +
                    "FROM events e WHERE e.is_active = TRUE " +
                    "ORDER BY e.event_date DESC, e.event_time DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Event> events = new ArrayList<>();
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                event.setUserRegistered(rs.getBoolean("user_registered"));
                events.add(event);
            }
        } catch (SQLException e) {
            logger.error("Error getting events with user status", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return events;
    }
    
    /**
     * Get upcoming events
     * @return List of upcoming events (today and future)
     */
    public List<Event> getUpcomingEvents() {
        String sql = "SELECT e.*, " +
                    "(SELECT COUNT(*) FROM registrations r WHERE r.event_id = e.event_id AND r.status = 'registered') as registered_count " +
                    "FROM events e WHERE e.is_active = TRUE AND e.event_date >= CURDATE() " +
                    "ORDER BY e.event_date ASC, e.event_time ASC LIMIT 10";
        
        return executeQueryForList(sql, new Object[0]);
    }
    
    /**
     * Get events by category
     * @param category The category to filter by
     * @return List of events in the category
     */
    public List<Event> getEventsByCategory(String category) {
        String sql = "SELECT e.*, " +
                    "(SELECT COUNT(*) FROM registrations r WHERE r.event_id = e.event_id AND r.status = 'registered') as registered_count " +
                    "FROM events e WHERE e.is_active = TRUE AND e.category = ? " +
                    "ORDER BY e.event_date DESC, e.event_time DESC";
        
        return executeQueryForList(sql, new Object[]{category});
    }
    
    /**
     * Search events by title, description, or location
     * @param searchTerm The search term
     * @return List of matching events
     */
    public List<Event> searchEvents(String searchTerm) {
        String sql = "SELECT e.*, " +
                    "(SELECT COUNT(*) FROM registrations r WHERE r.event_id = e.event_id AND r.status = 'registered') as registered_count " +
                    "FROM events e WHERE e.is_active = TRUE AND " +
                    "(e.title LIKE ? OR e.description LIKE ? OR e.location LIKE ?) " +
                    "ORDER BY e.event_date DESC, e.event_time DESC";
        
        String pattern = "%" + searchTerm + "%";
        return executeQueryForList(sql, new Object[]{pattern, pattern, pattern});
    }
    
    /**
     * Get events created by a specific user
     * @param userId The user ID
     * @return List of events created by the user
     */
    public List<Event> getEventsByCreator(int userId) {
        String sql = "SELECT e.*, " +
                    "(SELECT COUNT(*) FROM registrations r WHERE r.event_id = e.event_id AND r.status = 'registered') as registered_count " +
                    "FROM events e WHERE e.created_by = ? AND e.is_active = TRUE " +
                    "ORDER BY e.event_date DESC, e.event_time DESC";
        
        return executeQueryForList(sql, new Object[]{userId});
    }
    
    /**
     * Update an event
     * @param event Event object with updated information
     * @return true if update was successful
     */
    public boolean updateEvent(Event event) {
        String sql = "UPDATE events SET title = ?, description = ?, event_date = ?, event_time = ?, " +
                    "location = ?, capacity = ?, category = ?, image_url = ?, organizer = ?, " +
                    "contact_email = ?, contact_phone = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE event_id = ? AND is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setDate(3, Date.valueOf(event.getEventDate()));
            pstmt.setTime(4, Time.valueOf(event.getEventTime()));
            pstmt.setString(5, event.getLocation());
            pstmt.setInt(6, event.getCapacity());
            pstmt.setString(7, event.getCategory());
            pstmt.setString(8, event.getImageUrl());
            pstmt.setString(9, event.getOrganizer());
            pstmt.setString(10, event.getContactEmail());
            pstmt.setString(11, event.getContactPhone());
            pstmt.setInt(12, event.getEventId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Event updated successfully: " + event.getTitle());
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            logger.error("Error updating event: " + event.getTitle(), e);
            return false;
        } finally {
            dbUtil.closeResources(conn, pstmt);
        }
    }
    
    /**
     * Delete an event (soft delete - sets is_active to FALSE)
     * @param eventId The event ID
     * @return true if deletion was successful
     */
    public boolean deleteEvent(int eventId) {
        String sql = "UPDATE events SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP WHERE event_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, eventId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Event deleted successfully: " + eventId);
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            logger.error("Error deleting event: " + eventId, e);
            return false;
        } finally {
            dbUtil.closeResources(conn, pstmt);
        }
    }
    
    /**
     * Get total event count
     * @return Total number of active events
     */
    public int getEventCount() {
        String sql = "SELECT COUNT(*) FROM events WHERE is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error getting event count", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return 0;
    }
    
    /**
     * Get total registered count across all events
     * @return Total number of registrations
     */
    public int getTotalRegistrations() {
        String sql = "SELECT COUNT(*) FROM registrations WHERE status = 'registered'";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error getting total registrations", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return 0;
    }
    
    /**
     * Get all unique categories
     * @return List of categories
     */
    public List<String> getAllCategories() {
        String sql = "SELECT DISTINCT category FROM events WHERE is_active = TRUE ORDER BY category";
        List<String> categories = new ArrayList<>();
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            logger.error("Error getting categories", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return categories;
    }
    
    /**
     * Map ResultSet to Event object
     * @param rs ResultSet from database query
     * @return Event object
     * @throws SQLException if mapping fails
     */
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("event_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setEventDate(rs.getDate("event_date").toLocalDate());
        event.setEventTime(rs.getTime("event_time").toLocalTime());
        event.setLocation(rs.getString("location"));
        event.setCapacity(rs.getInt("capacity"));
        event.setCategory(rs.getString("category"));
        event.setImageUrl(rs.getString("image_url"));
        event.setOrganizer(rs.getString("organizer"));
        event.setContactEmail(rs.getString("contact_email"));
        event.setContactPhone(rs.getString("contact_phone"));
        event.setActive(rs.getBoolean("is_active"));
        event.setCreatedBy(rs.getInt("created_by"));
        event.setCreatedAt(rs.getTimestamp("created_at"));
        event.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set registered count if available
        try {
            event.setRegisteredCount(rs.getInt("registered_count"));
        } catch (SQLException e) {
            // Column might not exist in all queries
            event.setRegisteredCount(0);
        }
        
        return event;
    }
    
    /**
     * Execute a query and return a list of events
     * @param sql SQL query
     * @param params Parameters for the prepared statement
     * @return List of events
     */
    private List<Event> executeQueryForList(String sql, Object[] params) {
        List<Event> events = new ArrayList<>();
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
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            logger.error("Error executing query for event list", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return events;
    }
}