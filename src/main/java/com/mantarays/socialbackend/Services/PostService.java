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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService implements PostServiceIntf {
    private final PostRepository postRepo;

    @Override
    public boolean deleteComment(Post post, Comment comment) {
        try {
            post.getPost_comments().remove(comment);
            postRepo.save(post);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Post createPost(Post post) {
        return postRepo.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    @Override
    public Post getPostById(String id) {
        try {
            Long pId = Long.parseLong(id);

            return postRepo.getById(pId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Post getPost(Long post_id) {
        if (postRepo.existsById(post_id)) {
            return postRepo.getById(post_id);
        }
        return null;
    }

    @Override
    public Post savePost(Post post) {
        return postRepo.save(post);
    }

    @Override
    public Post sharePost(Post post, User user) {
        return null;
    }

    @Override
    public void updatePostText(Post post, String comment) {
        post.setPost_text(comment);
        postRepo.save(post);
    }

    @Override
    public void likePost(Post post) {
        post.setLikes(post.getLikes() + 1);
        postRepo.save(post);
    }

    @Override
    public void unlikePost(Post post) {
        post.setLikes(post.getLikes() - 1);
        postRepo.save(post);
    }

    @Override
    public void dislikePost(Post post) {
        post.setDislikes(post.getDislikes() + 1);
        postRepo.save(post);
    }

    @Override
    public void undislikePost(Post post) {
        post.setDislikes(post.getDislikes() - 1);
        postRepo.save(post);
    }

    @Override
    public void commentPost(Post post, Comment comment) {
        post.getPost_comments().add(comment);
        postRepo.save(post);
    }

    @Override
    public boolean deletePost(Post post) {
        try {
            postRepo.delete(post);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deletePostById(String id) {
        try {
            Long pId = Long.parseLong(id);

            postRepo.deleteById(pId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
