package com.skillbox.repository;

import com.skillbox.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c WHERE id = :commentId")
    Comment findCommentById(Integer commentId);
}
