<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="shared/header.jsp" %>

<div class="container mt-5 pt-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="glass-card p-5 text-center">
                <div class="mb-4">
                    <i class="fas fa-ban text-danger" style="font-size: 6rem;"></i>
                </div>
                <h1 class="text-white display-4 fw-bold">Access Denied</h1>
                <div class="my-4">
                    <div class="alert alert-danger">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        You do not have permission to access this page.
                    </div>
                </div>
                <p class="text-white-50 mb-4">
                    This area is restricted to authorized users only. 
                    Please contact the administrator if you believe this is an error.
                </p>
                <div class="d-flex flex-wrap justify-content-center gap-3">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                        <i class="fas fa-home me-2"></i>Go Home
                    </a>
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <c:choose>
                                <c:when test="${sessionScope.user.role == 'admin'}">
                                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-primary">
                                        <i class="fas fa-chart-pie me-2"></i>Dashboard
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/student/dashboard" class="btn btn-outline-primary">
                                        <i class="fas fa-home me-2"></i>Dashboard
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary">
                                <i class="fas fa-sign-in-alt me-2"></i>Login
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="shared/footer.jsp" %>