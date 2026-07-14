<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                    <h6 class="text-white">${user.fullName}</h6>
                    <span class="badge bg-primary">Administrator</span>
                </div>
                <hr class="border-secondary">
                <nav class="nav flex-column gap-1">
                    <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/dashboard">
                        <i class="fas fa-chart-pie me-2"></i> Dashboard
                    </a>
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/events">
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
                <h2 class="text-white">
                    <i class="fas fa-chart-pie me-2 text-primary"></i>Admin Dashboard
                </h2>
                <p class="text-white-50">Welcome back, ${user.fullName}! Here's your overview.</p>
            </div>
            
            <!-- Stats Cards -->
            <div class="row g-4 mb-4">
                <div class="col-md-3 col-6">
                    <div class="glass-card p-4 text-center stat-card">
                        <div class="stat-icon bg-primary-gradient">
                            <i class="fas fa-calendar-alt fa-2x"></i>
                        </div>
                        <h3 class="text-white mt-3">${totalEventsFormatted}</h3>
                        <p class="text-white-50 mb-0">Total Events</p>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="glass-card p-4 text-center stat-card">
                        <div class="stat-icon bg-success-gradient">
                            <i class="fas fa-ticket-alt fa-2x"></i>
                        </div>
                        <h3 class="text-white mt-3">${totalRegistrationsFormatted}</h3>
                        <p class="text-white-50 mb-0">Registrations</p>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="glass-card p-4 text-center stat-card">
                        <div class="stat-icon bg-warning-gradient">
                            <i class="fas fa-users fa-2x"></i>
                        </div>
                        <h3 class="text-white mt-3">${totalUsersFormatted}</h3>
                        <p class="text-white-50 mb-0">Total Users</p>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="glass-card p-4 text-center stat-card">
                        <div class="stat-icon bg-info-gradient">
                            <i class="fas fa-user-graduate fa-2x"></i>
                        </div>
                        <h3 class="text-white mt-3">${totalStudentsFormatted}</h3>
                        <p class="text-white-50 mb-0">Students</p>
                    </div>
                </div>
            </div>
            
            <!-- Quick Actions -->
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="glass-card p-4 text-center">
                        <i class="fas fa-plus-circle fa-3x text-primary mb-3"></i>
                        <h5 class="text-white">Create Event</h5>
                        <p class="text-white-50 small">Add a new event to the system</p>
                        <a href="${pageContext.request.contextPath}/admin/events?action=create" 
                           class="btn btn-primary w-100">
                            <i class="fas fa-plus me-2"></i>Create
                        </a>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="glass-card p-4 text-center">
                        <i class="fas fa-list fa-3x text-success mb-3"></i>
                        <h5 class="text-white">Manage Events</h5>
                        <p class="text-white-50 small">View and update existing events</p>
                        <a href="${pageContext.request.contextPath}/admin/events" 
                           class="btn btn-success w-100">
                            <i class="fas fa-edit me-2"></i>Manage
                        </a>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="glass-card p-4 text-center">
                        <i class="fas fa-users fa-3x text-warning mb-3"></i>
                        <h5 class="text-white">View Users</h5>
                        <p class="text-white-50 small">Manage student accounts</p>
                        <a href="${pageContext.request.contextPath}/admin/users" 
                           class="btn btn-warning w-100">
                            <i class="fas fa-user me-2"></i>View
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../shared/footer.jsp" %>