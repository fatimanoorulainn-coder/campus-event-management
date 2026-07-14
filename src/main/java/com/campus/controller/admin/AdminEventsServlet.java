package com.campus.controller.admin;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Servlet for Admin Event Management.
 * Allows admins to create, view, update, and delete events.
 */
@WebServlet("/admin/events")
public class AdminEventsServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(AdminEventsServlet.class);
    private AuthService authService;
    private EventService eventService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        eventService = new EventService();
        logger.info("AdminEventsServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (!authService.isLoggedIn(session) || !authService.isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        String eventIdParam = request.getParameter("id");
        
        try {
            if ("create".equals(action)) {
                // Show create event form
                List<String> categories = eventService.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/views/admin/create-event.jsp").forward(request, response);
                
            } else if ("edit".equals(action) && eventIdParam != null) {
                // Show edit event form
                int eventId = Integer.parseInt(eventIdParam);
                Event event = eventService.getEventById(eventId, null);
                if (event != null) {
                    List<String> categories = eventService.getAllCategories();
                    request.setAttribute("event", event);
                    request.setAttribute("categories", categories);
                    request.getRequestDispatcher("/WEB-INF/views/admin/edit-event.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/events?error=notfound");
                }
                
            } else if ("view".equals(action) && eventIdParam != null) {
                // View event details (admin view with attendees)
                int eventId = Integer.parseInt(eventIdParam);
                Event event = eventService.getEventById(eventId, null);
                if (event != null) {
                    request.setAttribute("event", event);
                    request.getRequestDispatcher("/WEB-INF/views/admin/view-event.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/events?error=notfound");
                }
                
            } else if ("delete".equals(action) && eventIdParam != null) {
                // Handle delete (POST method would be better, but GET is simpler for this)
                int eventId = Integer.parseInt(eventIdParam);
                boolean deleted = eventService.deleteEvent(eventId);
                if (deleted) {
                    response.sendRedirect(request.getContextPath() + "/admin/events?deleted=success");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/events?error=deletefailed");
                }
                
            } else {
                // List all events
                List<Event> events = eventService.getAllEvents(null);
                List<String> categories = eventService.getAllCategories();
                
                // Check for messages
                String deleted = request.getParameter("deleted");
                if ("success".equals(deleted)) {
                    request.setAttribute("successMessage", "Event deleted successfully!");
                }
                
                String created = request.getParameter("created");
                if ("success".equals(created)) {
                    request.setAttribute("successMessage", "Event created successfully!");
                }
                
                String updated = request.getParameter("updated");
                if ("success".equals(updated)) {
                    request.setAttribute("successMessage", "Event updated successfully!");
                }
                
                request.setAttribute("events", events);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/views/admin/events.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            logger.error("Invalid event ID", e);
            response.sendRedirect(request.getContextPath() + "/admin/events?error=invalidid");
        } catch (Exception e) {
            logger.error("Error in admin events", e);
            request.setAttribute("error", "An error occurred");
            response.sendRedirect(request.getContextPath() + "/admin/events");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (!authService.isLoggedIn(session) || !authService.isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        User currentUser = authService.getCurrentUser(session);
        
        try {
            if ("create".equals(action)) {
                // Create new event
                Event event = buildEventFromRequest(request);
                event.setCreatedBy(currentUser.getUserId());
                
                boolean created = eventService.createEvent(event, currentUser);
                if (created) {
                    response.sendRedirect(request.getContextPath() + "/admin/events?created=success");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/events?error=createfailed");
                }
                
            } else if ("update".equals(action)) {
                // Update existing event
                Event event = buildEventFromRequest(request);
                String eventIdParam = request.getParameter("eventId");
                
                if (eventIdParam != null) {
                    event.setEventId(Integer.parseInt(eventIdParam));
                    boolean updated = eventService.updateEvent(event);
                    if (updated) {
                        response.sendRedirect(request.getContextPath() + "/admin/events?updated=success");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/admin/events?error=updatefailed");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/events?error=missingid");
                }
                
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/events");
            }
            
        } catch (Exception e) {
            logger.error("Error in admin events POST", e);
            response.sendRedirect(request.getContextPath() + "/admin/events?error=exception");
        }
    }
    
    /**
     * Build an Event object from request parameters
     */
    private Event buildEventFromRequest(HttpServletRequest request) {
        Event event = new Event();
        
        event.setTitle(request.getParameter("title"));
        event.setDescription(request.getParameter("description"));
        event.setLocation(request.getParameter("location"));
        event.setCategory(request.getParameter("category"));
        event.setOrganizer(request.getParameter("organizer"));
        event.setContactEmail(request.getParameter("contactEmail"));
        event.setContactPhone(request.getParameter("contactPhone"));
        event.setImageUrl(request.getParameter("imageUrl"));
        
        // Parse date and time
        String dateStr = request.getParameter("eventDate");
        if (dateStr != null && !dateStr.isEmpty()) {
            event.setEventDate(LocalDate.parse(dateStr));
        }
        
        String timeStr = request.getParameter("eventTime");
        if (timeStr != null && !timeStr.isEmpty()) {
            event.setEventTime(LocalTime.parse(timeStr));
        }
        
        // Parse capacity
        String capacityStr = request.getParameter("capacity");
        if (capacityStr != null && !capacityStr.isEmpty()) {
            event.setCapacity(Integer.parseInt(capacityStr));
        }
        
        return event;
    }
}