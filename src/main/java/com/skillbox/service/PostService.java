package com.skillbox.service;

import com.skillbox.entity.Post;
import com.skillbox.entity.User;
import com.skillbox.entity.enums.ModerationStatus;
import com.skillbox.exceptions.PostNotFoundException;
import com.skillbox.pojo.EnteredPost;
import com.skillbox.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAllActivePosts(Integer offset, Integer limit, String mode) {
        switch (mode) {
            case "recent":
                return postRepository.findAll(PageRequest.of(offset, limit, Sort.by("time").descending())).toList();
            case "popular":
                return postRepository.findAllPostsSortedByCommentCount(PageRequest.of(offset, limit)).toList();
            case "best":
                return postRepository.findPostsSortedByLikeCount(PageRequest.of(offset, limit)).toList();
            default:
                return postRepository.findAll(PageRequest.of(offset, limit, Sort.by("time").ascending())).toList();
        }
    }

    public Integer getCountAllActivePosts() {
        return postRepository.getCountAllActivePosts();
    }

    public Integer getCountAllPosts() {
        return postRepository.getCountAllPosts();
    }

    public Map<String, Integer> getCountPostsByYear(String year) {
        Map<String, Integer> posts = new HashMap<>();

        postRepository.findAll().forEach(post -> {
            if (post.getTime().getYear() == Integer.parseInt(year)) {
                String date = post.getTime().format(formatter);
                if (posts.containsKey(date)) {
                    posts.replace(date, posts.get(date) + 1);
                } else {
                    posts.put(date, 1);
                }
            }
        });

        return posts;
    }

    public List<Post> searchPosts(Integer offset, Integer limit, String query) {
        List<Post> posts = new ArrayList<>();
        postRepository.findAll(PageRequest.of(offset, limit)).forEach(post -> {
            if (checkForMatch(query, post.getTitle())) {
                posts.add(post);
            }
        });
        return posts;
    }

    public Set<Integer> getListOfYearsWithPosts() {
        Set<Integer> years = new HashSet<>();
        postRepository.findAll().forEach(post -> {
            years.add(post.getTime().getYear());
        });
        return years;
    }

    public List<Post> getPostsByDate(Integer offset, Integer limit, String date) {
        return postRepository.findPostsByDate(LocalDate.parse(date, formatter),
                PageRequest.of(offset, limit)).toList();
    }

    public Integer getPostsCountByDate(String date) {
        return postRepository.getPostsCountByDate(LocalDate.parse(date, formatter));
    }

    public List<Post> getPostsByTag(Integer offset, Integer limit, String tag) {
        return postRepository.findPostByTag(tag,
                PageRequest.of(offset, limit)).toList();
    }

    public Integer getPostsCountByTag(String tag) {
        return postRepository.getPostsCountByTag(tag);
    }

    public Post getPostById(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException("Post with id=" + postId + " not found"));
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return post;
    }

    public Post getPostById(Integer postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException("Post with id=" + postId + " not found"));
        if (user.getIsModerator() == 0 || !post.getUser().equals(user)) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        }
        return post;
    }

    public Post getInactivePostByIdAndUserId(Integer postId, Integer userId) {
        return postRepository.findInactivePostByIdAndUserId(postId, userId);
    }

    public Post getPostRejectedForModerator(Integer postId, Integer userId) {
        return postRepository.findPostRejectedForModerator(postId, userId);
    }

    public Post getPostForModeration(Integer postId) {
        return postRepository.findPostForModeration(postId);
    }

    public List<Post> findMyPosts(Integer offset, Integer limit, String status, Integer userId) {
        ModerationStatus moderationStatus = getModerationStatus(status);
        short isActive = getActiveFromStatus(status);

        return postRepository.findMyPosts(moderationStatus,
                isActive, userId, PageRequest.of(offset, limit)).toList();
    }

    public Boolean checkingPostByModerator(Integer postId, String decision, User user) {
        Post post = postRepository.findByIdForModerator(postId).orElseThrow(() ->
                new PostNotFoundException("Post with id=" + postId + " not found"));
        ModerationStatus moderationStatus = getModerationStatus(decision);

        if (post == null || moderationStatus == null) {
            return false;
        }

        post.setModerationStatus(moderationStatus);
        post.setModeratorId(user.getUserId());
        postRepository.save(post);

        return true;
    }

    public List<Post> getPostsForModeration(Integer offset, Integer limit, String status, Integer userId) {
        ModerationStatus moderationStatus = getModerationStatus(status);
        if (moderationStatus.name().equals("NEW")) {
            return postRepository.findAllPostsForModeration(moderationStatus,
                    PageRequest.of(offset, limit)).toList();
        } else {
            return postRepository.findMyPostsForModeration(moderationStatus,
                    userId,
                    PageRequest.of(offset, limit)).toList();
        }
    }

    public Integer getCountPostsForModeration(String status, Integer userId) {
        ModerationStatus moderationStatus = getModerationStatus(status);
        if (moderationStatus.name().equals("NEW")) {
            return postRepository.getCountAllPostForModeration(moderationStatus);
        } else {
            return postRepository.getCountMyPostsForModeration(moderationStatus, userId);
        }
    }

    public Integer getCountMyPosts(String status, Integer userId) {
        ModerationStatus moderationStatus = getModerationStatus(status);
        short isActive = getActiveFromStatus(status);

        return postRepository.getCountMyPosts(moderationStatus, isActive, userId);
    }

    public EnteredPost checkingEnteredPost(String title, String text) {
        EnteredPost enteredPost = new EnteredPost();
        if (title.trim().isEmpty() || title.length() < 3) {
            enteredPost.setTitle("Заголовок не установлен");
        } else {
            enteredPost.setTitle("");
        }

        if (text.trim().isEmpty() || text.length() < 50) {
            enteredPost.setText("Текст публикации слишком короткий");
        } else {
            enteredPost.setText("");
        }

        return enteredPost;
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public Integer getLikesCountByUserId(Integer userId) {
        return postRepository.getLikesCountByUserId(userId);
    }

    public Integer getDislikesCountByUserId(Integer userId) {
        return postRepository.getDislikesCountByUserId(userId);
    }

    public Integer getPostsCountByUserId(Integer userId) {
        return postRepository.getPostsCountByUserId(userId);
    }

    public Integer getViewsCountByUserId(Integer userId) {
        return postRepository.getViewsCountByUserId(userId);
    }

    public Long getFirstPublicationByUserId(Integer userId) {
        return postRepository.findFirstPublicationByUserId(userId,
                PageRequest.of(0, 1)).getContent().get(0).getTime().toEpochSecond(ZoneOffset.UTC);
    }

    private Short getActiveFromStatus(String status) {
        if (status.equals("inactive")) {
            return 0;
        }
        return 1;
    }

    private ModerationStatus getModerationStatus(String status) {
        if (status.equals("inactive") || status.equals("pending") || status.equals("new")) {
            return ModerationStatus.NEW;
        } else if (status.equals("declined") || status.equals("decline")) {
            return ModerationStatus.DECLINED;
        } else {
            return ModerationStatus.ACCEPTED;
        }
    }

    private boolean checkForMatch(String what, String where) {
        Pattern p = Pattern.compile(what.toLowerCase());
        Matcher m = p.matcher(where.toLowerCase());
        return m.find();
    }
}
