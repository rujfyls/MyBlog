package com.skillbox.service;

import com.skillbox.entity.Post;
import com.skillbox.entity.Vote;
import com.skillbox.exceptions.PostNotFoundException;
import com.skillbox.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAllActivePosts(Integer offset, Integer limit, String mode) {
        if (mode.equals("recent")) {
            return postRepository.findAll(PageRequest.of(offset, limit, Sort.by("time").descending())).toList();

        } else if (mode.equals("popular")) {
            return postRepository.findAll(PageRequest.of(offset, limit)).stream()
                    .sorted(Comparator.comparingInt(a -> a.getComments().size()))
                    .collect(Collectors.toList());

        } else if (mode.equals("best")) {
            return postRepository.findAll(PageRequest.of(offset, limit)).stream()
                    .sorted(Comparator.comparing(v -> v.getListOfVotes().stream()
                            .mapToInt(Vote::getValue).reduce(0, Integer::sum)))
                    .collect(Collectors.toList());

        } else {
            return postRepository.findAll(PageRequest.of(offset, limit, Sort.by("time").ascending())).toList();
        }
    }

    public Integer getCountAllActivePosts() {
        return postRepository.countAllActivePosts();
    }

    public Integer getCountAllPosts() {
        return postRepository.countAllPosts();
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

    public List<Post> getPostsByTag(Integer offset, Integer limit, String tag) {
        return postRepository.findPostByTag(tag,
                PageRequest.of(offset, limit)).toList();
    }

    public Post getPostById(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException("Post with id=" + postId + " not found"));
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return post;
    }

    private boolean checkForMatch(String what, String where) {
        Pattern p = Pattern.compile(what.toLowerCase());
        Matcher m = p.matcher(where.toLowerCase());
        return m.find();
    }
}
