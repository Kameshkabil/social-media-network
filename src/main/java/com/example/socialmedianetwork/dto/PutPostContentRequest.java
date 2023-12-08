package com.example.socialmedianetwork.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutPostContentRequest {
    private String title;
    private String content;

}
