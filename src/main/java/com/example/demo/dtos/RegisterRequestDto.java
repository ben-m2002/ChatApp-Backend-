package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterRequestDto extends AuthRequestDto {
    private String email;
    public RegisterRequestDto(@JsonProperty("username") String username,
                              @JsonProperty("password")  String password,
                              @JsonProperty("email")  String email) {
        super(username, password);
        this.email = email;
    }
}
