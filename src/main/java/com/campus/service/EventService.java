package com.campus.service;

import com.campus.dao.EventDAO;
import com.campus.dao.RegistrationDAO;
import com.campus.model.Event;
import com.campus.model.User;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import com.campus.dao.UserDAO;

/**
 * Service class for event operations.
 * Handles business logic for events and registrations.
 */
public class EventService {
    
    private static final Logger logger = Logger.getLogger(EventService.class);
    private final EventDAO eventDAO;
    private final RegistrationDAO registrationDAO;
    
    public EventService() {
        this.eventDAO = new EventDAO();
        this.registrationDAO = new RegistrationDAO();
    }
    
    /**
     * Create a new event
     * @param event The event to create
     * @param user The user creating the event
     * @return true if event created successfully
     */
    public boolean createEvent(Event event, User user) {
        if (event == null || user == null) {
            logger.error("Cannot create event: Invalid parameters");
            return false;
        }
        
        // Validate event data
        if (!validateEventData(event)) {
            logger.warn("Event validation failed");
            return false;
        }
        
        // Check if event date is not in the past
        if (event.getEventDate().isBefore(LocalDate.now())) {
            logger.warn("Event date cannot be in the past");
            return false;
        }
        
        event.setCreatedBy(user.getUserId());
        
        boolean created = eventDAO.createEvent(event);
        
        if (created) {
            logger.info("Event created successfully by " + user.getUsername() + ": " + event.getTitle());
            return true;
        } else {
            logger.error("Failed to create event: Database error");
            return false;
        }
    }
    
    /**
     * Get event by ID with user registration status
     * @param eventId The event ID
     * @param userId The user ID (can be null)
     * @return Event object
     */
    public Event getEventById(int eventId, Integer userId) {
        Event event = eventDAO.findById(eventId);
        
        if (event != null && userId != null) {
            // Check if user is registered
            boolean registered = registrationDAO.isUserRegistered(userId, eventId);
            event.setUserRegistered(registered);
        }
        
        return event;
    }
    
    /**
     * Get all events with user registration status
     * @param userId The user ID
     * @return List of events
     */
    public List<Event> getAllEvents(Integer userId) {
        if (userId != null) {
            return eventDAO.getAllEventsWithUserStatus(userId);
        } else {
            return eventDAO.getAllEvents();
        }
    }
    
    /**
     * Get upcoming events
     * @return List of upcoming events
     */
    public List<Event> getUpcomingEvents() {
        return eventDAO.getUpcomingEvents();
    }
    
    /**
     * Search events
     * @param searchTerm The search term
     * @param userId The user ID (can be null)
     * @return List of matching events
     */
    public List<Event> searchEvents(String searchTerm, Integer userId) {
        List<Event> events = eventDAO.searchEvents(searchTerm);
        
        // Add registration status if user is logged in
        if (userId != null) {
            for (Event event : events) {
                boolean registered = registrationDAO.isUserRegistered(userId, event.getEventId());
                event.setUserRegistered(registered);
            }
        }
        
        return events;
    }
    
    /**
     * Get events by category
     * @param category The category
     * @param userId The user ID (can be null)
     * @return List of events
     */
    public List<Event> getEventsByCategory(String category, Integer userId) {
        List<Event> events = eventDAO.getEventsByCategory(category);
        
        if (userId != null) {
            for (Event event : events) {
                boolean registered = registrationDAO.isUserRegistered(userId, event.getEventId());
                event.setUserRegistered(registered);
            }
        }
        
        return events;
    }
    
    /**
     * Update an event
     * @param event The updated event
     * @return true if update successful
     */
    public boolean updateEvent(Event event) {
        if (event == null) {
            logger.error("Cannot update event: Event is null");
            return false;
        }
        
        if (!validateEventData(event)) {
            logger.warn("Event validation failed for update");
            return false;
        }
        
        boolean updated = eventDAO.updateEvent(event);
        
        if (updated) {
            logger.info("Event updated successfully: " + event.getTitle());
            return true;
        } else {
            logger.error("Failed to update event: " + event.getTitle());
            return false;
        }
    }
    
    /**
     * Delete an event
     * @param eventId The event ID
     * @return true if deletion successful
     */
    public boolean deleteEvent(int eventId) {
        boolean deleted = eventDAO.deleteEvent(eventId);
        
        if (deleted) {
            logger.info("Event deleted successfully: " + eventId);
            return true;
        } else {
            logger.error("Failed to delete event: " + eventId);
            return false;
        }
    }
    
    /**
     * Register a user for an event
     * @param userId The user ID
     * @param eventId The event ID
     * @return true if registration successful
     */
    public boolean registerForEvent(int userId, int eventId) {
        // Check if event exists and has capacity
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            logger.warn("Event not found: " + eventId);
            return false;
        }
        
        if (event.isFull()) {
            logger.warn("Event is full: " + eventId);
            return false;
        }
        
        // Check if already registered
        if (registrationDAO.isUserRegistered(userId, eventId)) {
            logger.warn("User already registered for event: " + eventId);
            return false;
        }
        
        boolean registered = registrationDAO.registerUserForEvent(userId, eventId);
        
        if (registered) {
            logger.info("User " + userId + " registered for event " + eventId);
            return true;
        } else {
            logger.error("Failed to register user " + userId + " for event " + eventId);
            return false;
        }
    }
    
    /**
     * Cancel a user's registration for an event
     * @param userId The user ID
     * @param eventId The event ID
     * @return true if cancellation successful
     */
    public boolean cancelRegistration(int userId, int eventId) {
        // Check if user is registered
        if (!registrationDAO.isUserRegistered(userId, eventId)) {
            logger.warn("User " + userId + " is not registered for event " + eventId);
            return false;
        }
        
        boolean cancelled = registrationDAO.cancelRegistration(userId, eventId);
        
        if (cancelled) {
            logger.info("User " + userId + " cancelled registration for event " + eventId);
            return true;
        } else {
            logger.error("Failed to cancel registration for user " + userId + " and event " + eventId);
            return false;
        }
    }
    
    /**
     * Get all categories
     * @return List of categories
     */
    public List<String> getAllCategories() {
        return eventDAO.getAllCategories();
    }
    
    /**
     * Get dashboard statistics
     * @return Object array with stats [totalEvents, totalRegistrations, totalUsers, totalStudents]
     */
    public Object[] getDashboardStats() {
        int totalEvents = eventDAO.getEventCount();
        int totalRegistrations = eventDAO.getTotalRegistrations();
        int totalUsers = 0;
        int totalStudents = 0;
        
        try {
            UserDAO userDAO = new UserDAO();
            totalUsers = userDAO.getUserCount();
            totalStudents = userDAO.getStudentCount();
        } catch (Exception e) {
            logger.error("Error getting user stats", e);
        }
        
        return new Object[]{totalEvents, totalRegistrations, totalUsers, totalStudents};
    }
    
    /**
     * Validate event data
     * @param event The event to validate
     * @return true if event data is valid
     */
    private boolean validateEventData(Event event) {
        if (event == null) {
            return false;
        }
        
        // Validate required fields
        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            logger.warn("Event validation failed: Title is required");
            return false;
        }
        
        if (event.getDescription() == null || event.getDescription().trim().isEmpty()) {
            logger.warn("Event validation failed: Description is required");
            return false;
        }
        
        if (event.getEventDate() == null) {
            logger.warn("Event validation failed: Date is required");
            return false;
        }
        
        if (event.getEventTime() == null) {
            logger.warn("Event validation failed: Time is required");
            return false;
        }
        
        if (event.getLocation() == null || event.getLocation().trim().isEmpty()) {
            logger.warn("Event validation failed: Location is required");
            return false;
        }
        
        if (event.getCapacity() <= 0) {
            logger.warn("Event validation failed: Capacity must be greater than 0");
            return false;
        }
        
        if (event.getCategory() == null || event.getCategory().trim().isEmpty()) {
            logger.warn("Event validation failed: Category is required");
            return false;
        }
        
        return true;
    }
}