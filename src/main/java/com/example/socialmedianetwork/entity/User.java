package com.example.socialmedianetwork.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String email;
    private String mobileNumber;
    private String location;



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    public User(long id, String username, String password, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PostContent> posts = new ArrayList<>();


    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL , fetch = FetchType.EAGER ,orphanRemoval = true)
    List<Friend> sendFriendRequest = new ArrayList<>();

    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL , fetch = FetchType.EAGER ,orphanRemoval = true)
    List<Friend> receivedFriendRequest = new ArrayList<>();


    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Message> receivedMessages = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Message> sentMessages = new ArrayList<>();

    public List<Friend> getSendFriendRequest() {
        return sendFriendRequest;
    }

    public void setSendFriendRequest(List<Friend> sendFriendRequest) {
        this.sendFriendRequest = sendFriendRequest;
    }

    public List<Friend> getReceivedFriendRequest() {
        return receivedFriendRequest;
    }

    public void setReceivedFriendRequest(List<Friend> receivedFriendRequest) {
        this.receivedFriendRequest = receivedFriendRequest;
    }



    public List<PostContent> getPosts() {
        return posts;
    }

    public void setPosts(List<PostContent> posts) {
        this.posts = posts;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
