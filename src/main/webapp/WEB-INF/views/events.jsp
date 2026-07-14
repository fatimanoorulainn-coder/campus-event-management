<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="shared/header.jsp" %>

<div class="container mt-5 pt-4">
    <!-- Page Header -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="glass-card p-4">
                <div class="row align-items-center">
                    <div class="col-md-6">
                        <h2 class="text-white">
                            <i class="fas fa-calendar-alt me-2 text-primary"></i>Events
                        </h2>
                        <p class="text-white-50 mb-0">Discover and register for campus events</p>
                    </div>
                    <div class="col-md-6 mt-3 mt-md-0">
                        <form action="${pageContext.request.contextPath}/events" method="get" class="d-flex gap-2">
                            <input type="text" name="search" class="form-control" 
                                   placeholder="Search events..." value="${searchTerm}">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-search"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Filters -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="glass-card p-3">
                <div class="d-flex flex-wrap gap-2">
                    <a href="${pageContext.request.contextPath}/events" 
                       class="btn ${selectedCategory == null ? 'btn-primary' : 'btn-outline-primary'}">
                        <i class="fas fa-list me-1"></i> All
                    </a>
                    <c:forEach var="category" items="${categories}">
                        <a href="${pageContext.request.contextPath}/events?category=${category}" 
                           class="btn ${selectedCategory == category ? 'btn-primary' : 'btn-outline-primary'}">
                            <i class="fas fa-tag me-1"></i> ${category}
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Error Messages -->
    <c:if test="${error != null}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle me-2"></i> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    
    <!-- Events Grid -->
    <div class="row g-4">
        <c:choose>
            <c:when test="${empty events}">
                <div class="col-12">
                    <div class="glass-card p-5 text-center">
                        <i class="fas fa-calendar-times fa-5x text-white-50 mb-4"></i>
                        <h3 class="text-white">No Events Found</h3>
                        <p class="text-white-50">
                            <c:if test="${searchTerm != null}">
                                No events match your search for "<strong>${searchTerm}</strong>"
                            </c:if>
                            <c:if test="${selectedCategory != null}">
                                No events in category "<strong>${selectedCategory}</strong>"
                            </c:if>
                            <c:if test="${searchTerm == null && selectedCategory == null}">
                                There are currently no events available.
                            </c:if>
                        </p>
                        <a href="${pageContext.request.contextPath}/events" class="btn btn-primary mt-3">
                            <i class="fas fa-redo me-2"></i>Clear Filters
                        </a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="event" items="${events}">
                    <div class="col-md-6 col-lg-4">
                        <div class="event-card glass-card p-3 h-100">
                            <div class="event-image-wrapper mb-3">
                                <img src="${event.imageUrl != null ? event.imageUrl : 'https://via.placeholder.com/400x250/6C63FF/FFFFFF?text=Event'}" 
                                     alt="${event.title}" class="event-card-img">
                                <span class="event-badge ${event.getAvailableSpots() > 0 ? 'badge-success' : 'badge-danger'}">
                                    ${event.getAvailableSpots() > 0 ? 'Available' : 'Full'}
                                </span>
                                <span class="event-badge badge-primary" style="right: auto; left: 10px;">
                                    <i class="fas fa-tag me-1"></i> ${event.category}
                                </span>
                            </div>
                            <h5 class="text-white text-truncate">${event.title}</h5>
                            <p class="text-white-50 small mb-2">
                                <i class="fas fa-calendar me-1"></i>
                                <fmt:formatDate value="${event.eventDate}" pattern="MMM dd, yyyy"/>
                                at <fmt:formatDate value="${event.eventTime}" pattern="hh:mm a"/>
                            </p>
                            <p class="text-white-50 small mb-2">
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
                                <c:choose>
                                    <c:when test="${isLoggedIn}">
                                        <c:if test="${event.userRegistered}">
                                            <span class="btn btn-success btn-sm">
                                                <i class="fas fa-check me-1"></i> Registered
                                            </span>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-sm">
                                            <i class="fas fa-sign-in-alt me-1"></i> Login
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="shared/footer.jsp" %>