package com.mantaray.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Objects;


@Entity
@Table(name = "users")
public class UserModel 
{
    private @Id @GeneratedValue long id;
    private @NotBlank String username;
    private @NotBlank String password;
    private @NotBlank boolean loggedIn; //For future? Maybe seeing what users are online for live chat?
    private String profilePicture;

    public UserModel(){}

    public UserModel(@NotBlank String username, @NotBlank String password)
    {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
        this.profilePicture = null;
    }

    public long getId()
    {
        return this.id;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPicture()
    {
        return this.profilePicture;
    }

    public void setProfilePicture(String profilePicture)
    {
        this.profilePicture = profilePicture;
    }

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    @Override
    public boolean equals(Object o) 
    {
        if (this == o) 
        {
            return true;
        }
        if (!(o instanceof UserModel)) 
        {
            return false;
        }

        UserModel user = (UserModel) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }    
    
    @Override
    public int hashCode() 
    {
        return Objects.hash(id, username, password, loggedIn, profilePicture);
    }    
    
    @Override
    public String toString() 
    {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", password='" + password + '\'' + ", loggedIn=" + loggedIn + '}';
    }
}
