package com.mantarays.socialbackend.Controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mantarays.socialbackend.Models.Comment;
import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Repositories.UserRepository;
import com.mantarays.socialbackend.Services.PostService;
import com.mantarays.socialbackend.Services.UserService;
import com.mantarays.socialbackend.Utilities.TokenUtility;
import com.mantarays.socialbackend.VerificationServices.PostTextVerification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final TokenUtility tokenUtility;

    @PostMapping("/posts/create")
    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String tokenHeader, @RequestBody Map<String, String> myMap)
    {
        String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/createpost").toUriString());
        User user = userRepository.findByUsername(tokenUtility.getUsernameFromToken(accessToken));
        Post post = new Post(user.getId(), myMap.get("post_text"));

        post.setOriginal_poster_id(user.getId());
        user.getPosts().add(post);
        postService.createPost(post);
        return ResponseEntity.created(uri).body(post);
    }

    @GetMapping("/posts/getposts")
    public ResponseEntity<?> getPostsFromUser(@RequestBody Map<String, String> myMap)
    {
        if(myMap != null && myMap.containsKey("user_id"))
        {
            Long user_id = Long.valueOf(myMap.get("user_id"));
            User user = userRepository.findById(user_id).get();
            return ResponseEntity.ok().body(user.getPosts());
        }
        return ResponseEntity.badRequest().body("user_id was null");
    }

    @PostMapping("posts/likepost")
    public ResponseEntity<?> likePost(@RequestHeader("Authorization") String tokenHeader,  @RequestBody Map<String, String> myMap)
    {
        if(myMap.containsKey("post_id"))
        {
            String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
            User user = userRepository.findByUsername(tokenUtility.getUsernameFromToken(accessToken));

            Long post_id = Long.valueOf(myMap.get("post_id"));
            Post post = postService.getPost(post_id);

            if(post != null)
            {
                if(post.getUsersThatLiked().contains(user.getId()))
                {
                    post.getUsersThatLiked().remove(user.getId());
                    postService.unlikePost(post);
                    return ResponseEntity.ok().body("Removed like from post.");
                }
                else
                {
                    post.getUsersThatLiked().add(user.getId());
                    postService.likePost(post);
                    return ResponseEntity.ok().body("Added like to post.");
                }
            }
            return ResponseEntity.badRequest().body("No post with such ID");
        }
        return ResponseEntity.badRequest().body("post_id was not given.");
    }

    @PostMapping("posts/dislikepost")
    public ResponseEntity<?> dislikePost(@RequestHeader("Authorization") String tokenHeader,  @RequestBody Map<String, String> myMap)
    {
        if(myMap.containsKey("post_id"))
        {
            String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
            User user = userRepository.findByUsername(tokenUtility.getUsernameFromToken(accessToken));

            Long post_id = Long.valueOf(myMap.get("post_id"));
            Post post = postService.getPost(post_id);

            if(post != null)
            {
                if(post.getUsersThatDisliked().contains(user.getId()))
                {
                    post.getUsersThatDisliked().remove(user.getId());
                    postService.undislikePost(post);
                    return ResponseEntity.ok().body("Removed dislike from post.");
                }
                else
                {
                    post.getUsersThatDisliked().add(user.getId());
                    postService.dislikePost(post);
                    return ResponseEntity.ok().body("Added dislike to post.");
                }
            }
            return ResponseEntity.badRequest().body("No post with such ID");
        }
        return ResponseEntity.badRequest().body("post_id was not given.");
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
