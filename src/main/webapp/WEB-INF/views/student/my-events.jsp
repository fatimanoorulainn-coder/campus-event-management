<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../shared/header.jsp" %>

<div class="container mt-5 pt-4">
    <div class="row mb-4">
        <div class="col-12">
            <div class="glass-card p-4">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h2 class="text-white">
                            <i class="fas fa-ticket-alt me-2 text-primary"></i>My Events
                        </h2>
                        <p class="text-white-50">Events you have registered for</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/events" class="btn btn-primary">
                        <i class="fas fa-plus-circle me-2"></i>Browse More Events
                    </a>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Success/Error Messages -->
    <c:if test="${successMessage != null}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle me-2"></i> ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${error != null}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle me-2"></i> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    
    <!-- Registered Events -->
    <div class="row g-4">
        <c:choose>
            <c:when test="${empty registeredEvents}">
                <div class="col-12">
                    <div class="glass-card p-5 text-center">
                        <i class="fas fa-calendar-times fa-5x text-white-50 mb-4"></i>
                        <h3 class="text-white">No Registered Events</h3>
                        <p class="text-white-50">You haven't registered for any events yet.</p>
                        <a href="${pageContext.request.contextPath}/events" class="btn btn-primary btn-lg mt-3">
                            <i class="fas fa-search me-2"></i>Browse Events
                        </a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="event" items="${registeredEvents}">
                    <div class="col-md-6 col-lg-4">
                        <div class="event-card glass-card p-3 h-100">
                            <div class="event-image-wrapper mb-3">
                                <img src="${event.imageUrl != null ? event.imageUrl : 'https://via.placeholder.com/400x250/6C63FF/FFFFFF?text=Event'}" 
                                     alt="${event.title}" class="event-card-img">
                                <span class="event-badge ${event.getAvailableSpots() > 0 ? 'badge-success' : 'badge-danger'}">
                                    ${event.getAvailableSpots() > 0 ? 'Available' : 'Full'}
                                </span>
                                <span class="event-badge badge-primary" style="right: auto; left: 10px;">
                                    <i class="fas fa-check me-1"></i> Registered
                                </span>
                            </div>
                            <h5 class="text-white">${event.title}</h5>
                            <p class="text-white-50 small">
                                <i class="fas fa-calendar me-1"></i>
                                <fmt:formatDate value="${event.eventDate}" pattern="MMM dd, yyyy"/>
                                at <fmt:formatDate value="${event.eventTime}" pattern="hh:mm a"/>
                            </p>
                            <p class="text-white-50 small">
                                <i class="fas fa-map-marker-alt me-1"></i> ${event.location}
                            </p>
                            <p class="text-white-50 small">
                                <i class="fas fa-users me-1"></i> 
                                ${event.registeredCount}/${event.capacity} registered
                            </p>
                            <div class="d-flex gap-2 mt-3">
                                <a href="${pageContext.request.contextPath}/event-details?id=${event.eventId}" 
                                   class="btn btn-outline-primary btn-sm flex-grow-1">
                                    <i class="fas fa-info-circle me-1"></i> Details
                                </a>
                                <button class="btn btn-danger btn-sm" data-bs-toggle="modal" 
                                        data-bs-target="#cancelModal${event.eventId}">
                                    <i class="fas fa-times me-1"></i> Cancel
                                </button>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Cancel Modal -->
                    <div class="modal fade" id="cancelModal${event.eventId}" tabindex="-1">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content glass-card">
                                <div class="modal-header border-secondary">
                                    <h5 class="modal-title text-white">
                                        <i class="fas fa-exclamation-triangle text-warning me-2"></i>Cancel Registration
                                    </h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <p class="text-white">Are you sure you want to cancel your registration for:</p>
                                    <h5 class="text-primary">${event.title}</h5>
                                    <p class="text-white-50 small">
                                        <fmt:formatDate value="${event.eventDate}" pattern="MMM dd, yyyy"/>
                                        at <fmt:formatDate value="${event.eventTime}" pattern="hh:mm a"/>
                                    </p>
                                    <div class="alert alert-warning">
                                        <i class="fas fa-info-circle me-2"></i>
                                        This action cannot be undone.
                                    </div>
                                </div>
                                <div class="modal-footer border-secondary">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                        <i class="fas fa-times me-2"></i>Cancel
                                    </button>
                                    <form action="${pageContext.request.contextPath}/student/my-events" method="post">
                                        <input type="hidden" name="action" value="cancel">
                                        <input type="hidden" name="eventId" value="${event.eventId}">
                                        <button type="submit" class="btn btn-danger">
                                            <i class="fas fa-check me-2"></i>Confirm Cancellation
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="../shared/footer.jsp" %>