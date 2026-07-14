package com.campus.service;

import com.campus.dao.UserDAO;
import com.campus.model.User;
import com.campus.util.PasswordUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

/**
 * Service class for authentication operations.
 * Handles login, logout, and session management.
 */
public class AuthService {
    
    private static final Logger logger = Logger.getLogger(AuthService.class);
    private final UserDAO userDAO;
    
    // Session attribute keys
    public static final String SESSION_USER = "user";
    public static final String SESSION_USER_ID = "userId";
    public static final String SESSION_ROLE = "role";
    public static final String SESSION_USERNAME = "username";
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Authenticate a user with username/email and password
     * @param usernameOrEmail The username or email
     * @param password The password
     * @return User object if authentication successful, null otherwise
     */
    public User authenticate(String usernameOrEmail, String password) {
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty() || 
            password == null || password.isEmpty()) {
            logger.warn("Authentication attempted with empty credentials");
            return null;
        }
        
        try {
            // Try to find by username first
            User user = userDAO.findByUsername(usernameOrEmail);
            
            // If not found by username, try email
            if (user == null) {
                user = userDAO.findByEmail(usernameOrEmail);
            }
            
            if (user == null) {
                logger.warn("User not found: " + usernameOrEmail);
                return null;
            }
            
            // Verify password
            if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                logger.info("User authenticated successfully: " + user.getUsername());
                return user;
            } else {
                logger.warn("Invalid password for user: " + usernameOrEmail);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error during authentication for user: " + usernameOrEmail, e);
            return null;
        }
    }
    
    /**
     * Register a new user
     * @param user User object with registration details
     * @param password Plain text password
     * @return true if registration successful
     */
    public boolean registerUser(User user, String password) {
        if (user == null || password == null || password.isEmpty()) {
            logger.error("Registration failed: Invalid user or password");
            return false;
        }
        
        try {
            // Validate password strength
            if (!PasswordUtil.isValidPassword(password)) {
                logger.warn("Registration failed: Password does not meet requirements");
                return false;
            }
            
            // Check if username already exists
            if (userDAO.usernameExists(user.getUsername())) {
                logger.warn("Registration failed: Username already exists: " + user.getUsername());
                return false;
            }
            
            // Check if email already exists
            if (userDAO.emailExists(user.getEmail())) {
                logger.warn("Registration failed: Email already exists: " + user.getEmail());
                return false;
            }
            
            // Hash the password
            String hashedPassword = PasswordUtil.hashPassword(password);
            user.setPasswordHash(hashedPassword);
            
            // Create user
            boolean created = userDAO.createUser(user);
            
            if (created) {
                logger.info("User registered successfully: " + user.getUsername());
                return true;
            } else {
                logger.error("Registration failed: Database error");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return false;
        }
    }
    
    /**
     * Create a session for a user
     * @param session The HttpSession object
     * @param user The User object
     */
    public void createSession(HttpSession session, User user) {
        if (session == null || user == null) {
            logger.error("Cannot create session: Invalid parameters");
            return;
        }
        
        session.setAttribute(SESSION_USER, user);
        session.setAttribute(SESSION_USER_ID, user.getUserId());
        session.setAttribute(SESSION_ROLE, user.getRole());
        session.setAttribute(SESSION_USERNAME, user.getUsername());
        
        // Set session timeout to 30 minutes
        session.setMaxInactiveInterval(30 * 60);
        
        logger.info("Session created for user: " + user.getUsername());
    }
    
    /**
     * Get the current user from session
     * @param session The HttpSession object
     * @return User object if logged in, null otherwise
     */
    public User getCurrentUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        
        Object userObj = session.getAttribute(SESSION_USER);
        if (userObj instanceof User) {
            return (User) userObj;
        }
        
        return null;
    }
    
    /**
     * Check if user is logged in
     * @param session The HttpSession object
     * @return true if user is logged in
     */
    public boolean isLoggedIn(HttpSession session) {
        return getCurrentUser(session) != null;
    }
    
    /**
     * Check if user has admin role
     * @param session The HttpSession object
     * @return true if user is an admin
     */
    public boolean isAdmin(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && user.isAdmin();
    }
    
    /**
     * Check if user has student role
     * @param session The HttpSession object
     * @return true if user is a student
     */
    public boolean isStudent(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && user.isStudent();
    }
    
    /**
     * Logout user - invalidate session
     * @param session The HttpSession object
     */
    public void logout(HttpSession session) {
        if (session != null) {
            String username = (String) session.getAttribute(SESSION_USERNAME);
            session.invalidate();
            logger.info("User logged out: " + username);
        }
    }
}