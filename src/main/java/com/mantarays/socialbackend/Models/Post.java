package com.mantarays.socialbackend.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long post_id;

    @NonNull
    private long poster_id;

    private long original_poster_id;

    @NonNull
    private String post_text;

    private int likes;

    private int dislikes;

    @NonNull
    @OneToMany
    private List<Comment> post_comments = new ArrayList<Comment>();

    @NonNull
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date post_date = new Date();
}
