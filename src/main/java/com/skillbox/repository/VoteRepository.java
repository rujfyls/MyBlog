package com.skillbox.repository;

import com.skillbox.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT v FROM Vote v JOIN v.user u JOIN v.post p WHERE u.userId= :userId AND p.postId = :postId")
    Vote findVoteByUserIdAndPostId(Integer userId, Integer postId);
}
