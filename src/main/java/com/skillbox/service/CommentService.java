package com.skillbox.service;

import com.skillbox.entity.Comment;
import com.skillbox.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepository.findCommentById(commentId);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
