package com.example.socialmedianetwork.controller;

import com.example.socialmedianetwork.dto.CommentRequestDto;
import com.example.socialmedianetwork.entity.Comment;
import com.example.socialmedianetwork.entity.PostContent;
import com.example.socialmedianetwork.repo.CommentRepository;
import com.example.socialmedianetwork.repo.PostRepository;
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
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;


    @SecurityRequirement(name = "bearer")
    @PostMapping("/create/comment")
    public ResponseEntity<String> createComment(@RequestBody CommentRequestDto commentRequestDto , HttpServletRequest request){

        Optional<PostContent> postContent = this.postRepository.findById(commentRequestDto.getPostId());
                //.orElseThrow(()->new ResourceNotFoundException("postId not found"));
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


//    public Long getUserIdFromToken(HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//
//            Claims claims = Jwts.parser().setSigningKey("kamesh-secret-key").parseClaimsJws(token).getBody();
//           // String userId = (String) claims.get("Email");
//            Long userId = (Long) claims.get("userID");
//            return userId;
//        }
//
//        return null;
//    }

//    public Long getUserIdFromToken(HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//
//            Claims claims = Jwts.parser().setSigningKey("kamesh-secret-key").parseClaimsJws(token).getBody();
//
//            Object userIdObject = claims.get("userID");
//
//            if (userIdObject instanceof Number) {
//                return ((Number) userIdObject).longValue();
//            } else if (userIdObject instanceof String) {
//                return Long.parseLong((String) userIdObject);
//            }
//
//            return null;
//        }
//
//        return null;
//    }








    @SecurityRequirement(name = "bearer")
    @DeleteMapping("/delete/comment/{commentId}")
    public ResponseEntity<Comment> deleteCommentById(@PathVariable(value = "commentId") long commentId){
//        Comment comment = this.commentRepository.findById(commentId).orElseThrow();
//        this.commentRepository.delete(comment);
//        return "Comment Deleted Successfully";
        //-----------------------------------------
//        Optional<PostContent> postContent = postRepository.findById(postId);
//        postContent.ifPresent((p) -> {
//            Optional<User> user = userRepository.findById(p.getUser().getId());
//            user.ifPresent((u) -> {
//                u.getPosts().remove(p);
//                userRepository.save(u);
//            });
//        });
//        return ResponseEntity.ok().build();

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
