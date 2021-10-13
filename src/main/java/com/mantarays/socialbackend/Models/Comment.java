package com.mantarays.socialbackend.Models;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment
{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long comment_id;

    private String comment_text;

    private int likes;

    private int dislikes;

    @OneToMany
    private List<Comment> comments = new ArrayList<Comment>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date comment_date;
}
