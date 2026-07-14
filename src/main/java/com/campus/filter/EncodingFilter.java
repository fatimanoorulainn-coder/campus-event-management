package com.campus.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to set character encoding for all requests and responses.
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {
    
    private static final String ENCODING = "UTF-8";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization not needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        // Set request encoding
        req.setCharacterEncoding(ENCODING);
        
        // Set response encoding and content type
        res.setCharacterEncoding(ENCODING);
        res.setContentType("text/html; charset=" + ENCODING);
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup not needed
    }
}