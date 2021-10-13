package com.mantarays.socialbackend.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;

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

    @OneToMany
    private List<Comment> post_comments = new ArrayList<Comment>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date post_date;
}
