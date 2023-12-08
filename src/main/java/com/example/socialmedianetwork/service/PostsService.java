package com.example.socialmedianetwork.service;

import com.example.socialmedianetwork.dto.PostRequest;
import com.example.socialmedianetwork.entity.PostContent;
import com.example.socialmedianetwork.entity.User;
import com.example.socialmedianetwork.exception.ResourceNotFoundException;
import com.example.socialmedianetwork.repo.PostRepository;
import com.example.socialmedianetwork.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity createPost(PostRequest postRequest, HttpServletRequest request) {
        try {
           // Long userId = getUserIdFromToken(request);
            Long userId = (Long) request.getAttribute("userID");
            Optional<User> user = userRepository.findById(userId);

            user.ifPresent(u -> {
                PostContent postContent = new PostContent();
                postContent.setContent(postRequest.getContent());
                postContent.setTitle(postRequest.getTitle());
                postContent.setUser(u);
                postContent.setLikes(0);
                postRepository.save(postContent);
            });

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public PostContent getParticularPost(long postId){
        return postRepository.findById(postId).orElse(null);
    }

    public PostContent updatePostContent(PostContent updatedPostContent, long postId) {
        PostContent postContentExisting = postRepository.findById(postId).orElse(null);

        if (postContentExisting != null) {
            postContentExisting.setTitle(updatedPostContent.getTitle());
            postContentExisting.setContent(updatedPostContent.getContent());
            return postRepository.save(postContentExisting);
        } else {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }


    public void likePost(long postId) {
        PostContent postContent = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        postContent.setLikes(postContent.getLikes() + 1);
        postRepository.save(postContent);
    }

    public void unLikePost(long postId) {
        PostContent postContent = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        int newLikes = postContent.getLikes() - 1;
        if (newLikes < 0) {
            postContent.setLikes(0);
        } else {
            postContent.setLikes(newLikes);
        }
        postRepository.save(postContent);
    }

    public List<PostContent> getTrendingPosts() {
        return postRepository.findTrendingPosts();
    }

    public void deleteParticularPostContent(long postId) {
        Optional<PostContent> postContent = postRepository.findById(postId);
        postContent.ifPresent(p -> {
            Optional<User> user = userRepository.findById(p.getUser().getId());
            user.ifPresent(u -> {
                u.getPosts().remove(p);
                userRepository.save(u);
            });
        });
    }
}
