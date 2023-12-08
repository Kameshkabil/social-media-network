package com.example.socialmedianetwork.controller;

import com.example.socialmedianetwork.dto.FriendDto;
import com.example.socialmedianetwork.entity.Friend;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.exception.ResourceNotFoundException;
import com.example.socialmedianetwork.repo.FriendRepository;
import com.example.socialmedianetwork.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    UserRepository userRepository;

//    @SecurityRequirement(name = "bearer")
//    @PostMapping("/add")
//    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendDto friendDto , @AuthenticationPrincipal){
//
//    }

    @SecurityRequirement(name = "bearer")
    @PostMapping("/add")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendDto friendDto , HttpServletRequest request) {

        //Long senderId = friendDto.getSenderId();
        Long senderId = getUserIdFromToken(request);
        Long receiverId = friendDto.getReceiverId();

        User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        Friend friend = new Friend();
        friend.setSender(sender);
        friend.setReceiver(receiver);
        friend.setStatus(Friend.FriendStatus.PENDING);

        friendRepository.save(friend);

        return ResponseEntity.ok().build();
    }
//
//    @PutMapping("/accept/{requestId}")
//    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId) {
//        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
//                .orElseThrow(() -> new NotFoundException("Friend request not found"));
//
//        friendRequest.setStatus(FriendRequestStatus.ACCEPTED);
//
//        friendRequestRepository.save(friendRequest);
//
//        // You might want to handle the logic of adding friends to UserEntity here
//
//        return ResponseEntity.ok().build();
//    }

//    @PutMapping("/accept/{requestId}")
//    public ResponseEntity<Void> acceptFriendRequest(@PathVariable(value = "requestId") long requestId){
//        Friend friend = friendRepository.findById(requestId).orElseThrow(()->new ResourceNotFoundException("Friend request not found"));
//        friend.setStatus(Friend.FriendStatus.ACCEPTED);
//        friendRepository.save(friend);
//        return ResponseEntity.ok().build();
//    }

    @SecurityRequirement(name = "bearer")
    @PutMapping("/accept/{requestId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable(value = "requestId") long requestId){
        Friend friend = friendRepository.findById(requestId).orElseThrow(()->new ResourceNotFoundException("Friend request not found"));
        friend.setStatus(Friend.FriendStatus.ACCEPTED);

        User sender = friend.getSender();
        User receiver = friend.getReceiver();

        Friend friendship = new Friend();
        friendship.setSender(sender);
        friendship.setReceiver(receiver);

        sender.getSendFriendRequest().add(friendship);
        receiver.getReceivedFriendRequest().add(friendship);

        friendRepository.save(friend);
        userRepository.save(sender);
        userRepository.save(receiver);
        return ResponseEntity.ok().build();

    }


    @SecurityRequirement(name = "bearer")
    @DeleteMapping("remove/{friendId}")
    public ResponseEntity<Void> removeFriendConnection(@PathVariable(value = "friendId") long friendId){
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(()->new ResourceNotFoundException("Friend Connection not Found"));

        User sender = friend.getSender();
        User receiver = friend.getReceiver();

        sender.getSendFriendRequest().remove(friend);
        receiver.getReceivedFriendRequest().remove(friend);

        friendRepository.delete(friend);

        userRepository.save(sender);
        userRepository.save(receiver);

        return ResponseEntity.ok().build();
    }

//    public ResponseEntity<List<FriendResponseDTO>> getFriendsList(@PathVariable(value = "userId") long userId) {
//        try {
//            List<Friend> friends = friendRepository.findBySender_IdOrReceiver_IdAndStatus(userId, userId, Friend.FriendStatus.ACCEPTED);
//
//            List<FriendResponseDTO> friendDTOs = friends.stream()
//                    .map(friend -> new FriendResponseDTO(
//                            friend.getFriend().getId(),
//                            friend.getFriend().getUsername(),
//                            friend.getFriend().getEmail()))
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok(friendDTOs);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }


    @SecurityRequirement(name = "bearer")
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<FriendDto>> getFriendsList(@PathVariable(value = "userId") long userId){
        try {
            List<Friend> friends = friendRepository.findBySender_IdOrReceiver_IdAndStatus(userId , userId , Friend.FriendStatus.ACCEPTED);

            List<FriendDto> friendList = friends.stream()
                    .map(friend -> new FriendDto(/*friend.getSender().getId(),*/friend.getReceiver().getId()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(friendList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    public Long getUserIdFromToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            Claims claims = Jwts.parser().setSigningKey("kamesh-secret-key").parseClaimsJws(token).getBody();
            Object userIdObject = claims.get("userID");

            if(userIdObject instanceof Number){
                return ((Number) userIdObject).longValue();
            } else if (userIdObject instanceof String) {
                return Long.parseLong((String) userIdObject);
            }
            return null;
        }
        return null;
    }



}
