package com.example.socialmedianetwork.controller;


import com.example.socialmedianetwork.dto.UserSignUp;
import com.example.socialmedianetwork.entity.JwtResponse;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.entity.UserLoginRequest;
import com.example.socialmedianetwork.repo.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @PostMapping("/signup")
    public User createNewUserAccount(@RequestBody UserSignUp userSignUp){
//        User user1 = new User();
//        user1.setUsername(user.getUsername());
//        user1.setEmail(user.getEmail());
//        user1.setPassword(passwordEncoder.encode(user.getPassword()));
//        user1.setMobileNumber(user.getMobileNumber());
//        user1.setLocation(user.getLocation());
//        user1.setCreatedAt(user.getCreatedAt());
//        user1.setUpdatedAt(user.getUpdatedAt());
//        return this.userRepository.save(user1);
        User user = new User();
        user.setUsername(userSignUp.getUsername());
        user.setEmail(userSignUp.getEmail());
        user.setPassword(user.getPassword());
        user.setMobileNumber(userSignUp.getMobileNumber());
        user.setPassword(passwordEncoder.encode(userSignUp.getPassword()));
        user.setLocation(userSignUp.getLocation());
        return this.userRepository.save(user);
    }


    @SecurityRequirement(name = "bearer")
    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> getPaticularUserProfileInformation(@PathVariable(value= "userId") long userId){
        Optional<User> user = userRepository.findById(userId);
        User response = null;
        if(user.isPresent()){
            System.out.println(user.get().getEmail());
            response = user.get();
        }
        return ResponseEntity.ok(user.get());
//        return ResponseEntity.ok(userId);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> checkLoginUser(@RequestBody UserLoginRequest userLoginRequest){
        try{
            authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getEmailId(),userLoginRequest.getPassword())
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        User user = userRepository.findByEmail(userLoginRequest.getEmailId()).get();
        User user1 = userRepository.findById(user.getId()).get();
        String token = generateJwtToken(user1.getId() , user.getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @SecurityRequirement(name = "bearer")
    @PutMapping("/users/{userId}")
    public User updateUserProfileDetail(@RequestBody User user , @PathVariable(value ="userId") long userId){
        User userExisting = this.userRepository.findById(userId).orElse(null);

        userExisting.setUsername(user.getUsername());
        userExisting.setEmail(user.getEmail());
        userExisting.setPassword(user.getPassword());
        userExisting.setMobileNumber(user.getMobileNumber());
        userExisting.setLocation(user.getLocation());
        return this.userRepository.save(userExisting);

    }


private String generateJwtToken(Long userId, String email){
    return Jwts.builder()
            .claim("userID",userId)
            .claim("email",email)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date( System.currentTimeMillis()+3600000+1000))
            .signWith(SignatureAlgorithm.HS512,"kamesh-secret-key")
            .compact();
}

}
