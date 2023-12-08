package com.example.socialmedianetwork.repo;

import com.example.socialmedianetwork.dto.PostRequest;
import com.example.socialmedianetwork.entity.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostContent,Long> {

    @Query("SELECT p FROM PostContent p ORDER BY p.likes DESC LIMIT 1")
    List<PostContent> findTrendingPosts();


}
