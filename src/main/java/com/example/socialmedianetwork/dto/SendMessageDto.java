package com.example.socialmedianetwork.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageDto {
    private long receiverId;
    private String content;
}
