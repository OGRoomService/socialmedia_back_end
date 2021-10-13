package com.mantarays.socialbackend.Services;

import javax.transaction.Transactional;

import com.mantarays.socialbackend.Models.Comment;
import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Repositories.PostRepository;
import com.mantarays.socialbackend.ServiceInterfaces.PostServiceIntf;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService implements PostServiceIntf
{
    private final PostRepository postRepo;

    @Override
    public Post createPost(Post post)
    {
        log.info("Adding post: {}, to database", post.getPost_id());
        return postRepo.save(post);
    }

    @Override
    public Post getPost(Long post_id)
    {
        Post post = postRepo.getById(post_id);
        log.info("Returning post: {}, to database", post.getPost_id());
        return post;
    }

    @Override
    public Post savePost(Post post)
    {
        log.info("Saving post: {}, to database", post.getPost_id());
        return postRepo.save(post);
    }

    @Override
    public Post sharePost(Post post, User user)
    {
        /**
         * TODO This will like need to take a Post type, and a User type
         * then the user can call user.Addpost(Post) or something idk...
         * We will be working on this much later...
         *
         * Putting in what i THINK would share them...
         */
        Post new_post = post;
        new_post.setOriginal_poster_id(post.getPoster_id());
        new_post.setPoster_id(user.getId());
        postRepo.save(new_post);
        return null;
    }

    @Override
    public void updatePostText(Post post, String comment)
    {
        post.setPost_text(comment);
    }

    @Override
    public void likePost(Post post)
    {
        post.setLikes(post.getLikes() + 1);
    }

    @Override
    public void dislikePost(Post post)
    {
        post.setDislikes(post.getDislikes() + 1);
    }

    @Override
    public void commentPost(Post post, Comment comment)
    {
        //post.getPost_comments().add(comment);
    }

    @Override
    public void deletePost(Post post)
    {
        postRepo.delete(post);
    }

}
