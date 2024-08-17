package com.libraryapp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryapp.entities.RolePermission;

@Repository
public interface RolePermissionRepository extends CrudRepository<RolePermission, Integer> {

    List<RolePermission> findByRoleId(int roleId);

    List<RolePermission> findByPermissionId(int permissionId);

}
