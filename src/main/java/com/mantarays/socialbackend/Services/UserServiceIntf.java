package com.mantarays.socialbackend.Services;

import java.util.List;

import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.User;

public interface UserServiceIntf 
{
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
}
