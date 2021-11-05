package com.mantarays.socialbackend.Models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    private String email;

    private boolean logged_in;

    @OneToMany
    private List<Post> posts = new ArrayList<Post>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<Role>();

    @ManyToMany
    private List<User> friends = new ArrayList<User>();
}
