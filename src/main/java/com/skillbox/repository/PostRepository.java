package com.skillbox.repository;

import com.skillbox.entity.Post;
import com.skillbox.entity.enums.ModerationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    @Query("SELECT count(p) FROM Post p WHERE p.isActive = 1 " +
            "and p.moderationStatus = 'ACCEPTED' and p.time <= current_timestamp")
    Integer countAllActivePosts();

    @Query("SELECT p FROM Post p WHERE p.isActive = 1  " +
            "and p.moderationStatus = 'ACCEPTED' and p.time <= current_timestamp")
    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1  " +
            "and p.moderationStatus = 'ACCEPTED' and p.time <= current_timestamp")
    List<Post> findAll();

    @Query("SELECT count(p) FROM Post p")
    Integer countAllPosts();

    @Query("SELECT p FROM Post p WHERE DATE(time) = :date AND p.isActive = 1  " +
            "and p.moderationStatus = 'ACCEPTED' and p.time <= current_timestamp")
    Page<Post> findPostsByDate(LocalDate date, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tag AND p.isActive = 1  " +
            "and p.moderationStatus = 'ACCEPTED' and p.time <= current_timestamp")
    Page<Post> findPostByTag(String tag, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1  and p.moderationStatus = 'ACCEPTED' " +
            "and p.time <= current_timestamp and id = :postId")
    Optional<Post> findById(Integer postId);

    @Query("SELECT p FROM Post p JOIN p.user u WHERE p.isActive = :isActive AND p.moderationStatus = :moderationStatus " +
            "AND u.userId = :userId")
    Page<Post> findMyPosts(ModerationStatus moderationStatus, Short isActive, Integer userId, Pageable pageable);

    @Query("SELECT count(p) FROM Post p JOIN p.user u WHERE p.isActive = :isActive AND p.moderationStatus = :moderationStatus " +
            "AND u.userId = :userId")
    Integer countMyPosts(ModerationStatus moderationStatus, Short isActive, Integer userId);
}
