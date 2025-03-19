package com.example.demo.mapper;


import com.example.demo.dtos.AuthRequestDto;
import com.example.demo.dtos.AuthResponseDto;
import com.example.demo.dtos.RegisterRequestDto;
import com.example.demo.models.UserModel;
import org.springframework.stereotype.Service;

@Service
public class AuthMapper {

    public UserModel registerRequestDtoToUserModel(RegisterRequestDto registerRequestDto) {
        return UserModel.builder().username(registerRequestDto.getUsername())
               .password(registerRequestDto.getPassword())
               .email(registerRequestDto.getEmail())
               .build();
    }

    public UserModel authRequestDtoToUserModel(AuthRequestDto authRequestDto) {
        return UserModel.builder().username(authRequestDto.getUsername())
                .password(authRequestDto.getPassword())
                .build();
    }

    public AuthResponseDto userModelToAuthResponseDto(UserModel userModel) {
        return new AuthResponseDto(userModel.getUsername(), userModel.getEmail(), "User Successfully Retrieved", "");
    }

}
