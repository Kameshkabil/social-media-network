package com.example.socialmedianetwork.service;


import com.example.socialmedianetwork.dto.SendMessageDto;
import com.example.socialmedianetwork.dto.SendMessageRequest;
import com.example.socialmedianetwork.entity.Message;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.exception.ResourceNotFoundException;
import com.example.socialmedianetwork.repo.MessageRepository;
import com.example.socialmedianetwork.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    public void sendDirectMessage(SendMessageDto sendMessageDto , HttpServletRequest request){
        Long senderId = (Long) request.getAttribute("userID");

        User sender = userRepository.findById(senderId)
                .orElseThrow(()->new ResourceNotFoundException("Sender not found"));
        User receiver = userRepository.findById(sendMessageDto.getReceiverId())
                .orElseThrow(()->new ResourceNotFoundException("Receiver not Found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(sendMessageDto.getContent());

        messageRepository.save(message);

    }

    public List<SendMessageRequest> getMessagesConversation(long conversationId){
        try {
            List<Message> messages = messageRepository.findBySender_IdOrReceiver_Id(conversationId , conversationId);

            return messages.stream()
                    .map(message -> new SendMessageRequest(
                            message.getContent(),
                            message.getSender().getId(),
                            message.getReceiver().getId()
                    )).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error fetching conversation message",e);
        }
    }
}
