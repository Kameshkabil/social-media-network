package com.example.socialmedianetwork.controller;

import com.example.socialmedianetwork.dto.SendMessageDto;
import com.example.socialmedianetwork.dto.SendMessageRequest;
import com.example.socialmedianetwork.entity.Message;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.exception.ResourceNotFoundException;
import com.example.socialmedianetwork.repo.MessageRepository;
import com.example.socialmedianetwork.repo.UserRepository;
import com.example.socialmedianetwork.service.MessageService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private UserRepository userRepository;
    private MessageRepository messageRepository;

    @Autowired
    public MessageController(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }
    @Autowired
    MessageService messageService;


//    @SecurityRequirement(name = "bearer")
//    @PostMapping("/send")
//    public ResponseEntity<Void> sendDirectMessage(@RequestBody SendMessageRequest request){
//        User sender = userRepository.findById(request.getSenderId())
//               .orElseThrow(()->new ResourceNotFoundException("Sender not found"));
//
//        User receiver = userRepository.findById(request.getReceiverId())
//                .orElseThrow(()->new ResourceNotFoundException("Receiver not Found"));
//
//        Message message = new Message();
//        message.setSender(sender);
//        message.setReceiver(receiver);
//        message.setContent(request.getContent());
//
//        messageRepository.save(message);
//        return ResponseEntity.ok().build();
//    }


    @SecurityRequirement(name = "bearer")
    @PostMapping("/send")
    public ResponseEntity<Void> sendDirectMessage(@RequestBody SendMessageDto sendMessageDto , HttpServletRequest request){
        messageService.sendDirectMessage(sendMessageDto , request);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/{conversationId}")
//    public ResponseEntity<List<MessageDTO>> getMessagesInConversation(@PathVariable Long conversationId) {
//        List<Message> messages = messageRepository.findByConversationId(conversationId);
//
//        // Convert messages to DTOs that include sender and receiver information
//        List<MessageDTO> messageDTOs = messages.stream()
//                .map(message -> new MessageDTO(message.getContent(),
//                        message.getSender().getId(),
//                        message.getReceiver().getId()))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(messageDTOs);
//    }


    @SecurityRequirement(name = "bearer")
    @GetMapping("/{conversationId}")
    public ResponseEntity<List<SendMessageRequest>> getMessagesConversation(@PathVariable(value = "conversationId") long conversationId){
        List<SendMessageRequest> messageRequests = messageService.getMessagesConversation(conversationId);
        return ResponseEntity.ok(messageRequests);
    }
//
//    @SecurityRequirement(name = "bearer")
//    @GetMapping("/{conversationId}")
//    public Message getMessagesInConversation(@PathVariable(value = "conversationId") long conversionId){
//        return this.messageRepository.findById(conversionId)
//                .orElseThrow(()->new ResourceNotFoundException("Conversation ID not found"+conversionId));
//    }


    public Long getUserIdFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            Claims claims = Jwts.parser().setSigningKey("kamesh-secret-key").parseClaimsJws(token).getBody();

            Object userIdObject = claims.get("userID");

            if (userIdObject instanceof Number) {
                return ((Number) userIdObject).longValue();
            } else if (userIdObject instanceof String) {
                return Long.parseLong((String) userIdObject);
            }

            return null;
        }

        return null;
    }
}
