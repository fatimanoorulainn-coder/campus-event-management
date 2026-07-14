package com.campus.util;

import org.mindrot.jbcrypt.BCrypt;
import org.apache.log4j.Logger;

/**
 * Utility class for password hashing and verification using BCrypt.
 * Provides secure password management with salt generation.
 */
public class PasswordUtil {
    
    private static final Logger logger = Logger.getLogger(PasswordUtil.class);
    private static final int LOG_ROUNDS = 10; // Work factor for BCrypt
    
    /**
     * Hash a password using BCrypt
     * @param plainPassword The plain text password
     * @return Hashed password string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            logger.error("Cannot hash empty password");
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        try {
            String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
            logger.debug("Password hashed successfully");
            return hashed;
        } catch (Exception e) {
            logger.error("Error hashing password", e);
            throw new RuntimeException("Failed to hash password", e);
        }
    }
    
    /**
     * Verify a plain password against a hashed password
     * @param plainPassword The plain text password to check
     * @param hashedPassword The stored hashed password
     * @return true if password matches
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            logger.error("Cannot verify empty password");
            return false;
        }
        
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            logger.error("Cannot verify against empty hash");
            return false;
        }
        
        try {
            boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
            logger.debug("Password verification " + (matches ? "successful" : "failed"));
            return matches;
        } catch (Exception e) {
            logger.error("Error verifying password", e);
            return false;
        }
    }
    
    /**
     * Check if a password meets security requirements
     * @param password The password to validate
     * @return true if password meets requirements
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        // Check for at least one digit
        boolean hasDigit = false;
        // Check for at least one uppercase letter
        boolean hasUpper = false;
        // Check for at least one lowercase letter
        boolean hasLower = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
        }
        
        return hasDigit && hasUpper && hasLower;
    }
}