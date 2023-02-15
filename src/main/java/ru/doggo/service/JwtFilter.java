package ru.doggo.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest,
                                    HttpServletResponse servletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        jwtProvider.getToken(servletRequest).ifPresent(token -> {
            jwtProvider.validateToken(token);
            String login = jwtProvider.getLoginFromToken(token);
            Authentication auth = jwtProvider.getAuthentication(login);
            SecurityContextHolder.getContext().setAuthentication(auth);
        });
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
