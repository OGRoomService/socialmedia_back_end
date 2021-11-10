package com.mantarays.socialbackend.Repositories;

import com.mantarays.socialbackend.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, Long>
{
}
