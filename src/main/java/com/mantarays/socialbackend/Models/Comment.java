package com.mantarays.socialbackend.Models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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

    @ElementCollection
    private List<Long> usersThatLiked = new ArrayList<Long>();

    private int dislikes;

//    @OneToMany
//    private List<Comment> comments = new ArrayList<Comment>();

    @NonNull
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonIgnore
    private Date comment_date = new Date();
}
