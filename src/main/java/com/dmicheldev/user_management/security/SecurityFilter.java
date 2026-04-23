package com.dmicheldev.user_management.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final int TOKEN_START_INDEX = 7;
    private static final String HEADER_AUTHORIZATION  = "Authorization";

    //injetar o tokenservice
    private final TokenService tokenService;
    public SecurityFilter(TokenService tokenService){
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException{

        String token = recoverToken(request);
        if(token != null){
            DecodedJWT decodedJWT = tokenService.validateToken(token);

            if(decodedJWT != null){
                String email = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                var authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        String header = request.getHeader(HEADER_AUTHORIZATION);
        if(header == null || header.isBlank() || !header.startsWith("Bearer ")){
            return null;
        }
        return header.substring(TOKEN_START_INDEX);
    }
}
