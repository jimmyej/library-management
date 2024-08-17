package com.libraryapp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.libraryapp.entities.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    List<Role> findByRoleName(String roleName);

    List<Role> findByActivated(boolean isActivated);

    boolean existsByRoleName(String roleName);

}
