package com.example.socialmedianetwork.repo;

import com.example.socialmedianetwork.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend , Long> {

    List<Friend> findBySender_IdOrReceiver_IdAndStatus(long senderId, long receiverId, Friend.FriendStatus friendStatus);
}
