package com.mantarays.socialbackend.Models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.GenerationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long post_id;

    private long poster_id;

    private long original_poster_id;

    private String post_text;

    private int likes;

    private int dislikes;

    //private List<Comment> post_comments;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date post_date;
}
