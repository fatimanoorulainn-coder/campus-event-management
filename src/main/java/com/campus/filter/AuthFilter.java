package com.campus.filter;

import com.campus.service.AuthService;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Authentication filter to protect resources and enforce role-based access control.
 * Intercepts requests and checks if user is authenticated and authorized.
 */
public class AuthFilter implements Filter {
    
    private static final Logger logger = Logger.getLogger(AuthFilter.class);
    
    // Public pages accessible without authentication
    private static final Set<String> PUBLIC_PATHS = new HashSet<>(Arrays.asList(
        "/",
        "/index.jsp",
        "/login",
        "/register",
        "/about",
        "/events",           // Public event listing
        "/event-details",    // Public event details
        "/css/",
        "/js/",
        "/assets/",
        "/WEB-INF/"
    ));
    
    // Pages accessible only by students
    private static final Set<String> STUDENT_PATHS = new HashSet<>(Arrays.asList(
        "/student/dashboard",
        "/student/profile",
        "/student/my-events"
    ));
    
    // Pages accessible only by admins
    private static final Set<String> ADMIN_PATHS = new HashSet<>(Arrays.asList(
        "/admin/dashboard",
        "/admin/events",
        "/admin/create-event",
        "/admin/edit-event",
        "/admin/users",
        "/admin/registrations"
    ));
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Authentication Filter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        String path = req.getServletPath();
        String contextPath = req.getContextPath();
        
        logger.debug("Filter processing request: " + path);
        
        // Check if the path is public
        if (isPublicPath(path)) {
            logger.debug("Public path: " + path + " - allowing access");
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        boolean isLoggedIn = isUserLoggedIn(session);
        
        if (!isLoggedIn) {
            logger.warn("Unauthenticated access attempt to: " + path);
            // Store the requested URL to redirect after login
            String requestedUrl = req.getRequestURL().toString();
            if (req.getQueryString() != null) {
                requestedUrl += "?" + req.getQueryString();
            }
            session = req.getSession(true);
            session.setAttribute("redirectAfterLogin", requestedUrl);
            
            // Redirect to login page
            res.sendRedirect(contextPath + "/login");
            return;
        }
        
        // Check role-based access
        String role = getRoleFromSession(session);
        
        if (isAdminPath(path) && !"admin".equals(role)) {
            logger.warn("Access denied: " + role + " attempted to access admin path: " + path);
            res.sendRedirect(contextPath + "/access-denied");
            return;
        }
        
        if (isStudentPath(path) && !"student".equals(role)) {
            logger.warn("Access denied: " + role + " attempted to access student path: " + path);
            res.sendRedirect(contextPath + "/access-denied");
            return;
        }
        
        logger.debug("Access granted to: " + path + " for role: " + role);
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        logger.info("Authentication Filter destroyed");
    }
    
    /**
     * Check if the path is public (accessible without authentication)
     * @param path The request path
     * @return true if path is public
     */
    private boolean isPublicPath(String path) {
        if (path == null) {
            return false;
        }
        
        // Check exact matches
        if (PUBLIC_PATHS.contains(path)) {
            return true;
        }
        
        // Check path prefixes
        for (String publicPath : PUBLIC_PATHS) {
            if (publicPath.endsWith("/") && path.startsWith(publicPath)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if the path is for admin only
     * @param path The request path
     * @return true if path is admin-only
     */
    private boolean isAdminPath(String path) {
        if (path == null) {
            return false;
        }
        
        for (String adminPath : ADMIN_PATHS) {
            if (path.equals(adminPath) || path.startsWith(adminPath + "/")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if the path is for students only
     * @param path The request path
     * @return true if path is student-only
     */
    private boolean isStudentPath(String path) {
        if (path == null) {
            return false;
        }
        
        for (String studentPath : STUDENT_PATHS) {
            if (path.equals(studentPath) || path.startsWith(studentPath + "/")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if user is logged in
     * @param session The HttpSession
     * @return true if user is logged in
     */
    private boolean isUserLoggedIn(HttpSession session) {
        if (session == null) {
            return false;
        }
        
        AuthService authService = new AuthService();
        return authService.isLoggedIn(session);
    }
    
    /**
     * Get user role from session
     * @param session The HttpSession
     * @return Role string or null if not logged in
     */
    private String getRoleFromSession(HttpSession session) {
        if (session == null) {
            return null;
        }
        
        Object roleObj = session.getAttribute(AuthService.SESSION_ROLE);
        if (roleObj instanceof String) {
            return (String) roleObj;
        }
        
        return null;
    }
}