package com.campus.dao;

import com.campus.model.User;
import com.campus.util.DatabaseUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entity.
 * Handles all database operations related to users.
 */
public class UserDAO {
    
    private static final Logger logger = Logger.getLogger(UserDAO.class);
    private final DatabaseUtil dbUtil;
    
    public UserDAO() {
        this.dbUtil = DatabaseUtil.getInstance();
    }
    
    /**
     * Create a new user in the database
     * @param user User object to create
     * @return true if user was created successfully
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, full_name, role, phone, department, student_id, profile_picture) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getPhone());
            pstmt.setString(7, user.getDepartment());
            pstmt.setString(8, user.getStudentId());
            pstmt.setString(9, user.getProfilePicture());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setUserId(rs.getInt(1));
                    }
                }
                logger.info("User created successfully: " + user.getUsername());
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            logger.error("Error creating user: " + user.getUsername(), e);
            return false;
        } finally {
            dbUtil.closeResources(conn, pstmt);
        }
    }
    
    /**
     * Find user by username
     * @param username The username to search for
     * @return User object if found, null otherwise
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by username: " + username, e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    /**
     * Find user by email
     * @param email The email to search for
     * @return User object if found, null otherwise
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by email: " + email, e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    /**
     * Find user by ID
     * @param userId The user ID to search for
     * @return User object if found, null otherwise
     */
    public User findById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by ID: " + userId, e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return null;
    }
    
    /**
     * Update user information
     * @param user User object with updated information
     * @return true if update was successful
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, department = ?, student_id = ?, " +
                    "profile_picture = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getDepartment());
            pstmt.setString(4, user.getStudentId());
            pstmt.setString(5, user.getProfilePicture());
            pstmt.setInt(6, user.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("User updated successfully: " + user.getUsername());
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            logger.error("Error updating user: " + user.getUsername(), e);
            return false;
        } finally {
            dbUtil.closeResources(conn, pstmt);
        }
    }
    
    /**
     * Update user password
     * @param userId User ID
     * @param newPasswordHash New password hash
     * @return true if update was successful
     */
    public boolean updatePassword(int userId, String newPasswordHash) {
        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPasswordHash);
            pstmt.setInt(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating password for user ID: " + userId, e);
            return false;
        } finally {
            dbUtil.closeResources(conn, pstmt);
        }
    }
    
    /**
     * Get all students (users with role 'student')
     * @return List of all students
     */
    public List<User> getAllStudents() {
        String sql = "SELECT * FROM users WHERE role = 'student' AND is_active = TRUE ORDER BY full_name";
        return executeQueryForList(sql, new Object[0]);
    }
    
    /**
     * Search users by name or email
     * @param searchTerm The search term
     * @return List of matching users
     */
    public List<User> searchUsers(String searchTerm) {
        String sql = "SELECT * FROM users WHERE is_active = TRUE AND " +
                    "(full_name LIKE ? OR email LIKE ? OR username LIKE ?)";
        String pattern = "%" + searchTerm + "%";
        return executeQueryForList(sql, new Object[]{pattern, pattern, pattern});
    }
    
    /**
     * Get total user count
     * @return Total number of active users
     */
    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE is_active = TRUE";
        
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
            logger.error("Error getting user count", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return 0;
    }
    
    /**
     * Get student count
     * @return Number of students
     */
    public int getStudentCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'student' AND is_active = TRUE";
        
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
            logger.error("Error getting student count", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return 0;
    }
    
    /**
     * Check if username already exists
     * @param username The username to check
     * @return true if username exists
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking username existence: " + username, e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return false;
    }
    
    /**
     * Check if email already exists
     * @param email The email to check
     * @return true if email exists
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking email existence: " + email, e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to User object
     * @param rs ResultSet from database query
     * @return User object
     * @throws SQLException if mapping fails
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setPhone(rs.getString("phone"));
        user.setDepartment(rs.getString("department"));
        user.setStudentId(rs.getString("student_id"));
        user.setProfilePicture(rs.getString("profile_picture"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    }
    
    /**
     * Execute a query and return a list of users
     * @param sql SQL query
     * @param params Parameters for the prepared statement
     * @return List of users
     */
    private List<User> executeQueryForList(String sql, Object[] params) {
        List<User> users = new ArrayList<>();
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
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            logger.error("Error executing query for user list", e);
        } finally {
            dbUtil.closeResources(conn, pstmt, rs);
        }
        
        return users;
    }
}