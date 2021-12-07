package com.mantarays.socialbackend.Controllers;

import java.util.HashMap;
import java.util.Map;

import com.mantarays.socialbackend.Models.Comment;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Services.CommentService;
import com.mantarays.socialbackend.Services.UserService;
import com.mantarays.socialbackend.Utilities.TokenUtility;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;
    private final TokenUtility tokenUtility;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("comments/like_comment")
    public ResponseEntity<?> likeComment(@RequestHeader("Authorization") String tokenHeader,
            @RequestBody Map<String, String> myMap) {
        try {
            String token = tokenUtility.getTokenFromHeader(tokenHeader);
            User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(token));
            Comment comment = commentService.getCommentById(myMap.get("comment_id"));
            Map<String, String> response = new HashMap<String, String>();

            if (comment.getUsersThatLiked().contains(user.getId())) {
                comment.getUsersThatLiked().remove(user.getId());
                response.put("liked", "" + true);
                commentService.likeComment(comment);
            } else {
                comment.getUsersThatLiked().add(user.getId());
                response.put("liked", "" + true);
                commentService.unlikeComment(comment);
            }
            response.put("likes", "" + comment.getUsersThatLiked().size());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Query Failed");
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("comments/delete_comment")
    public ResponseEntity<?> deleteComment(@RequestHeader("Authorization") String tokenHeader,
            @RequestBody Map<String, String> myMap) {
        try {
            String token = tokenUtility.getTokenFromHeader(tokenHeader);
            User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(token));
            Comment comment = commentService.getCommentById(myMap.get("comment_id"));
            Map<String, String> response = new HashMap<String, String>();

            /* if (comment.getCommenter_id() != user.getId()) 
                return ResponseEntity.badRequest().body("Query failed"); */
            
            boolean succeed = commentService.deleteCommentById(myMap.get("comment_id"));

            response.put("deleted", "" + succeed);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Query Failed");
        }
    }
}
