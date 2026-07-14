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
    <title>Register - Campus Event Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
    <div class="container-fluid min-vh-100 d-flex align-items-center justify-content-center py-5">
        <div class="row w-100 justify-content-center">
            <div class="col-lg-6 col-md-8 col-11">
                <!-- Logo -->
                <div class="text-center mb-4">
                    <div class="auth-logo">
                        <i class="fas fa-calendar-alt fa-3x text-primary"></i>
                    </div>
                    <h2 class="fw-bold mt-3 text-white">Create Account</h2>
                    <p class="text-white-50">Join the campus event community</p>
                </div>
                
                <!-- Register Card -->
                <div class="card glass-card p-4 p-md-5">
                    <div class="card-body">
                        <!-- Error messages -->
                        <c:if test="${error != null}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm" novalidate>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="username" class="form-label fw-semibold">
                                        <i class="fas fa-user me-2"></i>Username *
                                    </label>
                                    <input type="text" class="form-control" id="username" name="username" 
                                           placeholder="Choose a username" value="${param.username}" required>
                                    <div class="invalid-feedback">Username is required (min 3 characters)</div>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="email" class="form-label fw-semibold">
                                        <i class="fas fa-envelope me-2"></i>Email *
                                    </label>
                                    <input type="email" class="form-control" id="email" name="email" 
                                           placeholder="your@email.com" value="${param.email}" required>
                                    <div class="invalid-feedback">Please enter a valid email</div>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="fullName" class="form-label fw-semibold">
                                    <i class="fas fa-id-card me-2"></i>Full Name *
                                </label>
                                <input type="text" class="form-control" id="fullName" name="fullName" 
                                       placeholder="Enter your full name" value="${param.fullName}" required>
                                <div class="invalid-feedback">Full name is required</div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="password" class="form-label fw-semibold">
                                        <i class="fas fa-lock me-2"></i>Password *
                                    </label>
                                    <div class="input-group">
                                        <input type="password" class="form-control" id="password" name="password" 
                                               placeholder="Min 6 characters" required>
                                        <button type="button" class="btn btn-outline-secondary" id="togglePassword1">
                                            <i class="fas fa-eye"></i>
                                        </button>
                                    </div>
                                    <div class="invalid-feedback">Password must be at least 6 characters</div>
                                    <div class="form-text text-white-50">
                                        <i class="fas fa-info-circle"></i> Must be at least 6 characters
                                    </div>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="confirmPassword" class="form-label fw-semibold">
                                        <i class="fas fa-check-double me-2"></i>Confirm Password *
                                    </label>
                                    <div class="input-group">
                                        <input type="password" class="form-control" id="confirmPassword" 
                                               name="confirmPassword" placeholder="Confirm password" required>
                                        <button type="button" class="btn btn-outline-secondary" id="togglePassword2">
                                            <i class="fas fa-eye"></i>
                                        </button>
                                    </div>
                                    <div class="invalid-feedback">Passwords must match</div>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="phone" class="form-label fw-semibold">
                                        <i class="fas fa-phone me-2"></i>Phone
                                    </label>
                                    <input type="tel" class="form-control" id="phone" name="phone" 
                                           placeholder="Enter phone number" value="${param.phone}">
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="department" class="form-label fw-semibold">
                                        <i class="fas fa-building me-2"></i>Department
                                    </label>
                                    <input type="text" class="form-control" id="department" name="department" 
                                           placeholder="Your department" value="${param.department}">
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="studentId" class="form-label fw-semibold">
                                    <i class="fas fa-id-badge me-2"></i>Student ID
                                </label>
                                <input type="text" class="form-control" id="studentId" name="studentId" 
                                       placeholder="Enter your student ID" value="${param.studentId}">
                            </div>
                            
                            <div class="mb-3 form-text text-white-50">
                                <i class="fas fa-info-circle"></i> Fields with * are required
                            </div>
                            
                            <button type="submit" class="btn btn-primary btn-lg w-100 mb-3" id="registerBtn">
                                <span class="spinner-border spinner-border-sm d-none me-2" id="registerSpinner"></span>
                                <i class="fas fa-user-plus me-2"></i> Create Account
                            </button>
                            
                            <div class="text-center">
                                <p class="text-white-50">
                                    Already have an account? 
                                    <a href="${pageContext.request.contextPath}/login" class="text-decoration-none fw-semibold text-primary">
                                        Sign In
                                    </a>
                                </p>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Toggle password visibility
        document.getElementById('togglePassword1').addEventListener('click', function() {
            const password = document.getElementById('password');
            const icon = this.querySelector('i');
            if (password.type === 'password') {
                password.type = 'text';
                icon.classList.replace('fa-eye', 'fa-eye-slash');
            } else {
                password.type = 'password';
                icon.classList.replace('fa-eye-slash', 'fa-eye');
            }
        });
        
        document.getElementById('togglePassword2').addEventListener('click', function() {
            const password = document.getElementById('confirmPassword');
            const icon = this.querySelector('i');
            if (password.type === 'password') {
                password.type = 'text';
                icon.classList.replace('fa-eye', 'fa-eye-slash');
            } else {
                password.type = 'password';
                icon.classList.replace('fa-eye-slash', 'fa-eye');
            }
        });
        
        // Form validation
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            const username = document.getElementById('username');
            const email = document.getElementById('email');
            const fullName = document.getElementById('fullName');
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            let isValid = true;
            
            // Username validation
            if (!username.value.trim() || username.value.trim().length < 3) {
                username.classList.add('is-invalid');
                isValid = false;
            } else {
                username.classList.remove('is-invalid');
            }
            
            // Email validation
            const emailPattern = /^[A-Za-z0-9+_.-]+@(.+)$/;
            if (!email.value.trim() || !emailPattern.test(email.value.trim())) {
                email.classList.add('is-invalid');
                isValid = false;
            } else {
                email.classList.remove('is-invalid');
            }
            
            // Full name validation
            if (!fullName.value.trim()) {
                fullName.classList.add('is-invalid');
                isValid = false;
            } else {
                fullName.classList.remove('is-invalid');
            }
            
            // Password validation
            if (!password.value || password.value.length < 6) {
                password.classList.add('is-invalid');
                isValid = false;
            } else {
                password.classList.remove('is-invalid');
            }
            
            // Confirm password validation
            if (!confirmPassword.value || confirmPassword.value !== password.value) {
                confirmPassword.classList.add('is-invalid');
                isValid = false;
            } else {
                confirmPassword.classList.remove('is-invalid');
            }
            
            if (!isValid) {
                e.preventDefault();
                return;
            }
            
            // Show loading state
            const btn = document.getElementById('registerBtn');
            const spinner = document.getElementById('registerSpinner');
            btn.disabled = true;
            spinner.classList.remove('d-none');
            btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span> Creating account...';
        });
    </script>
</body>
</html>