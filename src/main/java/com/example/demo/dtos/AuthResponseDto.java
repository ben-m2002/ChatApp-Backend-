package com.example.demo.dtos;

import lombok.Data;



@Data
public class AuthResponseDto extends ResponseDto {
    private String username;
    private String email;

    public AuthResponseDto(String username, String email, String message, String token) {
        super(message, token);
        this.username = username;
        this.email = email;
    }
}
