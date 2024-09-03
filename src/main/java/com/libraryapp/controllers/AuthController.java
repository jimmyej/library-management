package com.libraryapp.controllers;

import com.libraryapp.dtos.requests.LoginRequest;
import com.libraryapp.dtos.requests.SignupRequest;
import com.libraryapp.dtos.responses.MessageResponse;
import com.libraryapp.dtos.responses.UserInfoResponse;
import com.libraryapp.entities.RoleEntity;
import com.libraryapp.entities.UserEntity;
import com.libraryapp.enums.ERole;
import com.libraryapp.repositories.RoleRepository;
import com.libraryapp.repositories.UserRepository;
import com.libraryapp.securities.JwtUtils;
import com.libraryapp.securities.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String ROLE_NOT_FOUND_MESSAGE = "Error: Role not found!";

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;

    @Autowired
    AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder encoder,
            JwtUtils jwtUtils){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signUpUser(@Valid @RequestBody SignupRequest request) {
        if(this.userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error username is already taken!"));
        }
        if(this.userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error email is already taken!"));
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .build();

        Set<String> setRoles = request.getRoles();
        Set<RoleEntity> roles = new HashSet<>();
        if(setRoles == null){
            RoleEntity roleEntity = this.roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
            roles.add(roleEntity);
        } else {
            setRoles.forEach(role -> {
                switch (role){
                    case "admin":
                        RoleEntity adminRole = this.roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        RoleEntity modRole = this.roleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
                        roles.add(modRole);
                        break;
                    default:
                        RoleEntity userRole = this.roleRepository.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_MESSAGE));
                        roles.add(userRole);
                        break;
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> signInUser(@Valid @RequestBody LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserInfoResponse response = UserInfoResponse.builder()
                .userId(userDetails.getUserId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<MessageResponse> logoutUser(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return  ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

}
