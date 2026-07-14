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

/**
 * Servlet for displaying event details.
 * Shows full event information and allows registration/cancellation.
 */
@WebServlet("/event-details")
public class EventDetailsServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(EventDetailsServlet.class);
    private AuthService authService;
    private EventService eventService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        eventService = new EventService();
        logger.info("EventDetailsServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String eventIdParam = request.getParameter("id");
        
        if (eventIdParam == null || eventIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/events");
            return;
        }
        
        try {
            int eventId = Integer.parseInt(eventIdParam);
            
            // Check if user is logged in
            HttpSession session = request.getSession(false);
            Integer userId = null;
            boolean isLoggedIn = authService.isLoggedIn(session);
            boolean isAdmin = false;
            
            if (isLoggedIn) {
                User user = authService.getCurrentUser(session);
                userId = user.getUserId();
                isAdmin = user.isAdmin();
            }
            
            // Get event details with registration status
            Event event = eventService.getEventById(eventId, userId);
            
            if (event == null) {
                response.sendRedirect(request.getContextPath() + "/events?error=notfound");
                return;
            }
            
            // Get categories for related events
            java.util.List<String> categories = eventService.getAllCategories();
            
            request.setAttribute("event", event);
            request.setAttribute("categories", categories);
            request.setAttribute("isLoggedIn", isLoggedIn);
            request.setAttribute("isAdmin", isAdmin);
            
            // Check for registration messages
            String registered = request.getParameter("registered");
            if ("success".equals(registered)) {
                request.setAttribute("successMessage", "Successfully registered for this event!");
            } else if ("cancelled".equals(registered)) {
                request.setAttribute("successMessage", "Registration cancelled successfully!");
            }
            
            String error = request.getParameter("error");
            if ("failed".equals(error)) {
                request.setAttribute("error", "An error occurred. Please try again.");
            } else if ("full".equals(error)) {
                request.setAttribute("error", "Sorry, this event is already full.");
            }
            
            request.getRequestDispatcher("/WEB-INF/views/event-details.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            logger.error("Invalid event ID", e);
            response.sendRedirect(request.getContextPath() + "/events?error=invalid");
        } catch (Exception e) {
            logger.error("Error loading event details", e);
            request.setAttribute("error", "An error occurred while loading event details");
            request.getRequestDispatcher("/WEB-INF/views/event-details.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (!authService.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = authService.getCurrentUser(session);
        String action = request.getParameter("action");
        String eventIdParam = request.getParameter("eventId");
        
        if (eventIdParam == null || eventIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/events");
            return;
        }
        
        try {
            int eventId = Integer.parseInt(eventIdParam);
            int userId = currentUser.getUserId();
            
            if ("register".equals(action)) {
                boolean registered = eventService.registerForEvent(userId, eventId);
                if (registered) {
                    response.sendRedirect(request.getContextPath() + "/event-details?id=" + eventId + "&registered=success");
                } else {
                    // Check if event is full
                    Event event = eventService.getEventById(eventId, null);
                    if (event != null && event.isFull()) {
                        response.sendRedirect(request.getContextPath() + "/event-details?id=" + eventId + "&error=full");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/event-details?id=" + eventId + "&error=failed");
                    }
                }
            } else if ("cancel".equals(action)) {
                boolean cancelled = eventService.cancelRegistration(userId, eventId);
                if (cancelled) {
                    response.sendRedirect(request.getContextPath() + "/event-details?id=" + eventId + "&cancelled=success");
                } else {
                    response.sendRedirect(request.getContextPath() + "/event-details?id=" + eventId + "&error=failed");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/event-details?id=" + eventId);
            }
            
        } catch (NumberFormatException e) {
            logger.error("Invalid event ID", e);
            response.sendRedirect(request.getContextPath() + "/events");
        } catch (Exception e) {
            logger.error("Error in event details POST", e);
            response.sendRedirect(request.getContextPath() + "/events");
        }
    }
}