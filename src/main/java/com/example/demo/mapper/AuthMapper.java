package com.example.demo.mapper;


import com.example.demo.dtos.AuthRequestDto;
import com.example.demo.dtos.AuthResponseDto;
import com.example.demo.dtos.RegisterRequestDto;
import com.example.demo.models.UserModel;
import org.springframework.stereotype.Service;

@Service
public class AuthMapper {

    public UserModel registerRequestDtoToUserModel(RegisterRequestDto registerRequestDto) {
        return UserModel.builder()
                .email(registerRequestDto.getEmail())
               .password(registerRequestDto.getPassword())
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
               .build();
    }

    public UserModel authRequestDtoToUserModel(AuthRequestDto authRequestDto) {
        return UserModel.builder().email(authRequestDto.getEmail())
                .password(authRequestDto.getPassword())
                .build();
    }

    public AuthResponseDto userModelToAuthResponseDto(UserModel userModel) {
        return new AuthResponseDto(userModel.getEmail(), "User Successfully Retrieved", "");
    }

}
