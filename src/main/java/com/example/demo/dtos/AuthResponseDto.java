package com.example.demo.dtos;

import lombok.Data;



@Data
public class AuthResponseDto extends ResponseDto {
    private String email;

    public AuthResponseDto(String email, String message, String token) {
        super(message, token);
        this.email = email;
    }
}
