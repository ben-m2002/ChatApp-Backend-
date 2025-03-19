package com.example.demo.services;

import com.example.demo.dtos.AuthRequestDto;
import com.example.demo.dtos.AuthResponseDto;
import com.example.demo.dtos.RegisterRequestDto;
import com.example.demo.dtos.UsersResponseDto;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.models.UserModel;
import com.example.demo.models.UserPrincipal;
import com.example.demo.others.Pair;
import com.example.demo.repos.UserModelRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService extends APIService {

  private final AuthMapper authMapper;
  private final UserModelRepo repo;
  private BCryptPasswordEncoder encoder;
  private final AuthenticationManager authManager;

  public AuthService(
      AuthMapper authMapper,
      UserModelRepo repo,
      AuthenticationManager authManager,
      JWTService jwtService) {
    super(jwtService);
    this.authMapper = authMapper;
    this.repo = repo;
    this.encoder = new BCryptPasswordEncoder(12);
    this.authManager = authManager;
  }

  public ResponseEntity<AuthResponseDto> register(RegisterRequestDto registerRequestDto) {
    UserModel userModel = authMapper.registerRequestDtoToUserModel(registerRequestDto);
    userModel.setPassword(encoder.encode(userModel.getPassword()));
    try {
      UserModel savedUser = repo.save(userModel);
      AuthResponseDto responseDto = authMapper.userModelToAuthResponseDto(savedUser);
      responseDto.setMessage("User registered successfully");
      responseDto.setToken(this.jwtService.generateToken(userModel.getUsername()));
      return ResponseEntity.ok(responseDto);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new AuthResponseDto("", "", e.getMessage(), ""));
    }
  }

  public ResponseEntity<AuthResponseDto> login(AuthRequestDto authRequestDto) {
    UserModel userModel = authMapper.authRequestDtoToUserModel(authRequestDto);
    Pair<Boolean, UserPrincipal> authPair = authenticate(userModel);
    if (authPair.getFirst()) {
      AuthResponseDto dto =
          new AuthResponseDto(
              userModel.getUsername(),
              authPair.getSecond().getEmail(),
              "User Successfully Logged In",
              this.jwtService.generateToken(userModel.getUsername()));
      return ResponseEntity.ok(dto);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new AuthResponseDto("", "", "Invalid Credentials", ""));
    }
  }

  // This needs to return A token
  public ResponseEntity<UsersResponseDto> getUsers(HttpServletRequest request) {
    List<AuthResponseDto> allUsers =
        repo.findAll().stream()
            .map(authMapper::userModelToAuthResponseDto)
            .collect(Collectors.toList());
    String newToken = this.updateTokenExpiration(request);
    return ResponseEntity.ok(new UsersResponseDto("All users", newToken, allUsers));
  }

  private Pair<Boolean, UserPrincipal> authenticate(UserModel userModel) {
    // you basically take an object of Authentication and then actually authenticate it
    Authentication auth =
        this.authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userModel.getUsername(), userModel.getPassword()));
    return new Pair<>(true, (UserPrincipal) auth.getPrincipal());
  }
}
