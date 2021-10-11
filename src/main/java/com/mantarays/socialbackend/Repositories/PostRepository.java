package com.mantarays.socialbackend.Repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mantarays.socialbackend.Models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> 
{
    //Post findByPostId(Long post_id);
}
