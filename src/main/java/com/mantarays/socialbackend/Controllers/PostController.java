package com.mantarays.socialbackend.Controllers;

import java.net.URI;
import java.util.*;

import com.mantarays.socialbackend.Models.Comment;
import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Services.CommentService;
import com.mantarays.socialbackend.Services.PostService;
import com.mantarays.socialbackend.Services.UserService;
import com.mantarays.socialbackend.Utilities.TokenUtility;
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
    private final CommentService commentService;
    private final PostTextVerification postTextVerification;
    private final TokenUtility tokenUtility;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/posts/create")
    public ResponseEntity<?> createPost(@RequestHeader("Authorization") String tokenHeader, @RequestBody Map<String, String> myMap)
    {
        String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/posts/createpost").toUriString());
        User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(accessToken));
        Post post = new Post(user.getId(), myMap.get("post_text"));

        post.setOriginal_poster_id(user.getId());
        user.getPosts().add(post);
        postService.createPost(post);
        return ResponseEntity.created(uri).body(post);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/posts/get_posts_from_id")
    public ResponseEntity<?> getPostsFromUserID(@RequestBody Map<String, String> myMap)
    {
        if(myMap != null && myMap.containsKey("user_id"))
        {
            User user = userService.getUserFromID(myMap.get("user_id"));
            return ResponseEntity.ok().body(user.getPosts());
        }
        return ResponseEntity.badRequest().body("user_id was null");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/posts/get_posts_from_friends")
    public ResponseEntity<?> getPostsFromFriends(@RequestHeader("Authorization") String tokenHeader)
    {
        String token = tokenUtility.getTokenFromHeader(tokenHeader);
        User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(token));
        List<User> friendsList = user.getFriends();

        long DAY_IN_MS = 1000 * 60 * 60 * 24;

        List<Post> friendsListPosts = new ArrayList<>();

        for(User friend : friendsList)
        {
            System.out.println("Grabbing all posts from: " + friend.getUsername());
            for(Post post : friend.getPosts())
            {
                if(post.getPost_date().after(new Date(System.currentTimeMillis() - (7 * DAY_IN_MS))))
                {
                    if(friendsListPosts.size() < 15)
                    {
                        friendsListPosts.add(post);
                    }
                }
            }
        }
        friendsListPosts.sort(Comparator.comparing(Post::getPost_date));
        Collections.reverse(friendsListPosts);
        return ResponseEntity.ok().body(friendsListPosts);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/posts/get_posts")
    public ResponseEntity<?> getPosts()
    {
        return ResponseEntity.ok().body(postService.getAllPosts());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("posts/like_post")
    public ResponseEntity<?> likePost(@RequestHeader("Authorization") String tokenHeader,  @RequestBody Map<String, String> myMap)
    {
        if(myMap.containsKey("post_id"))
        {
            String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
            User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(accessToken));

            Long post_id = Long.valueOf(myMap.get("post_id"));
            Post post = postService.getPost(post_id);

            if(post != null)
            {
                Map<String, String> response = new HashMap<String, String>();

                if(post.getUsersThatLiked().contains(user.getId()))
                {
                    post.getUsersThatLiked().remove(user.getId());
                    response.put("liked", "" + false);
                    postService.unlikePost(post);
                }
                else
                {
                    post.getUsersThatLiked().add(user.getId());
                    response.put("liked", "" + true);
                    postService.likePost(post);
                }

                response.put("likes", "" + post.getUsersThatLiked().size());
                return ResponseEntity.ok().body(response);
            }
            return ResponseEntity.badRequest().body("No post with such ID");
        }
        return ResponseEntity.badRequest().body("post_id was not given.");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("posts/dislike_post")
    public ResponseEntity<?> dislikePost(@RequestHeader("Authorization") String tokenHeader,  @RequestBody Map<String, String> myMap)
    {
        if(myMap.containsKey("post_id"))
        {
            String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
            User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(accessToken));

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

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("posts/comment_on_post")
    public ResponseEntity<?> commentOnPost(@RequestHeader("Authorization") String tokenHeader,
                                           @RequestBody Map<String, String> myMap)
    {
        User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(tokenUtility.getTokenFromHeader(tokenHeader)));
        Post post = postService.getPost(Long.valueOf(myMap.get("post_id")));

        Comment newComment = new Comment(user.getId(), myMap.get("comment_text"));

        commentService.createComment(newComment);
        postService.commentPost(post, newComment);

        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("posts/update_post_text")
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

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("posts/delete_post")
    public ResponseEntity<?> deletePost(Post post)
    {
        postService.deletePost(post);
        return ResponseEntity.accepted().build();
    }

}
