package com.example.demo.services;

import com.example.demo.models.AuthTokenModel;
import com.example.demo.repos.AuthTokenModelRepo;
import com.example.demo.repos.UserModelRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Data
public class JWTService {

  private String secretKey;
  private final AuthTokenModelRepo tokenRepo;

  public JWTService(AuthTokenModelRepo tokenRepo) throws NoSuchAlgorithmException {
    KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
    SecretKey sk = keyGen.generateKey();
    secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    this.tokenRepo = tokenRepo;
  }

  public String generateToken(String email) {
    Map<String, Object> claims =
        new HashMap<>(); // so each entry in the map is a claim that describes the owner of the
                         // token
    Date expirationTime = new Date(System.currentTimeMillis() + (1000 * 60));
    String token =
        Jwts.builder()
            .claims()
            .add(claims)
            .subject(email)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(expirationTime)
            .and()
            .signWith(this.getSigningKey())
            .compact();
    tokenRepo.save(AuthTokenModel.builder().token(token).email(email).expirationTime(expirationTime).build());
    return token;
  }

  private SecretKey getSigningKey() {
    byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
    return Keys.hmacShaKeyFor(secretKeyBytes);
  }

  public String extractEmail(String token) {
    Claims claims = extractAllClaims(token);
    return claims.getSubject();
  }

  public Date extractExpiration(String token) {
    Claims claims = extractAllClaims(token);
    return claims.getExpiration();
  }

  @Transactional
  public boolean validateToken(String token, UserDetails userDetails) {
    if (tokenRepo.findByToken(token) == null) {
      return false;
    }
    if (!userDetails.getUsername().equals(extractEmail(token))) {
      return false;
    }
    if (extractExpiration(token).before(new Date())) {
        revokeToken(token);
        return false;
    }
    return true;
  }

  public String getTokenFromRequest(HttpServletRequest request){
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      return token.substring(7);
    }
    return null;
  }

  @Transactional
  public String updateTokenExpiration(String email, String token) {
    this.revokeToken(token);
    String newToken = this.generateToken(email);
    return newToken;
  }

  @Transactional
  public void revokeToken(String token) {
    tokenRepo.deleteByToken(token);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(this.getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
