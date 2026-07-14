<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../shared/header.jsp" %>

<div class="container mt-5 pt-4">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-lg-3 mb-4">
            <div class="glass-card p-4 text-center">
                <div class="profile-avatar-wrapper mb-3">
                    <div class="profile-avatar">
                        <i class="fas fa-user fa-4x text-white"></i>
                    </div>
                </div>
                <h5 class="text-white">${user.fullName}</h5>
                <p class="text-white-50 small">@${user.username}</p>
                <div class="d-flex justify-content-center gap-2">
                    <span class="badge bg-primary">${user.role}</span>
                    <span class="badge bg-success">Active</span>
                </div>
                <hr class="border-secondary">
                <div class="text-start">
                    <p class="text-white-50 small mb-1">
                        <i class="fas fa-envelope me-2"></i> ${user.email}
                    </p>
                    <p class="text-white-50 small mb-1">
                        <i class="fas fa-phone me-2"></i> ${user.phone != null && !user.phone.isEmpty() ? user.phone : 'Not provided'}
                    </p>
                    <p class="text-white-50 small mb-1">
                        <i class="fas fa-building me-2"></i> ${user.department != null && !user.department.isEmpty() ? user.department : 'Not provided'}
                    </p>
                    <p class="text-white-50 small">
                        <i class="fas fa-id-badge me-2"></i> ${user.studentId != null && !user.studentId.isEmpty() ? user.studentId : 'Not provided'}
                    </p>
                </div>
            </div>
        </div>
        
        <!-- Main Content -->
        <div class="col-lg-9">
            <!-- Success/Error Messages -->
            <c:if test="${success != null}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i> ${success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${error != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            
            <!-- Update Profile -->
            <div class="glass-card p-4 mb-4">
                <h4 class="text-white mb-4">
                    <i class="fas fa-user-edit me-2 text-primary"></i>Update Profile
                </h4>
                <form action="${pageContext.request.contextPath}/student/profile" method="post">
                    <input type="hidden" name="action" value="updateProfile">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="fullName" class="form-label text-white">Full Name *</label>
                            <input type="text" class="form-control" id="fullName" name="fullName" 
                                   value="${user.fullName}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="phone" class="form-label text-white">Phone</label>
                            <input type="tel" class="form-control" id="phone" name="phone" 
                                   value="${user.phone}">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="department" class="form-label text-white">Department</label>
                            <input type="text" class="form-control" id="department" name="department" 
                                   value="${user.department}">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="studentId" class="form-label text-white">Student ID</label>
                            <input type="text" class="form-control" id="studentId" name="studentId" 
                                   value="${user.studentId}">
                        </div>
                    </div>
                    <div class="text-end">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save me-2"></i>Update Profile
                        </button>
                    </div>
                </form>
            </div>
            
            <!-- Change Password -->
            <div class="glass-card p-4">
                <h4 class="text-white mb-4">
                    <i class="fas fa-key me-2 text-warning"></i>Change Password
                </h4>
                <form action="${pageContext.request.contextPath}/student/profile" method="post">
                    <input type="hidden" name="action" value="updatePassword">
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label for="currentPassword" class="form-label text-white">Current Password *</label>
                            <input type="password" class="form-control" id="currentPassword" 
                                   name="currentPassword" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="newPassword" class="form-label text-white">New Password *</label>
                            <input type="password" class="form-control" id="newPassword" 
                                   name="newPassword" required minlength="6">
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="confirmPassword" class="form-label text-white">Confirm Password *</label>
                            <input type="password" class="form-control" id="confirmPassword" 
                                   name="confirmPassword" required>
                        </div>
                    </div>
                    <div class="text-end">
                        <button type="submit" class="btn btn-warning">
                            <i class="fas fa-lock me-2"></i>Change Password
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="../shared/footer.jsp" %>