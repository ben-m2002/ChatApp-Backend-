package com.example.demo.repos;

import com.example.demo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserModelRepo extends JpaRepository<UserModel, Integer> {
    UserModel findByUsername(String username);
}
