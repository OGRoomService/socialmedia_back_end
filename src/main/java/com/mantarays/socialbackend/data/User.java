package com.mantarays.socialbackend.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.mantarays.socialbackend.service.UsernameVerification;

@Entity
public class User 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String username;

    private String password;

    @ManyToMany
    private List<User> friendsList;

    private String email;

    private String profilePicture;

    public User() {
        this.username = "";
        this.password = "";
        this.friendsList = new ArrayList<User>();
        this.email = "";
        this.profilePicture = "";
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    /**
     * Getters
     */
    public String getUsername()
    {
        return this.username;
    }

    public String getEmail()
    {
        return this.email;
    }

    public List<User> getFriendsList()
    {
        return this.friendsList;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getProfilePicture()
    {
        return this.profilePicture;
    }


    /**
     * Setters
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setProfilePicture(String profilePicture)
    {
        this.profilePicture = profilePicture;
    }


    /**
     * Friends list functions
     */
    public void addToFriendsList(User user)
    {
        this.friendsList.add(user);
    }

    public void removeFromFriendsList(User user)
    {
        this.friendsList.remove(user);
    }

    public boolean isInFriendsList(User user)
    {
        return this.friendsList.contains(user);
    }

}
