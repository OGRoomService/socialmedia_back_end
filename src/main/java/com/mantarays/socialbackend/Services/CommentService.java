package com.mantarays.socialbackend.Services;

import com.mantarays.socialbackend.Models.Comment;
import com.mantarays.socialbackend.Repositories.CommentRepository;
import com.mantarays.socialbackend.ServiceInterfaces.CommentServiceIntf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService implements CommentServiceIntf {
    private final CommentRepository commentRepository;

    @Override
    public Comment getCommentById(String id) {
        try {
            Long lId = Long.parseLong(id);

            return commentRepository.getById(lId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteCommentById(String id) {
        try {
            System.out.println("Deleting comment");
            Long lId = Long.parseLong(id);
            commentRepository.deleteById(lId);
            System.out.println("deleted comment");


            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void likeComment(Comment comment) {
        comment.setLikes(comment.getLikes() + 1);
        commentRepository.save(comment);
    }

    @Override
    public void unlikeComment(Comment comment) {
        comment.setLikes(comment.getLikes() - 1);
        commentRepository.save(comment);
    }
}
