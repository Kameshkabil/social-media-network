package com.example.socialmedianetwork.dto;

import com.example.socialmedianetwork.entity.PostContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    //private long userId;

}
