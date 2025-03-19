package com.example.demo.dtos;


import lombok.Data;

import java.util.List;


@Data
public class UsersResponseDto extends ResponseDto {
    private List<AuthResponseDto> users;

    public UsersResponseDto(String message, String token, List<AuthResponseDto> users) {
        super(message, token);
        this.users = users;
    }
}
