package com.libraryapp;

import com.libraryapp.dtos.requests.SignupRequest;
import com.libraryapp.enums.ERole;
import com.libraryapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    AuthService authService;

    @Value("${library.app.initial.user}")
    private String user;

    @Value("${library.app.initial.password}")
    private String password;

    @Value("${library.app.initial.email}")
    private String email;

    @Value("${library.app.initial.roles}")
    private String roles;

    @Autowired
    public CommandLineAppStartupRunner(AuthService authService){
        this.authService = authService;
    }

    public void run(String... args) throws Exception {
        registerInitialUser();
    }

    public void registerInitialUser(){

        authService.registerRoles(ERole.ROLE_ADMIN);
        authService.registerRoles(ERole.ROLE_USER);
        authService.registerRoles(ERole.ROLE_MODERATOR);

        SignupRequest request = new SignupRequest();
        request.setUsername(user);
        request.setPassword(password);
        request.setEmail(email);
        request.setRoles(Arrays.stream(roles.split(",")).collect(Collectors.toSet()));
        authService.registerUser(request);
    }
}
