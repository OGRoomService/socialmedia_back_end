package com.mantarays.socialbackend.Services;

import java.util.List;

import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.User;

public interface UserServiceIntf 
{
    User createUser(User user);
    User getUser(String username);

    Role saveRole(Role role);
    void saveUser(User user);

    void updatePassword(User user, String password);
    void updateUsername(User user, String username);
    void updateEmail(User user, String email);
    void addRoleToUser(String username, String roleName);
    
    List<User> getUsers();
}
