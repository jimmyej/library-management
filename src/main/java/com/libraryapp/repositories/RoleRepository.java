package com.libraryapp.repositories;

import com.libraryapp.entities.RoleEntity;
import com.libraryapp.enums.ERole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByRoleName(ERole roleName);
}
