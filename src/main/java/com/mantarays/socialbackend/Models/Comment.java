package com.mantarays.socialbackend.Models;

import javax.persistence.*;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long comment_id;

    @NonNull
    private Long commenter_id;

    @NonNull
    private String comment_text;

    private int likes;

    private int dislikes;

//    @OneToMany
//    private List<Comment> comments = new ArrayList<Comment>();

    @NonNull
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date comment_date = new Date();
}
