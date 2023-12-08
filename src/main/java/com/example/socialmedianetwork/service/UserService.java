package com.example.socialmedianetwork.service;


import com.example.socialmedianetwork.dto.UserSignUp;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createNewUserAccount(UserSignUp userSignUp){
        User user = new User();
        user.setUsername(userSignUp.getUsername());
        user.setEmail(userSignUp.getEmail());
        user.setPassword(passwordEncoder.encode(userSignUp.getPassword()));
        user.setMobileNumber(userSignUp.getMobileNumber());
        user.setLocation(userSignUp.getLocation());
        return userRepository.save(user);
    }

    public Optional<User> getUserById(long userId){
        return userRepository.findById(userId);
    }


    public User updateUserProfileDetail(User updatedUser, long userId) {
        User userExisting = userRepository.findById(userId).orElse(null);

        if (userExisting != null) {
            userExisting.setUsername(updatedUser.getUsername());
            userExisting.setEmail(updatedUser.getEmail());
            userExisting.setPassword(updatedUser.getPassword());
            userExisting.setMobileNumber(updatedUser.getMobileNumber());
            userExisting.setLocation(updatedUser.getLocation());
            return userRepository.save(userExisting);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
}
