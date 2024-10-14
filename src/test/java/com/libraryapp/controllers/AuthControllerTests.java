package com.libraryapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryapp.dtos.requests.LoginRequest;
import com.libraryapp.dtos.requests.SignupRequest;
import com.libraryapp.entities.RoleEntity;
import com.libraryapp.entities.UserEntity;
import com.libraryapp.enums.ERole;
import com.libraryapp.repositories.RoleRepository;
import com.libraryapp.repositories.UserRepository;
import com.libraryapp.securities.JwtUtils;
import com.libraryapp.securities.UserDetailsImpl;
import com.libraryapp.services.impls.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@WithMockUser(username = "user")
class AuthControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    SignupRequest signupRequest;
    UserEntity user;

    @BeforeEach
    public void setup(){
        //Login information
        signupRequest = new SignupRequest();
        signupRequest.setUsername("test.user");
        signupRequest.setEmail("test.user@gmail.com");
        signupRequest.setPassword("123456789");
        signupRequest.setRoles(null);
        signupRequest.setCreatedBy("bot.user");
        signupRequest.setUpdatedBy("bot.user");

        user = UserEntity.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .createdBy(signupRequest.getCreatedBy())
                .updatedBy(signupRequest.getUpdatedBy())
                .enabled(true)
                .build();
    }

    //SIGNUP tests
    @Test
    void registerUser_withoutRole_success() throws Exception {

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(1);
        roleEntity.setRoleName(ERole.ROLE_USER);

        UserEntity newUser = user;
        newUser.setUserId(1);

        Mockito.when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        Mockito.when(roleRepository.findByRoleName(ERole.ROLE_USER)).thenReturn(Optional.of(roleEntity));
        Mockito.when(userRepository.save(user)).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/signup").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signupRequest))
        )
                .andExpect(status().isCreated())
        ;
    }

    @Test
    void registerUser_withRoleAdmin_success() throws Exception {
        registerUserWithRole("admin", ERole.ROLE_ADMIN);
    }

    @Test
    void registerUser_withRoleMod_success() throws Exception {
        registerUserWithRole("mod", ERole.ROLE_MODERATOR);
    }

    @Test
    void registerUser_withRoleUser_success() throws Exception {
        registerUserWithRole("user", ERole.ROLE_USER);
    }

    void registerUserWithRole(String roleName, ERole roleValue) throws  Exception{
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(1);
        roleEntity.setRoleName(roleValue);

        signupRequest.setRoles(Set.of(roleName));

        UserEntity newUser = user;
        newUser.setUserId(1);

        Mockito.when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        Mockito.when(roleRepository.findByRoleName(roleValue)).thenReturn(Optional.of(roleEntity));
        Mockito.when(userRepository.save(user)).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signup").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signupRequest))
                )
                .andExpect(status().isCreated())
        ;
    }

    @Test
    void registerUser_withExistingUsername() throws Exception {
        registerExistingUser(true, false);
    }

    @Test
    void registerUser_withExistingEmail() throws Exception {
        registerExistingUser(false, true);
    }

    void registerExistingUser(boolean existsUsername, boolean existsEmail) throws  Exception{
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(1);
        roleEntity.setRoleName(ERole.ROLE_USER);

        Mockito.when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(existsUsername);
        if(!existsUsername){
            Mockito.when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(existsEmail);
        }

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signup").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(signupRequest))
                )
                .andExpect(status().isBadRequest())
        ;
    }

    //SIGNIN tests
    @Test
    void loginUser_success() throws  Exception{
        String username = "test.user";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword("123456789");

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(1);
        roleEntity.setRoleName(ERole.ROLE_USER);

        Set<RoleEntity> roles = Set.of(roleEntity);

        user.setRoles(roles);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username(username)
                .authorities(authorities)
                .userId(1)
                .email("test.user@gmail.com")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String jwt = "ftedrthdfhe5kseagehsrsjehrmksegdmed5dsersshdghnsraerhshdjsedhxfd";
        ResponseCookie responseCookie = ResponseCookie.from("testCookie", jwt).path("/api").maxAge(1800L).httpOnly(true).build();

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtCookie(userDetails)).thenReturn(responseCookie);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signin").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk());
    }

    //SIGNOUT tests
    @Test
    void logoutUser_success() throws Exception {
        String jwt = "ftedrthdfhe5kseagehsrsjehrmksegdmed5dsersshdghnsraerhshdjsedhxfd";
        ResponseCookie responseCookie = ResponseCookie.from("testCookie", jwt).path("/api").maxAge(1800L).httpOnly(true).build();

        Mockito.when(jwtUtils.getCleanJwtCookie()).thenReturn(responseCookie);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/signout").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

    }

}
