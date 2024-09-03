package com.libraryapp.repositories;

import com.libraryapp.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsernameAndEnabled(String username, boolean enabled);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
