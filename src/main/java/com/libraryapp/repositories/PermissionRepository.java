package com.libraryapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryapp.entities.Permission;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Integer> {

    Optional<Permission> findByPermissionName(String permissionName);

    List<Permission> findByActivated(boolean isActivated);

    boolean existsByPermissionName(String permissionName);
}
