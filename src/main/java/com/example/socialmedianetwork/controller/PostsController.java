package com.example.socialmedianetwork.controller;

import com.example.socialmedianetwork.dto.PostRequest;
import com.example.socialmedianetwork.dto.PutPostContentRequest;
import com.example.socialmedianetwork.entity.PostContent;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.exception.ResourceNotFoundException;
import com.example.socialmedianetwork.repo.PostRepository;
import com.example.socialmedianetwork.repo.UserRepository;
import com.example.socialmedianetwork.service.PostsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PostsController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostsService postsService;

    @SecurityRequirement(name = "bearer")
    @PostMapping("/posts/create")
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest , HttpServletRequest request){
        return postsService.createPost(postRequest,request);

//        try {
//           // Optional<User> user = userRepository.findById(postRequest.getUserId());
//            Long userId = getUserIdFromToken(request);
//
//            Optional<User> user = userRepository.findById(userId);
//            user.ifPresent((u) -> {
//                PostContent postContent = new PostContent();
//                postContent.setContent(postRequest.getContent());
//                postContent.setTitle(postRequest.getTitle());
//                postContent.setUser(u);
//                postContent.setLikes(0);
//                postRepository.save(postContent);
//            });
//            return ResponseEntity.ok().build();
//        }catch (Exception e){
//            return ResponseEntity.ok(e.getMessage());
//        }

    }

    @SecurityRequirement(name = "bearer")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostContent> getParticularPost(@PathVariable(value = "postId") long postId){
        PostContent postContent = postsService.getParticularPost(postId);
        if(postContent!=null){
            return ResponseEntity.ok(postContent);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    @SecurityRequirement(name = "bearer")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostContent> updatePostContent(@RequestBody PostContent postContent , @PathVariable(value = "postId") long postId){
        try {
            PostContent updatedPost = postsService.updatePostContent(postContent , postId);
            return ResponseEntity.ok(updatedPost);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }


    @SecurityRequirement(name = "bearer")
    @DeleteMapping("posts/delete/{postId}")
    public ResponseEntity<Void> deleteParticularPostContent(@PathVariable(value = "postId") long postId){
        postsService.deleteParticularPostContent(postId);
        return ResponseEntity.ok().build();
    }


    @SecurityRequirement(name = "bearer")
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable(value = "postId") long postId){
      try {
          postsService.likePost(postId);
          return ResponseEntity.ok().build();
      }catch (ResourceNotFoundException e){
          return ResponseEntity.notFound().build();
      }
    }

    @SecurityRequirement(name = "bearer")
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<Void> unLikePost(@PathVariable(value = "postId") long postId){
        try {
            postsService.unLikePost(postId);
            return ResponseEntity.ok().build();
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }



    @SecurityRequirement(name = "bearer")
    @GetMapping("/trending")
    public ResponseEntity<List<PostContent>> getTrendingPosts(){
        List<PostContent> trendingPosts = postsService.getTrendingPosts();
        return ResponseEntity.ok(trendingPosts);
    }



    //get PayLoad Data in  jwtToken
    public Long getUserIdFromToken(HttpServletRequest httpServletRequest){
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            Claims claims = Jwts.parser().setSigningKey("kamesh-secret-key").parseClaimsJws(token).getBody();
            Object userIdObject = claims.get("userID");

            if (userIdObject instanceof Number) {
                return ((Number) userIdObject).longValue();
            } else if (userIdObject instanceof String) {
                return Long.parseLong((String) userIdObject);
            }
            return null;
        }
        return null;
    }
}
