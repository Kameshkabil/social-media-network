package com.example.socialmedianetwork.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;
    private FriendStatus status;


    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }




    public enum FriendStatus{
        PENDING,
        ACCEPTED,
        REJECTED
    }
}
