package com.mantarays.socialbackend.ServiceInterfaces;

import java.util.List;

import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.User;

public interface UserServiceIntf
{
    //User commands
    User createUser(User user);
    User getUser(String username);
    User getUserFromID(String id);
    User getUserFromEmail(String email);
    User getUserFromUsername(String username);
    User getUserFromPasswordResetToken(String passwordResetToken);
    //void saveUser(User user);

    //Update commands
    void updatePassword(User user, String password);
    void updateUsername(User user, String username);
    void updateEmail(User user, String email);
    void updatePasswordResetToken(User user, String passwordResetToken);
    void updateProfilePicture(User user, String linkToProfilePicture);
    void updateLoggedIn(User user, boolean loggedIn);

    //Role commands
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);

    //Friendlist stuff
    void addUserToFriendsList(User user, User newFriend);
    void removeUserFromFriendsList(User user, User oldFriend);

    List<User> getUsers();
}
