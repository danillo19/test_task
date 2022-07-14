package com.nsu.danilllo.security;

import com.nsu.danilllo.exceptions.JwtAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Component
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("do filter...");
        String token = jwtProvider.resolveToken((HttpServletRequest) servletRequest);

        try {
            if (token != null && jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                if (authentication != null) {
                    authentication.getPrincipal();
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch (JwtAuthenticationException exception) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse)servletResponse).sendError(exception.getStatus().value());
            throw new JwtAuthenticationException("Jwt token is expired or invalid");
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
