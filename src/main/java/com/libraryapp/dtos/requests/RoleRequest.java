package com.libraryapp.dtos.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class RoleRequest {

    private int roleId;

    private String roleName;

    private Boolean activated;

}
