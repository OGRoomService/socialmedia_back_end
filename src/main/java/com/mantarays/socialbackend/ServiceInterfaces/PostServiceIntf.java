package com.mantarays.socialbackend.ServiceInterfaces;

import com.mantarays.socialbackend.Models.Comment;
import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Models.User;

public interface PostServiceIntf 
{
    Post createPost(Post post);
    Post getPost(Long post_id);
    Post savePost(Post post);
    Post sharePost(Post post, User user);

    void updatePostText(Post post, String post_text);

    void likePost(Post post);
    void dislikePost(Post post);
    void commentPost(Post post, Comment comment);
    void deletePost(Post post);
}
