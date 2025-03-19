package com.example.demo.repos;

import com.example.demo.models.AuthTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenModelRepo extends JpaRepository<AuthTokenModel, Integer> {
    AuthTokenModel findByToken(String token);
    void deleteByToken(String token);
}
