<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../shared/header.jsp" %>

<div class="container mt-5 pt-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="glass-card p-5 text-center">
                <div class="mb-4">
                    <i class="fas fa-exclamation-triangle text-warning" style="font-size: 6rem;"></i>
                </div>
                <h1 class="text-white display-1 fw-bold">500</h1>
                <h2 class="text-white mb-3">Server Error</h2>
                <p class="text-white-50 mb-4">
                    Something went wrong on our end. Please try again later 
                    or contact support if the problem persists.
                </p>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                    <i class="fas fa-home me-2"></i>Go Home
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="../shared/footer.jsp" %>