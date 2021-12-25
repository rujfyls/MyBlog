package com.skillbox.service;

import com.skillbox.entity.Post;
import com.skillbox.entity.User;
import com.skillbox.entity.Vote;
import com.skillbox.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote getVoteByUserIdAndPostId(Integer userId, Integer postId) {
        return voteRepository.findVoteByUserIdAndPostId(userId, postId);
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    public Boolean likePost(Post post, User user) {
        return likeOrDislike(getVoteByUserIdAndPostId(user.getUserId(), post.getPostId()),
                Short.valueOf("1"),
                post,
                user);
    }

    public Boolean dislikePost(Post post, User user) {
        return likeOrDislike(getVoteByUserIdAndPostId(user.getUserId(), post.getPostId()),
                Short.valueOf("-1"),
                post,
                user);
    }


    private Boolean likeOrDislike(Vote vote, Short value, Post post, User user) {
        if (vote == null) {
            Vote newVote = new Vote();
            newVote.setPost(post);
            newVote.setTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime());
            newVote.setUser(user);
            newVote.setValue(value);

            save(newVote);
            return true;
        }
        if (vote.getValue().equals(value)) {
            return false;
        }
        vote.setValue(value);
        save(vote);
        return true;
    }
}
