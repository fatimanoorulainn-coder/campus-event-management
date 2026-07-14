<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../shared/header.jsp" %>

<!-- Student Dashboard -->
<div class="container mt-5 pt-4">
    <!-- Welcome Section -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="glass-card p-4 p-md-5">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <h1 class="display-5 fw-bold text-white">
                            Welcome back, ${sessionScope.user.fullName}! 👋
                        </h1>
                        <p class="text-white-50 fs-5">
                            <i class="fas fa-calendar-check me-2"></i>
                            You're registered for <strong>${totalRegistered}</strong> event${totalRegistered != 1 ? 's' : ''}
                        </p>
                    </div>
                    <div class="col-md-4 text-md-end mt-3 mt-md-0">
                        <a href="${pageContext.request.contextPath}/events" class="btn btn-primary btn-lg">
                            <i class="fas fa-plus-circle me-2"></i>Browse Events
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Success Message -->
    <c:if test="${successMessage != null}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle me-2"></i> ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    
    <!-- Stats Cards -->
    <div class="row g-4 mb-4">
        <div class="col-md-4">
            <div class="glass-card p-4 text-center stat-card">
                <div class="stat-icon bg-primary-gradient">
                    <i class="fas fa-calendar-alt fa-2x"></i>
                </div>
                <h3 class="text-white mt-3">${allEvents.size()}</h3>
                <p class="text-white-50 mb-0">Total Events</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="glass-card p-4 text-center stat-card">
                <div class="stat-icon bg-success-gradient">
                    <i class="fas fa-ticket-alt fa-2x"></i>
                </div>
                <h3 class="text-white mt-3">${totalRegistered}</h3>
                <p class="text-white-50 mb-0">My Registrations</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="glass-card p-4 text-center stat-card">
                <div class="stat-icon bg-warning-gradient">
                    <i class="fas fa-clock fa-2x"></i>
                </div>
                <h3 class="text-white mt-3">${upcomingEvents.size()}</h3>
                <p class="text-white-50 mb-0">Upcoming Events</p>
            </div>
        </div>
    </div>
    
    <!-- My Registered Events -->
    <div class="row g-4 mb-4">
        <div class="col-12">
            <div class="glass-card p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h3 class="text-white mb-0">
                        <i class="fas fa-ticket-alt me-2 text-primary"></i>My Events
                    </h3>
                    <a href="${pageContext.request.contextPath}/student/my-events" class="btn btn-outline-primary">
                        View All <i class="fas fa-arrow-right ms-2"></i>
                    </a>
                </div>
                
                <c:choose>
                    <c:when test="${empty registeredEvents}">
                        <div class="text-center py-5">
                            <i class="fas fa-calendar-times fa-4x text-white-50 mb-3"></i>
                            <h4 class="text-white-50">No registered events</h4>
                            <p class="text-white-50">Browse events and register for your favorites!</p>
                            <a href="${pageContext.request.contextPath}/events" class="btn btn-primary mt-2">
                                Browse Events
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row g-4">
                            <c:forEach var="event" items="${registeredEvents}" end="3">
                                <div class="col-md-6 col-lg-3">
                                    <div class="event-card glass-card p-3 h-100">
                                        <div class="event-image-wrapper mb-3">
                                            <img src="${event.imageUrl != null ? event.imageUrl : 'https://via.placeholder.com/300x200/6C63FF/FFFFFF?text=Event'}" 
                                                 alt="${event.title}" class="event-card-img">
                                            <span class="event-badge ${event.isFull() ? 'badge-danger' : 'badge-success'}">
                                                ${event.isFull() ? 'Full' : 'Available'}
                                            </span>
                                        </div>
                                        <h5 class="text-white text-truncate">${event.title}</h5>
                                        <p class="text-white-50 small mb-2">
                                            <i class="fas fa-calendar me-1"></i>
                                            <fmt:formatDate value="${event.eventDate}" pattern="MMM dd, yyyy"/>
                                        </p>
                                        <p class="text-white-50 small mb-2">
                                            <i class="fas fa-map-marker-alt me-1"></i> ${event.location}
                                        </p>
                                        <p class="text-white-50 small">
                                            <i class="fas fa-users me-1"></i> 
                                            ${event.registeredCount}/${event.capacity} registered
                                        </p>
                                        <a href="${pageContext.request.contextPath}/event-details?id=${event.eventId}" 
                                           class="btn btn-primary btn-sm w-100 mt-2">
                                            View Details
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    
    <!-- Upcoming Events -->
    <div class="row g-4">
        <div class="col-12">
            <div class="glass-card p-4">
                <h3 class="text-white mb-4">
                    <i class="fas fa-clock me-2 text-warning"></i>Upcoming Events
                </h3>
                <div class="table-responsive">
                    <table class="table table-dark table-hover table-striped">
                        <thead>
                            <tr>
                                <th>Event</th>
                                <th>Date</th>
                                <th>Location</th>
                                <th>Available Spots</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty upcomingEvents}">
                                    <tr>
                                        <td colspan="5" class="text-center text-white-50 py-3">
                                            No upcoming events scheduled
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="event" items="${upcomingEvents}" end="4">
                                        <tr>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/event-details?id=${event.eventId}" 
                                                   class="text-white text-decoration-none">
                                                    ${event.title}
                                                </a>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${event.eventDate}" pattern="MMM dd, yyyy"/>
                                                at <fmt:formatDate value="${event.eventTime}" pattern="hh:mm a"/>
                                            </td>
                                            <td>${event.location}</td>
                                            <td>
                                                <span class="badge ${event.getAvailableSpots() > 0 ? 'bg-success' : 'bg-danger'}">
                                                    ${event.getAvailableSpots()} spots left
                                                </span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${event.userRegistered}">
                                                        <a href="${pageContext.request.contextPath}/event-details?id=${event.eventId}" 
                                                           class="btn btn-outline-info btn-sm">
                                                            <i class="fas fa-check me-1"></i> Registered
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:if test="${!event.isFull()}">
                                                            <a href="${pageContext.request.contextPath}/event-details?id=${event.eventId}" 
                                                               class="btn btn-primary btn-sm">
                                                                <i class="fas fa-plus me-1"></i> Register
                                                            </a>
                                                        </c:if>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../shared/footer.jsp" %>