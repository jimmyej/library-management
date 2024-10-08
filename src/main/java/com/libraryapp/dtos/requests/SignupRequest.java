package com.libraryapp.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
    private String createdBy;
    private String updatedBy;
}
