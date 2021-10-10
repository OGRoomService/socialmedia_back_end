package com.mantarays.socialbackend.Models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    //Comments can have comments...
    private List<Comment> comments;
}
