package com.example.socialmedianetwork.controller;


import com.example.socialmedianetwork.dto.UserSignUp;
import com.example.socialmedianetwork.entity.JwtResponse;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.entity.UserLoginRequest;
import com.example.socialmedianetwork.repo.UserRepository;
import com.example.socialmedianetwork.service.UserService;
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

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> createNewUserAccount(@RequestBody UserSignUp userSignUp){

        User newUser = userService.createNewUserAccount(userSignUp);
        return ResponseEntity.ok("User account created successfully");
    }


    @SecurityRequirement(name = "bearer")
    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> getPaticularUserProfileInformation(@PathVariable(value= "userId") long userId){
//        Optional<User> user = userRepository.findById(userId);
//        User response = null;
//        if(user.isPresent()){
//            System.out.println(user.get().getEmail());
//            response = user.get();
//        }
//        return ResponseEntity.ok(user.get());
////        return ResponseEntity.ok(userId);

        Optional<User> user = userService.getUserById(userId);
        if(user.isPresent()){
            System.out.println(user.get().getUsername());
            return ResponseEntity.ok(user.get());
        }else{
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<User> updateUserProfileDetail(@RequestBody User user , @PathVariable(value ="userId") long userId){
        try{
            User updateUserProfile = userService.updateUserProfileDetail(user , userId);
            return ResponseEntity.ok(updateUserProfile);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
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
