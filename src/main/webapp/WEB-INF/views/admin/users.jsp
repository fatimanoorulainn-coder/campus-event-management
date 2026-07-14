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
                    <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/users">
                        <i class="fas fa-users me-2"></i> Users                    </a>
                    <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/registrations">
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
                        <i class="fas fa-users me-2 text-primary"></i>Users
                    </h2>
                    <p class="text-white-50 mb-0">Manage student accounts and registrations</p>
                </div>
            </div>
            
            <!-- Search -->
            <div class="glass-card p-4 mb-4">
                <form action="${pageContext.request.contextPath}/admin/users" method="get" class="d-flex gap-2">
                    <input type="text" name="search" class="form-control" 
                           placeholder="Search by name, email, or username..." 
                           value="${searchTerm}">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-search me-2"></i>Search
                    </button>
                    <c:if test="${searchTerm != null}">
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">
                            <i class="fas fa-times me-2"></i>Clear
                        </a>
                    </c:if>
                </form>
            </div>
            
            <!-- Users Table -->
            <div class="glass-card p-4">
                <div class="table-responsive">
                    <table class="table table-dark table-hover">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Full Name</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Department</th>
                                <th>Student ID</th>
                                <th>Joined</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty students}">
                                    <tr>
                                        <td colspan="7" class="text-center text-white-50 py-4">
                                            <i class="fas fa-users fa-2x d-block mb-2"></i>
                                            <c:choose>
                                                <c:when test="${searchTerm != null}">
                                                    No users found matching "<strong>${searchTerm}</strong>"
                                                </c:when>
                                                <c:otherwise>
                                                    No users registered yet
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="student" items="${students}">
                                        <tr>
                                            <td>#${student.userId}</td>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <div class="user-avatar-sm me-2">
                                                        <i class="fas fa-user-circle fa-2x text-primary"></i>
                                                    </div>
                                                    ${student.fullName}
                                                </div>
                                            </td>
                                            <td>${student.username}</td>
                                            <td>${student.email}</td>
                                            <td>${student.department != null ? student.department : '-'}</td>
                                            <td>${student.studentId != null ? student.studentId : '-'}</td>
                                            <td>
                                                <fmt:formatDate value="${student.createdAt}" pattern="MMM dd, yyyy"/>
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
                    Showing ${students.size()} student${students.size() != 1 ? 's' : ''}
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../shared/footer.jsp" %>