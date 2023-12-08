package com.example.socialmedianetwork.service;

import com.example.socialmedianetwork.dto.FriendDto;
import com.example.socialmedianetwork.entity.Friend;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.exception.ResourceNotFoundException;
import com.example.socialmedianetwork.repo.FriendRepository;
import com.example.socialmedianetwork.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendRepository friendRepository;

    public void sendFriendRequest(FriendDto friendDto, HttpServletRequest request){
        Long senderId = (Long) request.getAttribute("userID");
        Long receiverId = friendDto.getReceiverId();

        User sender = userRepository.findById(senderId).orElseThrow(()->new ResourceNotFoundException("sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(()->new ResourceNotFoundException("Receiver not found"));

        Friend friend = new Friend();
        friend.setSender(sender);
        friend.setReceiver(receiver);
        friend.setStatus(Friend.FriendStatus.PENDING);
        friendRepository.save(friend);
    }

    public void acceptFriendRequest(long requestId){
        Friend friend = friendRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend request not found"));

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
    }

    public void removeFriendConnection(long friendId){
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend Connection not Found"));

        User sender = friend.getSender();
        User receiver = friend.getReceiver();

        sender.getSendFriendRequest().remove(friend);
        receiver.getReceivedFriendRequest().remove(friend);

        friendRepository.delete(friend);

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public List<FriendDto> getFriendsList(long userId) {
        try {
            List<Friend> friends = friendRepository.findBySender_IdOrReceiver_IdAndStatus(userId, userId, Friend.FriendStatus.ACCEPTED);

            return friends.stream()
                    .map(friend -> new FriendDto(/*friend.getSender().getId(),*/ friend.getReceiver().getId()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching friends list", e);
        }
    }

}
