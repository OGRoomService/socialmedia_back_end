package com.mantarays.socialbackend.Controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.mantarays.socialbackend.Models.Comment;
import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Repositories.UserRepository;
import com.mantarays.socialbackend.Services.PostService;
import com.mantarays.socialbackend.Services.UserService;
import com.mantarays.socialbackend.VerificationServices.PostTextVerification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PostController
{
    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PostTextVerification postTextVerification;

    @PostMapping("/posts/createPost")
    public ResponseEntity<?> createPost(@RequestParam Map<String, String> myMap)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/createPost").toUriString());
        Post post = new Post(
            0,
            Long.valueOf(myMap.get("poster_id")),
            Long.valueOf(myMap.get("original_poster_id")),
            myMap.get("post_text"),
            0,
            0,
            new ArrayList<Comment>(),
            new Date()
        );
        User user = userRepository.findById(Long.valueOf(myMap.get("poster_id"))).get();
        user.getPosts().add(post);
        postService.createPost(post);
        return ResponseEntity.created(uri).body(post);
    }

    @GetMapping("/posts/getUserPosts")
    public ResponseEntity<?> getPostsFromUser(@RequestParam Map<String, String> myMap)
    {
        if(myMap != null && myMap.containsKey("user_id"))
        {
            Long user_id = Long.valueOf(myMap.get("user_id"));
            if(user_id > 0)
            {
                User user = userRepository.findById(user_id).get();
                return ResponseEntity.ok().body(user.getPosts());
            }
        }
        return ResponseEntity.badRequest().body("user_id was null");
    }

    @PostMapping("posts/likePost")
    public ResponseEntity<?> likePost(@RequestParam Map<String, String> myMap)
    {
        if(myMap != null && myMap.containsKey("post_id"))
        {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/likePost").toUriString());
            Long post_id = Long.valueOf(myMap.get("post_id"));
            Post post = postService.getPost(post_id);
            postService.likePost(post);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("post_id was null");
    }

    @PostMapping("posts/dislikePost")
    public ResponseEntity<?> dislikePost(Post post)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/dislikePost").toUriString());
        postService.dislikePost(post);
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("posts/savePost")
    public ResponseEntity<?> savePost(Post post)
    {
        if(!postTextVerification.checkPostText(post.getPost_text()))
        {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Post text length failed preconditions.");
        }

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/savePost").toUriString());
        return ResponseEntity.created(uri).body(postService.createPost(post));
    }

    @PostMapping("posts/updatePostText")
    public ResponseEntity<?> updatePostText(Post post, String text)
    {
        if(!postTextVerification.checkPostText(text))
        {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Post text length failed preconditions.");
        }
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/updatePostText").toUriString());
        postService.updatePostText(post, text);
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("posts/deletePost")
    public ResponseEntity<?> deletePost(Post post)
    {
        postService.deletePost(post);
        return ResponseEntity.accepted().build();
    }

}
