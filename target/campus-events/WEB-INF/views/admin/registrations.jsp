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
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/events">
                        <i class="fas fa-calendar-plus me-2"></i> Events
                    </a>
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/users">
                        <i class="fas fa-users me-2"></i> Users
                    </a>
                    <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/registrations">
                        <i class="fas fa-clipboard-list me-2"></i> Registrations
                    </a>
                </nav>
            </div>
        </div>
        
        <!-- Main Content -->
        <div class="col-lg-10">
            <div class="glass-card p-4 mb-4">
                <div>
                    <h2 class="text-white">
                        <i class="fas fa-clipboard-list me-2 text-primary"></i>All Registrations
                    </h2>
                    <p class="text-white-50 mb-0">View all event registrations across the system</p>
                </div>
            </div>
            
            <!-- Registrations Table -->
            <div class="glass-card p-4">
                <div class="table-responsive">
                    <table class="table table-dark table-hover">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Student</th>
                                <th>Event</th>
                                <th>Event Date</th>
                                <th>Location</th>
                                <th>Status</th>
                                <th>Registered On</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty registrations}">
                                    <tr>
                                        <td colspan="7" class="text-center text-white-50 py-4">
                                            <i class="fas fa-clipboard-list fa-2x d-block mb-2"></i>
                                            No registrations found
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="reg" items="${registrations}">
                                        <tr>
                                            <td>#${reg.registrationId}</td>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <i class="fas fa-user-circle fa-2x text-primary me-2"></i>
                                                    <div>
                                                        <div class="text-white">${reg.userName}</div>
                                                        <div class="text-white-50 small">ID: ${reg.userId}</div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/event-details?id=${reg.eventId}" 
                                                   class="text-white text-decoration-none">
                                                    ${reg.eventTitle}
                                                </a>
                                            </td>
                                            <td>
                                                <c:if test="${reg.eventDate != null}">
                                                    <fmt:formatDate value="${reg.eventDate}" pattern="MMM dd, yyyy"/>
                                                </c:if>
                                            </td>
                                            <td>${reg.eventLocation}</td>
                                            <td>
                                                <span class="badge ${reg.status == 'registered' ? 'bg-success' : 'bg-danger'}">
                                                    ${reg.status}
                                                </span>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${reg.registrationDate}" pattern="MMM dd, yyyy hh:mm a"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
                
                <div class="text-white-50 small mt-3">
                    <i class="fas fa-info-circle me-1"></i> 
                    Total registrations: ${registrations.size()}
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../shared/footer.jsp" %>