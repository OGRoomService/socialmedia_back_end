package com.mantarays.socialbackend.Models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private String username;

    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NonNull
    private String email;

    private boolean logged_in;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password_reset_token;

    @NonNull
    @JsonIgnore
    @OneToMany
    private List<Post> posts = new ArrayList<Post>();

    @NonNull
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<Role>();

    @NonNull
    @JsonIgnore
    @ManyToMany
    private List<User> friends = new ArrayList<User>();
}
