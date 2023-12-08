package com.example.socialmedianetwork.controller;

import com.example.socialmedianetwork.dto.CommentRequestDto;
import com.example.socialmedianetwork.entity.Comment;
import com.example.socialmedianetwork.entity.PostContent;
import com.example.socialmedianetwork.repo.CommentRepository;
import com.example.socialmedianetwork.repo.PostRepository;
import com.example.socialmedianetwork.service.CommentService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    CommentService commentService;


    @SecurityRequirement(name = "bearer")
    @PostMapping("/create/comment")
    public ResponseEntity<String> createComment(@RequestBody CommentRequestDto commentRequestDto , HttpServletRequest request){
        return commentService.createComment(commentRequestDto , request);
    }


    @SecurityRequirement(name = "bearer")
    @DeleteMapping("/delete/comment/{commentId}")
    public ResponseEntity<Comment> deleteCommentById(@PathVariable(value = "commentId") long commentId){
        return commentService.deleteCommentById(commentId);
    }
}
