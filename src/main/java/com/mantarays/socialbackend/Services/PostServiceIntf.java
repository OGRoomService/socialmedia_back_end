package com.mantarays.socialbackend.Services;

import com.mantarays.socialbackend.Models.Post;

public interface PostServiceIntf 
{
    Post createPost(Post post);
    Post getPost(Long post_id);
    Post savePost(Post post);
    Post sharePost(Post post);

    void updatePostText(String comment);

    void likePost(Post post);
    void dislikePost(Post post);
    void commentPost(String comment);
}
