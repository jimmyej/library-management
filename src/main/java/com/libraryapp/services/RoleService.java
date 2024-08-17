package com.libraryapp.services;

import java.util.List;

import com.libraryapp.dtos.requests.RoleRequest;
import com.libraryapp.entities.Role;

public interface RoleService {

    Role getRoleByRoleName(String roleName);

    Role registerRole(RoleRequest role);

    Role editRole(RoleRequest role);

    Boolean removeRoleById(int roleId);

    List<Role> getRolesByIsActivated(String activated);
}
