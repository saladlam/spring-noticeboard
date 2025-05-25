package info.saladlam.example.spring.noticeboard.framework;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class CspNonceFilter extends OncePerRequestFilter {

    public static final String ATTRIBUTE_NAME = CspNonceFilter.class.getName() + ".NONCE";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String nonce = UUID.randomUUID().toString().replace("-", "");
        request.setAttribute(ATTRIBUTE_NAME, nonce);
        filterChain.doFilter(request, response);
    }

}
