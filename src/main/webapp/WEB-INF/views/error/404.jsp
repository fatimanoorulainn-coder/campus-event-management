<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../shared/header.jsp" %>

<div class="container mt-5 pt-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="glass-card p-5 text-center">
                <div class="mb-4">
                    <i class="fas fa-search text-primary" style="font-size: 6rem;"></i>
                </div>
                <h1 class="text-white display-1 fw-bold">404</h1>
                <h2 class="text-white mb-3">Page Not Found</h2>
                <p class="text-white-50 mb-4">
                    The page you are looking for might have been removed, 
                    had its name changed, or is temporarily unavailable.
                </p>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                    <i class="fas fa-home me-2"></i>Go Home
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="../shared/footer.jsp" %>