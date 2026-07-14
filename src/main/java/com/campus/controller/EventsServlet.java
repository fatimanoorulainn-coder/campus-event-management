package com.campus.controller;

import com.campus.model.Event;
import com.campus.model.User;
import com.campus.service.AuthService;
import com.campus.service.EventService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for displaying events listing.
 * Shows all events with search and filter functionality.
 */
@WebServlet("/events")
public class EventsServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(EventsServlet.class);
    private AuthService authService;
    private EventService eventService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        eventService = new EventService();
        logger.info("EventsServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer userId = null;
        boolean isLoggedIn = false;
        
        // Check if user is logged in
        if (authService.isLoggedIn(session)) {
            User user = authService.getCurrentUser(session);
            userId = user.getUserId();
            isLoggedIn = true;
        }
        
        try {
            // Get search and filter parameters
            String searchParam = request.getParameter("search");
            String categoryParam = request.getParameter("category");
            
            List<Event> events;
            List<String> categories = eventService.getAllCategories();
            
            if (searchParam != null && !searchParam.trim().isEmpty()) {
                // Search events
                events = eventService.searchEvents(searchParam.trim(), userId);
                request.setAttribute("searchTerm", searchParam.trim());
            } else if (categoryParam != null && !categoryParam.trim().isEmpty()) {
                // Filter by category
                events = eventService.getEventsByCategory(categoryParam.trim(), userId);
                request.setAttribute("selectedCategory", categoryParam.trim());
            } else {
                // Get all events
                events = eventService.getAllEvents(userId);
            }
            
            request.setAttribute("events", events);
            request.setAttribute("categories", categories);
            request.setAttribute("isLoggedIn", isLoggedIn);
            
            request.getRequestDispatcher("/WEB-INF/views/events.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading events", e);
            request.setAttribute("error", "An error occurred while loading events");
            request.getRequestDispatcher("/WEB-INF/views/events.jsp").forward(request, response);
        }
    }
}