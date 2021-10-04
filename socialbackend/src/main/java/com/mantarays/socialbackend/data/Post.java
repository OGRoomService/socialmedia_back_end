package com.mantarays.socialbackend.data;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Post 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    private String posters_username;

    private String post_text;

    private String post_picture;

    //Not sure if this is gonna work? TemporalType.TIMESTAMP generates a Date I THINK...
    @Temporal(TemporalType.TIMESTAMP)
    private Date post_date;

    private int likes;

    private int dislikes;


    /**
     * Getters
     */
    public String getPostersUsername()
    {
        return this.posters_username;
    }

    public String getPostText()
    {
        return this.post_text;
    }

    public String getPostPicture()
    {
        return this.post_picture;
    }

    public Date getPostDate()
    {
        return this.post_date;
    }

    public int getLikes()
    {
        return this.likes;
    }

    public int getDislikes()
    {
        return this.dislikes;
    }

    /**
     * Setters
     */
    public void setPostersUsername(String username)
    {
        this.posters_username = username;
    }

    public void setPostText(String text)
    {
        this.post_text = text;
    }

    public void setPostPicture(String picture)
    {
        this.post_picture = picture;
    }

    //Im not sure if we want this since its generated?
    public void setPostDate(Date date)
    {
        this.post_date = date;
    }

    /**
     * Adding/removing likes/dislikes
     */
    public void incrementLikes()
    {
        this.likes++;
    }

    public void decrementLikes()
    {
        this.likes--;
    }

    public void incrementDislikes()
    {
        this.dislikes++;
    }

    public void decrementDislikes()
    {
        this.dislikes--;
    }
}
