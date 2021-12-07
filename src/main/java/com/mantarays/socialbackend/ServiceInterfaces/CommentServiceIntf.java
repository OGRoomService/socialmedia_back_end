package com.mantarays.socialbackend.ServiceInterfaces;

import com.mantarays.socialbackend.Models.Comment;

public interface CommentServiceIntf
{
    Comment getCommentById(String id);
    boolean deleteCommentById(String id);
    void createComment(Comment comment);
    void likeComment(Comment comment);
    void unlikeComment(Comment comment);
}
