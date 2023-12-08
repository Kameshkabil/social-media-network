package com.example.socialmedianetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {
    private long senderId;
    private long receiverId;
    private String content;

    public SendMessageRequest(String content, long id, long id1) {
        this.content = content;
        this.senderId=id;
        this.receiverId=id1;
    }
}
