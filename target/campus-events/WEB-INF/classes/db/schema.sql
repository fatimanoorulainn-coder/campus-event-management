-- Campus Event Management System Database Schema

-- Create database
CREATE DATABASE IF NOT EXISTS campus_events;
USE campus_events;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('student', 'admin') DEFAULT 'student',
    phone VARCHAR(20),
    department VARCHAR(100),
    student_id VARCHAR(20) UNIQUE,
    profile_picture VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_username (username)
);

-- Events table
CREATE TABLE IF NOT EXISTS events (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    event_date DATE NOT NULL,
    event_time TIME NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    image_url VARCHAR(255),
    organizer VARCHAR(100),
    contact_email VARCHAR(100),
    contact_phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id),
    INDEX idx_event_date (event_date),
    INDEX idx_category (category),
    INDEX idx_active (is_active)
);

-- Registrations table
CREATE TABLE IF NOT EXISTS registrations (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('registered', 'cancelled', 'attended') DEFAULT 'registered',
    attended BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    UNIQUE KEY unique_registration (user_id, event_id),
    INDEX idx_user_event (user_id, event_id),
    INDEX idx_status (status)
);

-- Sample data

-- Insert admin user (password: admin123)
INSERT INTO users (username, email, password_hash, full_name, role, phone, department, student_id, profile_picture)
VALUES 
('admin', 'admin@campus.edu', '$2a$10$XBm3hA5HEtUvN5x8ZPPqUe3wKJ6WjKj9XyR8mN5vL2QxP1YFzJ4S', 'System Administrator', 'admin', '1234567890', 'Administration', 'ADMIN001', NULL),
('john_doe', 'john@campus.edu', '$2a$10$XBm3hA5HEtUvN5x8ZPPqUe3wKJ6WjKj9XyR8mN5vL2QxP1YFzJ4S', 'John Doe', 'student', '9876543210', 'Computer Science', 'STU001', NULL),
('jane_smith', 'jane@campus.edu', '$2a$10$XBm3hA5HEtUvN5x8ZPPqUe3wKJ6WjKj9XyR8mN5vL2QxP1YFzJ4S', 'Jane Smith', 'student', '8765432109', 'Engineering', 'STU002', NULL);

-- Insert events
INSERT INTO events (title, description, event_date, event_time, location, capacity, category, image_url, organizer, contact_email, contact_phone, created_by)
VALUES 
('Tech Symposium 2026', 'Annual technology symposium featuring guest speakers, workshops, and networking opportunities for students interested in emerging technologies.', '2026-08-15', '09:00:00', 'Main Auditorium', 200, 'Technology', 'https://via.placeholder.com/800x400/6C63FF/FFFFFF?text=Tech+Symposium', 'Tech Club', 'techclub@campus.edu', '555-0101', 1),
('Career Fair 2026', 'Connect with top employers and explore internship and job opportunities. Bring your resume and dress professionally.', '2026-08-20', '10:00:00', 'Student Center Hall', 150, 'Career', 'https://via.placeholder.com/800x400/FF6B6B/FFFFFF?text=Career+Fair', 'Career Services', 'career@campus.edu', '555-0102', 1),
('Sports Tournament: Basketball', 'Inter-department basketball tournament. Register as a team or as an individual player. Prizes for winners!', '2026-08-25', '14:00:00', 'Sports Complex', 100, 'Sports', 'https://via.placeholder.com/800x400/4ECDC4/FFFFFF?text=Basketball', 'Sports Council', 'sports@campus.edu', '555-0103', 1),
('Art & Culture Festival', 'Celebrate diversity and creativity with art exhibitions, cultural performances, food stalls, and workshops.', '2026-09-01', '11:00:00', 'Open Air Theater', 300, 'Culture', 'https://via.placeholder.com/800x400/FFE66D/FFFFFF?text=Art+Festival', 'Cultural Committee', 'culture@campus.edu', '555-0104', 1),
('Hackathon 2026', '48-hour coding competition. Build innovative solutions to real-world problems. Prizes and mentorship available.', '2026-09-10', '08:00:00', 'Innovation Lab', 80, 'Technology', 'https://via.placeholder.com/800x400/95E1D3/FFFFFF?text=Hackathon', 'CS Department', 'hackathon@campus.edu', '555-0105', 1),
('Guest Lecture: AI & Future', 'Distinguished speaker discussing the future of AI and its impact on various industries. Open to all students.', '2026-09-15', '15:00:00', 'Conference Room A', 120, 'Academic', 'https://via.placeholder.com/800x400/F38181/FFFFFF?text=AI+Lecture', 'AI Research Lab', 'ai@campus.edu', '555-0106', 1);

-- Insert sample registrations
INSERT INTO registrations (user_id, event_id, status) VALUES 
(2, 1, 'registered'),
(2, 2, 'registered'),
(3, 1, 'registered'),
(3, 3, 'registered');

-- Create index for better performance
CREATE INDEX idx_events_date_range ON events(event_date);
CREATE INDEX idx_registrations_user ON registrations(user_id);
CREATE INDEX idx_registrations_event ON registrations(event_id);