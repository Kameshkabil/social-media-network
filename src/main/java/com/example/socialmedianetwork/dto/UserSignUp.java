package com.example.socialmedianetwork.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUp {
    private long id;
    private String username;
    private String password;
    private String email;
    private String mobileNumber;
    private String location;

}
