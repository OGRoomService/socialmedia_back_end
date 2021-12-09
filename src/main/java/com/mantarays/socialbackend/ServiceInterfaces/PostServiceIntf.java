package com.mantarays.socialbackend.ServiceInterfaces;

import com.mantarays.socialbackend.Models.Comment;
import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostServiceIntf
{
    boolean deleteComment(Post post, Comment comment);
    Page<Post> pagePosts(int page);

    Post createPost(Post post);
    Post getPost(Long post_id);
    Post getPostById(String id);
    Post savePost(Post post);
    Post sharePost(Post post, User user);

    void updatePostText(Post post, String post_text);

    void likePost(Post post);
    void unlikePost(Post post);
    void dislikePost(Post post);
    void undislikePost(Post post);
    void commentPost(Post post, Comment comment);
    boolean deletePost(Post post);
    boolean deletePostById(String id);
}
