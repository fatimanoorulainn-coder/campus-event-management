<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="shared/header.jsp" %>

<div class="container mt-5 pt-4">
    <!-- Hero Section -->
    <div class="row mb-5">
        <div class="col-12">
            <div class="glass-card p-4 p-md-5 text-center" style="background: linear-gradient(135deg, rgba(108,99,255,0.15), rgba(78,205,196,0.08));">
                <h1 class="display-4 fw-bold text-white mb-3">
                    <i class="fas fa-calendar-alt text-primary me-3"></i>
                    Discover Events
                </h1>
                <p class="text-white-50 fs-5 mb-4">
                    Find and register for exciting campus events
                </p>
                
                <!-- Search Bar -->
                <div class="row justify-content-center">
                    <div class="col-lg-8 col-md-10">
                        <form action="${pageContext.request.contextPath}/events" method="get" class="search-form">
                            <div class="input-group input-group-lg">
                                <span class="input-group-text bg-transparent border-end-0">
                                    <i class="fas fa-search text-primary"></i>
                                </span>
                                <input type="text" name="search" class="form-control border-start-0" 
                                       placeholder="Search events by title, description, or location..."
                                       value="${searchTerm}">
                                <button type="submit" class="btn btn-primary px-4">
                                    <i class="fas fa-search me-2"></i>Search
                                </button>
                            </div>
                        </form>
                        
                        <!-- Active Filters Display -->
                        <c:if test="${searchTerm != null || selectedCategory != null}">
                            <div class="mt-3 d-flex flex-wrap gap-2 justify-content-center">
                                <span class="badge bg-primary bg-opacity-25 text-white px-3 py-2">
                                    <i class="fas fa-filter me-2"></i>
                                    <c:if test="${searchTerm != null}">
                                        Search: "${searchTerm}"
                                    </c:if>
                                    <c:if test="${selectedCategory != null}">
                                        Category: ${selectedCategory}
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/events" class="text-white ms-2 text-decoration-none">
                                        <i class="fas fa-times-circle"></i>
                                    </a>
                                </span>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Category Filters -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="glass-card p-3">
                <div class="d-flex flex-wrap gap-2 justify-content-center">
                    <a href="${pageContext.request.contextPath}/events" 
                       class="btn ${selectedCategory == null && searchTerm == null ? 'btn-primary' : 'btn-outline-primary'} rounded-pill px-4">
                        <i class="fas fa-th-large me-2"></i>All Events
                    </a>
                    <c:forEach var="category" items="${categories}">
                        <a href="${pageContext.request.contextPath}/events?category=${category}" 
                           class="btn ${selectedCategory == category ? 'btn-primary' : 'btn-outline-primary'} rounded-pill px-4">
                            <i class="fas fa-tag me-2"></i>${category}
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Results Info -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <span class="text-white-50">
                        <i class="fas fa-list-ul me-2"></i>
                        Showing <strong class="text-white">${events.size()}</strong> event${events.size() != 1 ? 's' : ''}
                        <c:if test="${searchTerm != null}">
                            matching "<strong class="text-white">${searchTerm}</strong>"
                        </c:if>
                        <c:if test="${selectedCategory != null}">
                            in <strong class="text-white">${selectedCategory}</strong>
                        </c:if>
                    </span>
                </div>
                <div>
                    <div class="btn-group">
                        <button class="btn btn-outline-primary btn-sm" onclick="toggleView('grid')">
                            <i class="fas fa-th"></i>
                        </button>
                        <button class="btn btn-outline-primary btn-sm" onclick="toggleView('list')">
                            <i class="fas fa-list"></i>
                        </button>
                    </div>
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
    <div class="row g-4" id="eventsContainer">
        <c:choose>
            <c:when test="${empty events}">
                <div class="col-12">
                    <div class="glass-card p-5 text-center empty-state">
                        <i class="fas fa-calendar-times fa-5x text-primary mb-4"></i>
                        <h3 class="text-white">No Events Found</h3>
                        <p class="text-white-50 mb-4">
                            <c:if test="${searchTerm != null}">
                                No events match your search for "<strong class="text-white">${searchTerm}</strong>"
                            </c:if>
                            <c:if test="${selectedCategory != null}">
                                No events in category "<strong class="text-white">${selectedCategory}</strong>"
                            </c:if>
                            <c:if test="${searchTerm == null && selectedCategory == null}">
                                There are currently no events available. Check back later!
                            </c:if>
                        </p>
                        <div class="d-flex gap-3 justify-content-center flex-wrap">
                            <a href="${pageContext.request.contextPath}/events" class="btn btn-primary">
                                <i class="fas fa-redo me-2"></i>Clear Filters
                            </a>
                            <c:if test="${sessionScope.user != null && sessionScope.user.role == 'admin'}">
                                <a href="${pageContext.request.contextPath}/admin/events?action=create" class="btn btn-success">
                                    <i class="fas fa-plus-circle me-2"></i>Create Event
                                </a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="event" items="${events}">
                    <div class="col-md-6 col-lg-4 event-item" data-view="grid">
                        <div class="event-card glass-card p-0 overflow-hidden h-100">
                            <!-- Event Image -->
                            <div class="event-image-wrapper position-relative">
                                <img src="${event.imageUrl != null ? event.imageUrl : 'https://via.placeholder.com/400x250/6C63FF/FFFFFF?text=Event'}" 
                                     alt="${event.title}" class="event-card-img w-100" style="height: 220px; object-fit: cover;">
                                <div class="event-overlay position-absolute top-0 start-0 w-100 h-100" 
                                     style="background: linear-gradient(180deg, transparent 50%, rgba(0,0,0,0.7));">
                                </div>
                                <div class="position-absolute top-0 start-0 p-3">
                                    <span class="badge bg-primary bg-opacity-75 px-3 py-2">
                                        <i class="fas fa-tag me-1"></i> ${event.category}
                                    </span>
                                </div>
                                <div class="position-absolute top-0 end-0 p-3">
                                    <span class="badge ${event.getAvailableSpots() > 0 ? 'bg-success bg-opacity-75' : 'bg-danger bg-opacity-75'} px-3 py-2">
                                        ${event.getAvailableSpots() > 0 ? 'Available' : 'Full'}
                                    </span>
                                </div>
                                <div class="position-absolute bottom-0 start-0 w-100 p-3">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="text-white">
                                            <i class="fas fa-calendar me-1"></i>
                                            <fmt:formatDate value="${event.eventDate}" pattern="MMM dd, yyyy"/>
                                        </div>
                                        <div class="text-white">
                                            <i class="fas fa-users me-1"></i>
                                            ${event.registeredCount}/${event.capacity}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Event Info -->
                            <div class="p-3">
                                <h5 class="text-white fw-bold mb-2 text-truncate">${event.title}</h5>
                                <div class="d-flex flex-wrap gap-2 mb-2">
                                    <span class="badge bg-secondary bg-opacity-25 text-white-50">
                                        <i class="fas fa-clock me-1"></i>
                                        <fmt:formatDate value="${event.eventTime}" pattern="hh:mm a"/>
                                    </span>
                                    <span class="badge bg-secondary bg-opacity-25 text-white-50">
                                        <i class="fas fa-map-marker-alt me-1"></i> ${event.location}
                                    </span>
                                </div>
                                <p class="text-white-50 small mb-3" style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                    ${event.description}
                                </p>
                                
                                <!-- Progress Bar -->
                                <div class="mb-3">
                                    <div class="d-flex justify-content-between text-white-50 small mb-1">
                                        <span>${event.registeredCount} registered</span>
                                        <span>${event.capacity} capacity</span>
                                    </div>
                                  
                                
                                <!-- Actions -->
                                <div class="d-flex gap-2">
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
                                            <c:if test="${!event.userRegistered && event.getAvailableSpots() > 0}">
                                                <form action="${pageContext.request.contextPath}/event-details" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="register">
                                                    <input type="hidden" name="eventId" value="${event.eventId}">
                                                    <button type="submit" class="btn btn-primary btn-sm">
                                                        <i class="fas fa-plus me-1"></i> Register
                                                    </button>
                                                </form>
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
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    
    <!-- Load More / Pagination (Optional) -->
    <c:if test="${events.size() > 9}">
        <div class="row mt-4">
            <div class="col-12 text-center">
                <button class="btn btn-outline-primary rounded-pill px-5" onclick="loadMoreEvents()">
                    <i class="fas fa-arrow-down me-2"></i>Load More Events
                </button>
            </div>
        </div>
    </c:if>
</div>

<script>
    // Toggle between grid and list view
    function toggleView(view) {
        const items = document.querySelectorAll('.event-item');
        const container = document.getElementById('eventsContainer');
        
        if (view === 'list') {
            container.className = 'row g-3';
            items.forEach(item => {
                item.className = 'col-12 event-item';
                item.dataset.view = 'list';
            });
        } else {
            container.className = 'row g-4';
            items.forEach(item => {
                item.className = 'col-md-6 col-lg-4 event-item';
                item.dataset.view = 'grid';
            });
        }
    }
    
    // Load more events (simulated)
    function loadMoreEvents() {
        const btn = event.target;
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span> Loading...';
        
        setTimeout(() => {
            btn.disabled = false;
            btn.innerHTML = '<i class="fas fa-arrow-down me-2"></i>Load More Events';
            showToast('All events loaded successfully!', 'success');
        }, 1500);
    }
    
    // Smooth animation for event cards
    document.addEventListener('DOMContentLoaded', function() {
        const cards = document.querySelectorAll('.event-card');
        cards.forEach((card, index) => {
            card.style.opacity = '0';
            card.style.transform = 'translateY(30px)';
            setTimeout(() => {
                card.style.transition = 'all 0.6s cubic-bezier(0.4, 0, 0.2, 1)';
                card.style.opacity = '1';
                card.style.transform = 'translateY(0)';
            }, 100 + (index * 50));
        });
    });
</script>

<style>
    .search-form .input-group-text {
        border-radius: 50px 0 0 50px;
        padding-left: 20px;
    }
    
    .search-form .form-control {
        border-radius: 0;
        padding: 12px 20px;
        background: rgba(255, 255, 255, 0.05);
        border-color: rgba(255, 255, 255, 0.1);
        color: white;
    }
    
    .search-form .form-control:focus {
        background: rgba(255, 255, 255, 0.08);
        border-color: var(--primary-color);
        box-shadow: none;
    }
    
    .search-form .btn {
        border-radius: 0 50px 50px 0;
        padding: 12px 30px;
    }
    
    .event-overlay {
        pointer-events: none;
    }
    
    .event-card {
        transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    }
    
    .event-card:hover {
        transform: translateY(-8px);
        box-shadow: 0 20px 60px rgba(108, 99, 255, 0.2);
    }
    
    .event-card .badge {
        font-weight: 500;
        letter-spacing: 0.3px;
    }
    
    .empty-state i {
        opacity: 0.5;
    }
    
    @media (max-width: 768px) {
        .search-form .input-group {
            flex-wrap: nowrap;
        }
        .search-form .form-control {
            font-size: 0.9rem;
            padding: 10px 16px;
        }
        .search-form .btn {
            padding: 10px 20px;
            font-size: 0.9rem;
        }
        .display-4 {
            font-size: 2rem;
        }
    }
</style>

<%@ include file="shared/footer.jsp" %>