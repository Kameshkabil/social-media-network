package com.example.socialmedianetwork.controller;

import com.example.socialmedianetwork.dto.PostRequest;
import com.example.socialmedianetwork.dto.PutPostContentRequest;
import com.example.socialmedianetwork.entity.PostContent;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.exception.ResourceNotFoundException;
import com.example.socialmedianetwork.repo.PostRepository;
import com.example.socialmedianetwork.repo.UserRepository;
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


    @SecurityRequirement(name = "bearer")
    @PostMapping("/posts/create")
    public ResponseEntity createPost(@RequestBody PostRequest postRequest , HttpServletRequest request){
        try {
           // Optional<User> user = userRepository.findById(postRequest.getUserId());
            Long userId = getUserIdFromToken(request);

            Optional<User> user = userRepository.findById(userId);
            user.ifPresent((u) -> {
                PostContent postContent = new PostContent();
                postContent.setContent(postRequest.getContent());
                postContent.setTitle(postRequest.getTitle());
                postContent.setUser(u);
                postContent.setLikes(0);
                postRepository.save(postContent);
            });
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
//
//        User user = postContent.getUser();
//        user.getPosts().add(postContent);
//        postContent.setUser(user);
//        return this.postRepository.save(postContent);
    }

    @SecurityRequirement(name = "bearer")
    @GetMapping("/posts/{postId}")
    public PostContent getParticularPost(@PathVariable(value = "postId") long postId){
        PostContent postContent = postRepository.findById(postId).get();
        return postContent;
    }

    @SecurityRequirement(name = "bearer")
    @PutMapping("/posts/{postId}")
    public PostContent updatePostContent(@RequestBody PostContent postContent , @PathVariable(value = "postId") long postId){
        PostContent postContentExisting = this.postRepository.findById(postId).orElse(null);

        postContentExisting.setTitle(postContent.getTitle());
        postContentExisting.setContent(postContent.getContent());
        return this.postRepository.save(postContentExisting);
    }


    @SecurityRequirement(name = "bearer")
    @DeleteMapping("posts/delete/{postId}")
    public ResponseEntity<PostContent> deleteParticularPostContent(@PathVariable(value = "postId") long postId){
        Optional<PostContent> postContent = postRepository.findById(postId);
        postContent.ifPresent((p) -> {
            Optional<User> user = userRepository.findById(p.getUser().getId());
            user.ifPresent((u) -> {
                u.getPosts().remove(p);
                userRepository.save(u);
            });
        });
        return ResponseEntity.ok().build();
//        if(postContentExisting.isPresent()){
//            this.postRepository.deleteById(postContentExisting.get().getId());
//            return "Deleted Successfully..";
//        }else {
//            return "post not found";
//        }
    }


    @SecurityRequirement(name = "bearer")
    @PostMapping("/posts/{postId}/like")
    public void likePost(@PathVariable(value = "postId") long postId){
        PostContent postContent = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found with id:"+ postId));

        postContent.setLikes(postContent.getLikes()+1);
        postRepository.save(postContent);
    }

    @SecurityRequirement(name = "bearer")
    @DeleteMapping("/posts/{postId}/like")
    public void unLikePost(@PathVariable(value = "postId") long postId){
        PostContent postContent = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post Not Found"+postId));

        int newlikes = postContent.getLikes()-1;
        if(newlikes<0){
            postContent.setLikes(0);
        }else {
            postContent.setLikes(newlikes);
        }
        postRepository.save(postContent);
    }



    @SecurityRequirement(name = "bearer")
    @GetMapping("/trending")
    public List<PostContent> getTrendingPosts(){
        return postRepository.findTrendingPosts();
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
