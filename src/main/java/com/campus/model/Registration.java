package com.campus.model;

import java.sql.Timestamp;
import java.time.LocalDate;
/**
 * Registration entity representing an event registration.
 * Maps to the 'registrations' table in the database.
 */
public class Registration {
    private int registrationId;
    private int userId;
    private int eventId;
    private Timestamp registrationDate;
    private String status; // 'registered', 'cancelled', 'attended'
    private boolean attended;
    
    // Additional fields for display purposes
    private String eventTitle;
    private String userName;
    private LocalDate eventDate;
    private String eventLocation;
    
    // Constructors
    public Registration() {
        this.status = "registered";
        this.attended = false;
    }
    
    // Getters and Setters
    public int getRegistrationId() {
        return registrationId;
    }
    
    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getEventId() {
        return eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    
    public Timestamp getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isAttended() {
        return attended;
    }
    
    public void setAttended(boolean attended) {
        this.attended = attended;
    }
    
    public String getEventTitle() {
        return eventTitle;
    }
    
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public LocalDate getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
    
    public String getEventLocation() {
        return eventLocation;
    }
    
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
    
    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(status);
    }
}