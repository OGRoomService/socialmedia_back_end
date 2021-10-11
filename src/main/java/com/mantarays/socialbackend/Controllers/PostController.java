package com.mantarays.socialbackend.Controllers;

import java.net.URI;

import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Services.PostService;
import com.mantarays.socialbackend.VerificationServices.PostTextVerification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController
{
    private final PostService postService;
    private final PostTextVerification postTextVerification;

    @GetMapping("/posts/getUserPosts")
    public ResponseEntity<?> getPostsFromUser(User user)
    {
        return ResponseEntity.ok().body(user.getPosts());
    }

    @PostMapping("posts/likePost")
    public ResponseEntity<?> likePost(Post post)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/likePost").toUriString());
        postService.likePost(post);
        return ResponseEntity.created(uri).build();
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
