package com.nsu.danilllo.security;

import com.nsu.danilllo.model.Role;
import com.nsu.danilllo.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Integer expirationTime;
    @Value("${jwt.authorizationHeader}")
    private String authorizationHeader;

    private UserDetailsService userDetailsService;

    @Autowired
    public JwtProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String username, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", roles.toArray(new Role[0])[0].getAuthority());
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(validity)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        }
        catch (JwtException | IllegalArgumentException exception) {
            throw new JwtAuthenticationException("Jwt token expired or invalid", HttpStatus.UNAUTHORIZED);
        }
    }


    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }

    public String resolveToken(HttpServletRequest request) {
        String token =  request.getHeader(authorizationHeader);
        return token;
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}