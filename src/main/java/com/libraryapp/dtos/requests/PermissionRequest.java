package com.libraryapp.dtos.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PermissionRequest {

    private int permissionId;

    private String permissionName;

    private String description;

    private Boolean activated;

}
