package com.libraryapp.services.impls;

import com.libraryapp.entities.UserEntity;
import com.libraryapp.repositories.UserRepository;
import com.libraryapp.securities.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsernameAndEnabled(username, true)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with username : "+username));
        return UserDetailsImpl.build(user);
    }
}
