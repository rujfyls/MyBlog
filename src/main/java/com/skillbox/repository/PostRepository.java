package com.skillbox.repository;

import com.skillbox.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(nativeQuery = true, value = "select count(*) from posts where " +
            "is_active=1 and moderation_status='ACCEPTED' and time <= current_timestamp")
    Integer countAllActivePosts();

    @Query(nativeQuery = true, value = "select * from posts where " +
            "is_active=1 and moderation_status='ACCEPTED' and time <= current_timestamp")
    List<Post> findAllActivePosts(Pageable pageable);

    @Query(nativeQuery = true, value = "select count(*) from posts")
    Integer countAllPosts();
}
