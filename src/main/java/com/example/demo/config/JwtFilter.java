package com.example.demo.config;

import com.example.demo.services.JWTService;
import com.example.demo.services.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private ApplicationContext applicationContext;

  public JwtFilter(JWTService jwtService, ApplicationContext applicationContext) {
    this.jwtService = jwtService;
    this.applicationContext = applicationContext;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // Bearer spdsapdspodapoasds  example
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String username = null;
    try {
      if (authHeader != null && authHeader.startsWith("Bearer")) {
        token = authHeader.substring(7);
        username = jwtService.extractUsername(token);
      }
      if (username != null
          && SecurityContextHolder.getContext().getAuthentication()
              == null) { // so if there is a username on the token and the request is not
                         // authenticated
        UserDetails userDetails =
            applicationContext
                .getBean(MyUserDetailsService.class)
                .loadUserByUsername(username); // here we will grab a user details object
        if (jwtService.validateToken(
            token,
            userDetails)) { // basically check if the token is valid, and the user the token refers
          // too still exists
          UsernamePasswordAuthenticationToken
              authToken = // this is the in memory application of our token
              new UsernamePasswordAuthenticationToken(
                      userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext()
              .setAuthentication(authToken); // so this request is now authenticated
        }
      }
    } catch (Exception e) {
      if (e.getClass().getSimpleName().equals("ExpiredJwtException")) {
        jwtService.revokeToken(token);
        response.sendError(401, "Token Expired");
      }
    }
    filterChain.doFilter(request, response);
  }
}
