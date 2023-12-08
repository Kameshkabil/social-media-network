package com.example.socialmedianetwork.service;

import com.example.socialmedianetwork.dto.CommentRequestDto;
import com.example.socialmedianetwork.entity.Comment;
import com.example.socialmedianetwork.entity.PostContent;
import com.example.socialmedianetwork.repo.CommentRepository;
import com.example.socialmedianetwork.repo.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    public ResponseEntity<String> createComment(CommentRequestDto commentRequestDto , HttpServletRequest request){

        Optional<PostContent> postContent = this.postRepository.findById(commentRequestDto.getPostId());
        postContent.ifPresent((p)-> {
            Comment comment = new Comment();
            comment.setComment(commentRequestDto.getComment());
            comment.setPostContent(p);

            Long userId =  (Long)request.getAttribute("userID");
            comment.setUserId(userId);

            this.commentRepository.save(comment);
        });
        return new ResponseEntity<String>( "Comment Posted Successfully✅", HttpStatus.OK);
    }

    public ResponseEntity<Comment> deleteCommentById(long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent((c) ->{
            Optional<PostContent> postContent = postRepository.findById(c.getPostContent().getId());
            postContent.ifPresent((p)->{
                p.getComments().remove(c);
                postRepository.save(p);
            });
        });
        return ResponseEntity.ok().build();
    }
}
