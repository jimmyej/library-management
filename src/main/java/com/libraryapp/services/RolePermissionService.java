package com.libraryapp.services;

import java.util.List;
import com.libraryapp.dtos.requests.RolePermissionRequest;
import com.libraryapp.entities.RolePermission;

public interface RolePermissionService {

    List<RolePermission> getRolePermissionByRoleId(int roleId);

    List<RolePermission> getRolePermissionByPermissionId(int permissionId);

    RolePermission registerRolePermission(RolePermissionRequest rolePermission);

    RolePermission editRolePermission(RolePermissionRequest rolePermission);

    boolean removeRolePermissionById(int rolePermissionId);
}
