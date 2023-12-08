package com.example.socialmedianetwork.securityConfig;

import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("username not found"));

        return new org.springframework.security.core.userdetails.User(user.getEmail() , user.getPassword() , Collections.emptyList());
    }
}
