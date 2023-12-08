package com.example.socialmedianetwork.repo;

import com.example.socialmedianetwork.entity.Message;
import com.example.socialmedianetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findBySenderAndReceiver(User sender , User receiver);
    Optional<Message> findByIdAndSenderAndReceiver(Long messageId,User sender,User receiver);
   // List<Message> findBySenderConversationIdOrReceiverConversationId(Long senderConversationId, Long receiverConversationId);
   //List<Message> findByConversationId(Long conversationId);
    List<Message> findBySender_IdOrReceiver_Id(long senderId, long receiverId);

}
