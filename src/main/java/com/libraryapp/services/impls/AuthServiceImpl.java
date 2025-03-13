package com.libraryapp.services.impls;

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
import com.libraryapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String ROLE_NOT_FOUND_MESSAGE = "Error: Role not found!";

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;

    @Autowired
    AuthServiceImpl(
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

    public ResponseEntity<MessageResponse> registerUser(SignupRequest request) {
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
                .createdBy(request.getCreatedBy())
                .updatedBy(request.getUpdatedBy())
                .enabled(true)
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
        return new ResponseEntity<>(new MessageResponse("User registered successfully"), HttpStatus.CREATED);
    }

    public ResponseEntity<UserInfoResponse> loginUser(LoginRequest request) {
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

    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }

    public RoleEntity registerRoles(ERole role){
        RoleEntity newRole = null;
        boolean existsRole = roleRepository.existsByRoleName(role);
        if(!existsRole) {
            newRole = roleRepository.save(new RoleEntity(role));
        }
        return  newRole;
    }
}
