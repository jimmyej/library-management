package com.libraryapp.dtos.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RolePermissionRequest {

    private int id;
    private int idRole;
    private int idPermission;

}
