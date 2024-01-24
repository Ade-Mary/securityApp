package com.example.SecurityApp.config;

import com.example.SecurityApp.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Configuration
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
       //Extract header
       final  String authorizationHeader = request.getHeader("Authorization");
       final String jwtToken;
       final String username;

       if (authorizationHeader == null || authorizationHeader.startsWith("Bearer")){
           filterChain.doFilter(request,response);
           return;
       }

       jwtToken = authorizationHeader.substring(7);

       username = jwtService.extractUsername(jwtToken);
       if (username != null && SecurityContextHolder.getContext() == null){
           UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

           if (jwtService.isTokenValid(jwtToken,userDetails)){
               UsernamePasswordAuthenticationToken authenticationToken =
                       new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

               authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

               SecurityContextHolder.getContext().setAuthentication(authenticationToken);

               filterChain.doFilter(request,response);
           }
       }
    }
}
