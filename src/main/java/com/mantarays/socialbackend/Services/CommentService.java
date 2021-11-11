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
public class CommentService implements CommentServiceIntf
{
    private final CommentRepository commentRepository;

    @Override
    public void createComment(Comment comment)
    {
        commentRepository.save(comment);
    }
}
