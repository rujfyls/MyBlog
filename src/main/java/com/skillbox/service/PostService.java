package com.skillbox.service;

import com.skillbox.entity.Post;
import com.skillbox.entity.Vote;
import com.skillbox.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAllActivePosts(Integer offset, Integer limit, String mode) {
        if (mode.equals("recent")) {
            Pageable pageable = PageRequest.of(offset, limit, Sort.by("time").descending());
            return postRepository.findAllActivePosts(pageable);

        } else if (mode.equals("popular")) {
            Pageable pageable = PageRequest.of(offset, limit);
            return postRepository.findAllActivePosts(pageable).stream()
                    .sorted(Comparator.comparingInt(a -> a.getComments().size()))
                    .collect(Collectors.toList());

        } else if (mode.equals("best")) {
            Pageable pageable = PageRequest.of(offset, limit);
            return postRepository.findAllActivePosts(pageable).stream()
                    .sorted(Comparator.comparing(v -> v.getListOfVotes().stream()
                            .mapToInt(Vote::getValue).reduce(0, Integer::sum)))
                    .collect(Collectors.toList());

        } else {
            Pageable pageable = PageRequest.of(offset, limit, Sort.by("time").ascending());
            return postRepository.findAllActivePosts(pageable);
        }
    }

    public Integer getCountAllActivePosts() {
        return postRepository.countAllActivePosts();
    }

    public Integer getCountAllPosts() {
        return postRepository.countAllPosts();
    }
}
