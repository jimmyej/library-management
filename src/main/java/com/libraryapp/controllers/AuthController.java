package com.libraryapp.controllers;

import com.libraryapp.dtos.requests.LoginRequest;
import com.libraryapp.dtos.requests.SignupRequest;
import com.libraryapp.dtos.responses.MessageResponse;
import com.libraryapp.dtos.responses.UserInfoResponse;
import com.libraryapp.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    @Autowired
    AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signUpUser(@Valid @RequestBody SignupRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> signInUser(@Valid @RequestBody LoginRequest request){
        return authService.loginUser(request);
    }

    @PostMapping("/signout")
    public ResponseEntity<MessageResponse> logoutUser(){
        ResponseCookie cookie = authService.logoutUser();
        return  ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

}
