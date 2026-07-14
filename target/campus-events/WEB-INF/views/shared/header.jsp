<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Campus Event Management</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome 6 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark glass-navbar fixed-top">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <i class="fas fa-calendar-alt me-2"></i>
                <span class="fw-bold">CampusEvents</span>
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" 
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link ${pageContext.request.requestURI.contains('events') ? 'active' : ''}" 
                           href="${pageContext.request.contextPath}/events">
                            <i class="fas fa-list me-1"></i> Events
                        </a>
                    </li>
                    
                    <c:if test="${sessionScope.user != null}">
                        <c:if test="${sessionScope.user.role == 'student'}">
                            <li class="nav-item">
                                <a class="nav-link ${pageContext.request.requestURI.contains('my-events') ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/student/my-events">
                                    <i class="fas fa-ticket me-1"></i> My Events
                                </a>
                            </li>
                        </c:if>
                        
                        <c:if test="${sessionScope.user.role == 'admin'}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="adminDropdown" role="button" 
                                   data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="fas fa-cog me-1"></i> Admin
                                </a>
                                <ul class="dropdown-menu" aria-labelledby="adminDropdown">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard">
                                        <i class="fas fa-chart-pie me-2"></i> Dashboard
                                    </a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/events">
                                        <i class="fas fa-calendar-plus me-2"></i> Manage Events
                                    </a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/users">
                                        <i class="fas fa-users me-2"></i> Users
                                    </a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/registrations">
                                        <i class="fas fa-clipboard-list me-2"></i> Registrations
                                    </a></li>
                                </ul>
                            </li>
                        </c:if>
                    </c:if>
                </ul>
                
                <ul class="navbar-nav">
                    <c:if test="${sessionScope.user != null}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" 
                               data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="fas fa-user-circle me-1"></i>
                                <span class="d-none d-md-inline">${sessionScope.user.fullName}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                <li><span class="dropdown-item-text text-muted">
                                    <i class="fas fa-user me-2"></i> ${sessionScope.user.username}
                                </span></li>
                                <li><hr class="dropdown-divider"></li>
                                
                                <c:if test="${sessionScope.user.role == 'student'}">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/student/profile">
                                        <i class="fas fa-id-card me-2"></i> Profile
                                    </a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/student/dashboard">
                                        <i class="fas fa-home me-2"></i> Dashboard
                                    </a></li>
                                </c:if>
                                
                                <c:if test="${sessionScope.user.role == 'admin'}">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard">
                                        <i class="fas fa-chart-pie me-2"></i> Dashboard
                                    </a></li>
                                </c:if>
                                
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                    <i class="fas fa-sign-out-alt me-2"></i> Logout
                                </a></li>
                            </ul>
                        </li>
                    </c:if>
                    
                    <c:if test="${sessionScope.user == null}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                <i class="fas fa-sign-in-alt me-1"></i> Login
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link btn btn-outline-light btn-sm ms-2 px-3" href="${pageContext.request.contextPath}/register">
                                <i class="fas fa-user-plus me-1"></i> Register
                            </a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </nav>
    
    <!-- Main Content Wrapper -->
    <div class="main-wrapper">