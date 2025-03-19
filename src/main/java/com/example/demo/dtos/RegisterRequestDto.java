package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterRequestDto extends AuthRequestDto {
    private String firstName;
    private String lastName;
    public RegisterRequestDto(
            @JsonProperty("email")  String email,
            @JsonProperty("password")  String password)
    {
        super(email, password);
    }
}
