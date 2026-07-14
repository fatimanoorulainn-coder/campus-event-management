<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../shared/header.jsp" %>

<div class="container-fluid mt-5 pt-4">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-lg-2 d-none d-lg-block">
            <div class="glass-card p-3 sticky-top" style="top: 80px;">
                <div class="text-center mb-4">
                    <div class="profile-avatar-wrapper mb-2">
                        <div class="profile-avatar" style="width: 60px; height: 60px;">
                            <i class="fas fa-user-shield fa-2x text-white"></i>
                        </div>
                    </div>
                    <h6 class="text-white">${sessionScope.user.fullName}</h6>
                    <span class="badge bg-primary">Administrator</span>
                </div>
                <hr class="border-secondary">
                <nav class="nav flex-column gap-1">
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/dashboard">
                        <i class="fas fa-chart-pie me-2"></i> Dashboard
                    </a>
                    <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/events">
                        <i class="fas fa-calendar-plus me-2"></i> Events
                    </a>
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/users">
                        <i class="fas fa-users me-2"></i> Users
                    </a>
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/registrations">
                        <i class="fas fa-clipboard-list me-2"></i> Registrations
                    </a>
                </nav>
            </div>
        </div>
        
        <!-- Main Content -->
        <div class="col-lg-10">
            <div class="glass-card p-4 mb-4">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h2 class="text-white">
                            <i class="fas fa-edit me-2 text-warning"></i>Edit Event
                        </h2>
                        <p class="text-white-50 mb-0">Update event details</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/admin/events" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Back to Events
                    </a>
                </div>
            </div>
            
            <c:if test="${event != null}">
                <div class="glass-card p-4">
                    <form action="${pageContext.request.contextPath}/admin/events" method="post" id="eventForm" novalidate>
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="eventId" value="${event.eventId}">
                        
                        <div class="row">
                            <div class="col-md-8 mb-3">
                                <label for="title" class="form-label text-white">Event Title *</label>
                                <input type="text" class="form-control" id="title" name="title" 
                                       value="${event.title}" required>
                                <div class="invalid-feedback">Event title is required</div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="category" class="form-label text-white">Category *</label>
                                <select class="form-select" id="category" name="category" required>
                                    <option value="">Select Category</option>
                                    <c:forEach var="cat" items="${categories}">
                                        <option value="${cat}" ${cat == event.category ? 'selected' : ''}>${cat}</option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Please select a category</div>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label text-white">Description *</label>
                            <textarea class="form-control" id="description" name="description" rows="4" required>${event.description}</textarea>
                            <div class="invalid-feedback">Description is required</div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="eventDate" class="form-label text-white">Event Date *</label>
                                <input type="date" class="form-control" id="eventDate" name="eventDate" 
                                       value="${event.eventDate}" required>
                                <div class="invalid-feedback">Event date is required</div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="eventTime" class="form-label text-white">Event Time *</label>
                                <input type="time" class="form-control" id="eventTime" name="eventTime" 
                                       value="${event.eventTime}" required>
                                <div class="invalid-feedback">Event time is required</div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="capacity" class="form-label text-white">Capacity *</label>
                                <input type="number" class="form-control" id="capacity" name="capacity" 
                                       min="1" max="1000" value="${event.capacity}" required>
                                <div class="invalid-feedback">Please enter a valid capacity (min 1)</div>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="location" class="form-label text-white">Location *</label>
                            <input type="text" class="form-control" id="location" name="location" 
                                   value="${event.location}" required>
                            <div class="invalid-feedback">Location is required</div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="organizer" class="form-label text-white">Organizer</label>
                                <input type="text" class="form-control" id="organizer" name="organizer" 
                                       value="${event.organizer}">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="contactEmail" class="form-label text-white">Contact Email</label>
                                <input type="email" class="form-control" id="contactEmail" name="contactEmail" 
                                       value="${event.contactEmail}">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="contactPhone" class="form-label text-white">Contact Phone</label>
                                <input type="tel" class="form-control" id="contactPhone" name="contactPhone" 
                                       value="${event.contactPhone}">
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="imageUrl" class="form-label text-white">Image URL</label>
                            <input type="url" class="form-control" id="imageUrl" name="imageUrl" 
                                   value="${event.imageUrl}" placeholder="https://example.com/image.jpg">
                            <div class="form-text text-white-50">
                                <i class="fas fa-info-circle me-1"></i> Leave empty to use default image
                            </div>
                        </div>
                        
                        <div class="d-flex gap-2 mt-4">
                            <button type="submit" class="btn btn-warning btn-lg">
                                <i class="fas fa-save me-2"></i>Update Event
                            </button>
                            <a href="${pageContext.request.contextPath}/admin/events" class="btn btn-secondary btn-lg">
                                <i class="fas fa-times me-2"></i>Cancel
                            </a>
                        </div>
                    </form>
                </div>
            </c:if>
            
            <c:if test="${event == null}">
                <div class="glass-card p-5 text-center">
                    <i class="fas fa-exclamation-triangle fa-5x text-warning mb-4"></i>
                    <h3 class="text-white">Event Not Found</h3>
                    <p class="text-white-50">The event you're trying to edit doesn't exist.</p>
                    <a href="${pageContext.request.contextPath}/admin/events" class="btn btn-primary mt-3">
                        <i class="fas fa-arrow-left me-2"></i>Back to Events
                    </a>
                </div>
            </c:if>
        </div>
    </div>
</div>

<script>
    document.getElementById('eventForm')?.addEventListener('submit', function(e) {
        const inputs = this.querySelectorAll('input[required], select[required], textarea[required]');
        let isValid = true;
        
        inputs.forEach(input => {
            if (!input.value.trim()) {
                input.classList.add('is-invalid');
                isValid = false;
            } else {
                input.classList.remove('is-invalid');
            }
        });
        
        if (!isValid) {
            e.preventDefault();
        }
    });
</script>

<%@ include file="../shared/footer.jsp" %>