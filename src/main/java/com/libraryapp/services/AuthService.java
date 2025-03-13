package com.libraryapp.services;

import com.libraryapp.dtos.requests.LoginRequest;
import com.libraryapp.dtos.requests.SignupRequest;
import com.libraryapp.dtos.responses.MessageResponse;
import com.libraryapp.dtos.responses.UserInfoResponse;
import com.libraryapp.entities.RoleEntity;
import com.libraryapp.enums.ERole;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<MessageResponse> registerUser(SignupRequest request);
    ResponseEntity<UserInfoResponse> loginUser(LoginRequest request);
    ResponseCookie logoutUser();
    RoleEntity registerRoles(ERole role);
}
