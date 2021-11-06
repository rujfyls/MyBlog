package com.skillbox.controller;

import com.skillbox.controller.dto.response.CalendarResponseDTO;
import com.skillbox.controller.dto.response.PostResponseDTO;
import com.skillbox.controller.dto.response.PostsResponseDTO;
import com.skillbox.controller.dto.response.TagResponseDTO;
import com.skillbox.entity.Post;
import com.skillbox.entity.User;
import com.skillbox.service.PostService;
import com.skillbox.service.TagService;
import com.skillbox.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;
    private final TagService tagService;
    private final UserService userService;

    public ApiPostController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/post")
    public PostsResponseDTO posts(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                  @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                  @RequestParam(value = "mode", defaultValue = "recent", required = false) String mode) {

        return new PostsResponseDTO(postService.getCountAllActivePosts(),
                postService.findAllActivePosts(offset / limit, limit, mode));
    }

    @GetMapping("/tag")
    public TagResponseDTO tag() {
        return new TagResponseDTO(tagService.getWeightAllTags(postService.getCountAllPosts()));
    }

    @GetMapping("/post/search")
    public PostsResponseDTO searchPosts(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                        @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                        @RequestParam(value = "query", required = false) String query) {
        if (query != null && !query.trim().isEmpty()) {
            List<Post> listOfFoundPosts = postService.searchPosts(offset / limit, limit, query);
            return new PostsResponseDTO(listOfFoundPosts.size(), listOfFoundPosts);
        } else {
            return posts(offset / limit, limit, "recent");
        }
    }

    @GetMapping("/calendar")
    public CalendarResponseDTO calendar(@RequestParam(value = "year", required = false) String year) {
        if (year.isEmpty()) {
            year = String.valueOf(LocalDateTime.now().getYear());
        }
        return new CalendarResponseDTO(postService.getListOfYearsWithPosts(), postService.getCountPostsByYear(year));
    }

    @GetMapping("/post/byDate")
    public PostsResponseDTO getPostsByDate(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                           @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                           @RequestParam(value = "date", required = false) String date) {

        List<Post> listOfFoundPosts = postService.getPostsByDate(offset / limit, limit, date);
        return new PostsResponseDTO(listOfFoundPosts.size(), listOfFoundPosts);
    }

    @GetMapping("/post/byTag")
    public PostsResponseDTO getPostsByTag(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                          @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                          @RequestParam(value = "tag", required = false) String tag) {

        List<Post> listOfFoundPosts = postService.getPostsByTag(offset / limit, limit, tag);
        return new PostsResponseDTO(listOfFoundPosts.size(), listOfFoundPosts);
    }

    @GetMapping("/post/{postId}")
    public PostResponseDTO getPostById(@PathVariable Integer postId) {
        return new PostResponseDTO(postService.getPostById(postId));
    }

    @GetMapping("/post/my")
    public PostsResponseDTO getMyPosts(Principal principal,
                                       @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                       @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                       @RequestParam(value = "status", required = false) String status) {
        User currentUser = userService.getUserByEmail(principal.getName());

        List<Post> findPosts = postService.findMyPosts(offset / limit, limit, status, currentUser.getUserId());
        Integer countPosts = postService.getCountMyPosts(status, currentUser.getUserId());
        return new PostsResponseDTO(countPosts, findPosts);
    }
}
