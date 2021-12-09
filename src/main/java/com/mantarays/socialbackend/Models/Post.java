package com.mantarays.socialbackend.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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
    private Long post_id;

    @NonNull
    @Column(name = "poster_id")
    private Long posterId;

    private Long original_poster_id;

    @NonNull
    private String post_text;

    private String postPictureLink;

    private int likes;

    @ElementCollection
    private List<Long> usersThatLiked = new ArrayList<Long>();

    private int dislikes;

    @ElementCollection
    private List<Long> usersThatDisliked = new ArrayList<Long>();

    @NonNull
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> post_comments = new ArrayList<Comment>();

    @NonNull
    @CreatedDate
    @Column(name = "post_date", nullable = false, updatable = false)
    private Date postDate = new Date();
}
