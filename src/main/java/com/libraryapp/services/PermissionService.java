package com.libraryapp.services;

import java.util.List;

import com.libraryapp.dtos.requests.PermissionRequest;
import com.libraryapp.entities.Permission;

public interface PermissionService {

    List<Permission> getPermissionsByName(String permissionName);

    Permission registerPermission(PermissionRequest permission);

    Permission editPermission(PermissionRequest permission);

    Boolean removePermissionById(int permissionId);

    List<Permission> getPermissionsByIsActivated(String activated);

}
