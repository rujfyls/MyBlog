package com.skillbox.controller;

import com.skillbox.controller.dto.request.*;
import com.skillbox.controller.dto.response.*;
import com.skillbox.entity.Comment;
import com.skillbox.entity.Post;
import com.skillbox.entity.Tag;
import com.skillbox.entity.User;
import com.skillbox.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;
    private final TagService tagService;
    private final UserService userService;
    private final CommentService commentService;
    private final VoteService voteService;

    public ApiPostController(PostService postService,
                             TagService tagService,
                             UserService userService,
                             CommentService commentService,
                             VoteService voteService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
        this.commentService = commentService;
        this.voteService = voteService;
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
            Integer postCount = postService.getPostsCountWithSearchQuery(query);
            return new PostsResponseDTO(postCount, listOfFoundPosts);
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
    public PostsResponseDTO postsByDate(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                        @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                        @RequestParam(value = "date", required = false) String date) {

        return new PostsResponseDTO(postService.getPostsCountByDate(date),
                postService.getPostsByDate(offset / limit, limit, date));
    }

    @GetMapping("/post/byTag")
    public PostsResponseDTO postsByTag(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                       @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                       @RequestParam(value = "tag", required = false) String tag) {

        return new PostsResponseDTO(postService.getPostsCountByTag(tag),
                postService.getPostsByTag(offset / limit, limit, tag));
    }

    @GetMapping("/post/{postId}")
    public PostResponseDTO postById(@PathVariable Integer postId, Principal principal) {
        if (principal != null) {
            User currentUser = userService.getUserByEmail(principal.getName());
            if (currentUser != null) {
                return new PostResponseDTO(getPostById(postId, currentUser));
            }
        }
        return new PostResponseDTO(postService.getPostById(postId));
    }

    @GetMapping("/post/my")
    @PreAuthorize("hasAuthority('user:write')")
    public PostsResponseDTO myPosts(Principal principal,
                                    @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                    @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                    @RequestParam(value = "status", required = false) String status) {
        User currentUser = userService.getUserByEmail(principal.getName());

        List<Post> findPosts = postService.findMyPosts(offset / limit, limit, status, currentUser.getUserId());
        Integer countPosts = postService.getCountMyPosts(status, currentUser.getUserId());
        return new PostsResponseDTO(countPosts, findPosts);
    }

    @PostMapping("/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResultResponseDTO moderationPost(@RequestBody PostModerationRequestDTO post, Principal principal) {
        User currentUser = userService.getUserByEmail(principal.getName());
        if (currentUser.getIsModerator() != 1) {
            return new ResultResponseDTO(false);
        }
        return new ResultResponseDTO(postService.checkingPostByModerator(post.getPostId(), post.getDecision(), currentUser));
    }

    @GetMapping("/post/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public PostsResponseDTO moderation(Principal principal,
                                       @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                       @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                       @RequestParam(value = "status", required = false) String status) {
        User currentUser = userService.getUserByEmail(principal.getName());

        List<Post> findPosts = postService.getPostsForModeration(offset / limit, limit, status, currentUser.getUserId());
        Integer countPosts = postService.getCountPostsForModeration(status, currentUser.getUserId());
        return new PostsResponseDTO(countPosts, findPosts);
    }

    @PostMapping("/post")
    @PreAuthorize("hasAuthority('user:write')")
    public AddingAndEditingPostResponseDTO addPost(@RequestBody PostRequestDTO postRequestDTO, Principal principal) {

        String text = postRequestDTO.getText();
        String title = postRequestDTO.getTitle();

        User currentUser = userService.getUserByEmail(principal.getName());
        EnteredPostRequestDTO enteredPost = postService.checkingEnteredPost(title, text);

        if (enteredPost.checkingForErrors()) {
            Post post = new Post();
            post.setIsActive(postRequestDTO.getActive());
            post.setText(text);
            post.setTitle(title);
            post.setUser(currentUser);
            post.setViewCount(0);

            LocalDateTime receivedTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(postRequestDTO.getTimestamp()),
                    ZoneId.systemDefault());
            LocalDateTime currentTime = LocalDateTime.now(ZoneId.systemDefault());
            if (receivedTime.isAfter(currentTime)) {
                post.setTime(receivedTime);
            } else {
                post.setTime(currentTime);
            }

            tagService.getTagsOrSave(postRequestDTO.getTags()).forEach(post::addTag);

            postService.save(post);
        }
        return new AddingAndEditingPostResponseDTO(enteredPost);
    }

    @PutMapping("/post/{postId}")
    @PreAuthorize("hasAuthority('user:write')")
    public AddingAndEditingPostResponseDTO updatePost(@PathVariable Integer postId,
                                                      @RequestBody PostRequestDTO postRequestDTO,
                                                      Principal principal) {

        Post post = getPostById(postId, userService.getUserByEmail(principal.getName()));

        String text = postRequestDTO.getText();
        String title = postRequestDTO.getTitle();
        List<Tag> receivedTags = tagService.getTagsOrSave(postRequestDTO.getTags());

        EnteredPostRequestDTO enteredPost = postService.checkingEnteredPost(title, text);

        if (enteredPost.checkingForErrors()) {
            post.setIsActive(postRequestDTO.getActive());
            post.setText(text);
            post.setTitle(title);

            receivedTags.stream().filter(tag -> !post.getTags().contains(tag)).forEach(post::addTag);
            post.getTags().removeIf(tag -> !receivedTags.contains(tag));

            postService.save(post);
        }

        return new AddingAndEditingPostResponseDTO(enteredPost);
    }

    @PostMapping("/comment")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommentResponseDTO> comment(@RequestBody CommentRequestDTO commentRequestDTO, Principal principal) {
        Integer postId = commentRequestDTO.getPostId();
        Integer parentCommentId = commentRequestDTO.getParentId();
        String text = commentRequestDTO.getText();

        User currentUser = userService.getUserByEmail(principal.getName());
        Post post = getPostById(postId, currentUser);
        Comment parentComment = commentService.getCommentById(parentCommentId);

        ErrorsCommentDTO errorsCommentDTO = new ErrorsCommentDTO();
        CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
        if (post == null && postId != null) {
            errorsCommentDTO.setText("Post with id= " + postId + " not found!");
            commentResponseDTO.setErrors(errorsCommentDTO);
            return ResponseEntity.badRequest().body(commentResponseDTO);
        }
        if (parentComment == null && parentCommentId != null) {
            errorsCommentDTO.setText("Comment with id= " + parentCommentId + " not found!");
            commentResponseDTO.setErrors(errorsCommentDTO);
            return ResponseEntity.badRequest().body(commentResponseDTO);
        }
        if (text.length() < 3) {
            errorsCommentDTO.setText("The comment text is not set or too short!");
            commentResponseDTO.setErrors(errorsCommentDTO);
            return ResponseEntity.badRequest().body(commentResponseDTO);
        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime());
        comment.setUser(currentUser);
        comment.setPost(post);

        commentResponseDTO.setCommentId(commentService.save(comment).getCommentId());

        if (parentComment != null) {
            parentComment.addComment(comment);
            comment.setParent(parentComment);
            commentService.save(comment);
            commentService.save(parentComment);
        } else {
            post.addComment(comment);
            postService.save(post);
        }

        return ResponseEntity.ok(commentResponseDTO);
    }

    @PostMapping("/post/like")
    @PreAuthorize("hasAuthority('user:write')")
    public ResultResponseDTO like(@RequestBody LikeOrDislikeRequestDTO likeRequestDTO, Principal principal) {
        return new ResultResponseDTO(voteService.likePost(
                postService.getPostById(likeRequestDTO.getPostId()),
                userService.getUserByEmail(principal.getName())));
    }

    @PostMapping("/post/dislike")
    @PreAuthorize("hasAuthority('user:write')")
    public ResultResponseDTO dislike(@RequestBody LikeOrDislikeRequestDTO dislikeRequestDTO, Principal principal) {
        return new ResultResponseDTO(voteService.dislikePost(
                postService.getPostById(dislikeRequestDTO.getPostId()),
                userService.getUserByEmail(principal.getName())));
    }

    private Post getPostById(Integer postId, User user) {
        Post inactivePost = postService.getInactivePostByIdAndUserId(postId, user.getUserId());
        if (inactivePost != null) {
            return inactivePost;
        }

        if (user.getIsModerator() > 0) {
            Post postForModeration = postService.getPostForModeration(postId);
            if (postForModeration != null) {
                return postForModeration;
            }

            Post postRejectedForModerator = postService.getPostRejectedForModerator(postId, user.getUserId());
            if (postRejectedForModerator != null) {
                return postRejectedForModerator;
            }
        }

        return postService.getPostById(postId, user);
    }
}
