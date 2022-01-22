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
    Integer getCountAllActivePosts();

    @Query("SELECT p FROM Post p WHERE p.isActive = 1  " +
            "and p.moderationStatus = 'ACCEPTED' and p.time <= current_timestamp")
    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN p.comments AS comment WHERE p.isActive = 1  " +
            "AND p.moderationStatus = 'ACCEPTED' AND p.time <= current_timestamp " +
            "GROUP BY p ORDER BY COUNT(comment) DESC")
    Page<Post> findAllPostsSortedByCommentCount(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN p.listOfVotes AS vote WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' AND p.time <= current_timestamp " +
            "GROUP BY p ORDER BY COUNT(CASE WHEN vote.value>0 THEN 1 END) DESC")
    Page<Post> findPostsSortedByLikeCount(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1 and p.moderationStatus = :moderationStatus")
    Page<Post> findAllPostsForModeration(ModerationStatus moderationStatus, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1 AND p.moderationStatus = :moderationStatus " +
            "AND p.moderatorId = :userId")
    Page<Post> findMyPostsForModeration(ModerationStatus moderationStatus, Integer userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1  " +
            "AND p.moderationStatus = 'ACCEPTED' AND p.time <= current_timestamp")
    List<Post> findAll();

    @Query("SELECT COUNT(p) FROM Post p")
    Integer getCountAllPosts();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.isActive = 1 AND p.moderationStatus = :moderationStatus")
    Integer getCountAllPostForModeration(ModerationStatus moderationStatus);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.isActive = 1 AND p.moderationStatus = :moderationStatus " +
            "AND p.moderatorId = :userId")
    Integer getCountMyPostsForModeration(ModerationStatus moderationStatus, Integer userId);

    @Query("SELECT p FROM Post p WHERE DATE(time) = :date AND p.isActive = 1  " +
            "AND p.moderationStatus = 'ACCEPTED' AND p.time <= current_timestamp")
    Page<Post> findPostsByDate(LocalDate date, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p WHERE DATE(time) = :date AND p.isActive = 1  " +
            "AND p.moderationStatus = 'ACCEPTED' AND p.time <= current_timestamp")
    Integer getPostsCountByDate(LocalDate date);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tag AND p.isActive = 1  " +
            "AND p.moderationStatus = 'ACCEPTED' AND p.time <= current_timestamp")
    Page<Post> findPostByTag(String tag, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= current_timestamp " +
            "AND (p.text LIKE CONCAT('%',:query,'%') OR p.title LIKE CONCAT('%',:query,'%'))")
    Page<Post> findPostsWithSearchQuery(String query, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.isActive = 1  " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= current_timestamp " +
            "AND (p.text LIKE CONCAT('%',:query,'%') OR p.title LIKE CONCAT('%',:query,'%'))")
    Integer getPostsCountWithSearchQuery(String query);

    @Query("SELECT COUNT(p) FROM Post p JOIN p.tags t WHERE t.name = :tag AND p.isActive = 1  " +
            "AND p.moderationStatus = 'ACCEPTED' AND p.time <= current_timestamp")
    Integer getPostsCountByTag(String tag);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1  and p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= current_timestamp AND postId = :postId")
    Optional<Post> findById(Integer postId);

    @Query("SELECT p FROM Post p JOIN p.user u WHERE u.userId = :userId AND p.postId = :postId")
    Post findMyPostByIdAndUserId(Integer postId, Integer userId);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1 " +
            "AND p.moderatorId = :userId AND p.postId = :postId")
    Post findPostRejectedForModerator(Integer postId, Integer userId);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1 AND p.moderationStatus = 'NEW' AND p.postId = :postId")
    Post findPostForModeration(Integer postId);

    @Query("SELECT p FROM Post p WHERE p.isActive = 1  AND postId = :postId")
    Optional<Post> findByIdForModerator(Integer postId);

    @Query("SELECT p FROM Post p JOIN p.user u WHERE p.isActive = :isActive AND p.moderationStatus = :moderationStatus " +
            "AND u.userId = :userId")
    Page<Post> findMyPosts(ModerationStatus moderationStatus, Short isActive, Integer userId, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p JOIN p.user u WHERE p.isActive = :isActive AND p.moderationStatus = :moderationStatus " +
            "AND u.userId = :userId")
    Integer getCountMyPosts(ModerationStatus moderationStatus, Short isActive, Integer userId);

    @Query("SELECT COUNT(p) FROM Post p JOIN p.user u WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND u.userId = :userId")
    Integer getPostsCountByUserId(Integer userId);

    @Query("SELECT COUNT(v) FROM Vote v JOIN v.user u JOIN v.post p WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND v.value > 0 " +
            "AND p.user.userId = :userId")
    Integer getLikesCountByUserId(Integer userId);

    @Query("SELECT COUNT(v) FROM Vote v JOIN v.user u JOIN v.post p WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND v.value < 0 " +
            "AND p.user.userId = :userId")
    Integer getDislikesCountByUserId(Integer userId);

    @Query("SELECT SUM(p.viewCount) FROM Post p JOIN p.user u WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND u.userId = :userId")
    Integer getViewsCountByUserId(Integer userId);

    @Query("SELECT p FROM Post p JOIN p.user u WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND u.userId = :userId " +
            "ORDER BY p.time ASC")
    List<Post> findFirstPublicationByUserId(Integer userId);
}
