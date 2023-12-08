package com.example.socialmedianetwork.controller;

import com.example.socialmedianetwork.dto.FriendDto;
import com.example.socialmedianetwork.entity.Friend;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.exception.ResourceNotFoundException;
import com.example.socialmedianetwork.repo.FriendRepository;
import com.example.socialmedianetwork.repo.UserRepository;
import com.example.socialmedianetwork.service.FriendService;
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

    @Autowired
    FriendService friendService;

//    @SecurityRequirement(name = "bearer")
//    @PostMapping("/add")
//    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendDto friendDto , @AuthenticationPrincipal){
//
//    }

    @SecurityRequirement(name = "bearer")
    @PostMapping("/add")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendDto friendDto , HttpServletRequest request) {
        friendService.sendFriendRequest(friendDto,request);
        return ResponseEntity.ok("Friend request send successfully");
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
        friendService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();

    }


    @SecurityRequirement(name = "bearer")
    @DeleteMapping("remove/{friendId}")
    public ResponseEntity<Void> removeFriendConnection(@PathVariable(value = "friendId") long friendId){
        friendService.removeFriendConnection(friendId);
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
        List<FriendDto> friendDtoList = friendService.getFriendsList(userId);
        return ResponseEntity.ok(friendDtoList);
    }





}
