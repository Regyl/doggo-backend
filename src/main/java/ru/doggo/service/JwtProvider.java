package ru.doggo.service;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtProvider {

    private static final String BEARER = "Bearer";

    @Getter
    private final long tokenLifetime;
    private final long refreshTokenLifetime;
    private final Key key;
    private final JwtParser parser;
    private final UserDetailsService userDetailsService;

    public JwtProvider(@Value("${jwt.key}") String jwtSecret,
                       @Value("${jwt.token-lifetime}") Long tokenLifetime, //in hours
                       @Value("${jwt.refresh.token-lifetime}") Long refreshTokenLifetime, //in hours
                       UserDetailsService userDetailsService) {

        this.tokenLifetime = tokenLifetime;
        this.refreshTokenLifetime = refreshTokenLifetime;

        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.parser = Jwts.parserBuilder().setSigningKey(this.key).build();
        this.userDetailsService = userDetailsService;
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenLifetime);
    }

    public String generateAccessToken(String username) {
        return generateToken(username, tokenLifetime);
    }

    @SneakyThrows
    public void validateToken(String token) {
        parser.parseClaimsJws(token);
    }

    public Optional<String> getToken(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearer != null && bearer.startsWith(BEARER)) {
            return Optional.of(bearer.substring(BEARER.length()));
        } else {
            return Optional.empty();
        }
    }

    public Authentication getAuthentication(String token) {;
        UserDetails userDetails = userDetailsService.loadUserByUsername(token);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getLoginFromToken(String token) {
        return parser
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String generateToken(String username, long expiration) {
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        LocalDateTime expireAt = now.plusHours(expiration);
        Date expirationDate = Date.from(expireAt.toInstant(ZoneOffset.UTC));

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
}
