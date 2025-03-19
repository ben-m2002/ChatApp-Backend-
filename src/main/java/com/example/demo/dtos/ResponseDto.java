package com.example.demo.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto {
    private String message;
    private String token;

    public ResponseDto(String message, String token) {
        this.message = message;
        this.token = token;
    }
}
