package com.example.demo.services;

import com.example.demo.models.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class APIService {

  protected final JWTService jwtService;

  public APIService(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  public String updateTokenExpiration(HttpServletRequest request) {
    String token = this.jwtService.getTokenFromRequest(request);
    UserPrincipal userPrincipal =
        (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return this.jwtService.updateTokenExpiration(userPrincipal.getUsername(), token);
  }
}
