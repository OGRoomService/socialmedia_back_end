package com.mantarays.socialbackend.ServiceInterfaces;

import java.util.List;

import com.mantarays.socialbackend.Models.RecoveryQuestion;
import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.User;

public interface UserServiceIntf
{
    //User commands
    User createUser(User user);
    User getUser(String username);
    void saveUser(User user);

    //Update commands
    void updatePassword(User user, String password);
    void updateUsername(User user, String username);
    void updateEmail(User user, String email);

    //Role commands
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);

    //RecoveryQuestion commands
    void addRecoveryQuestionToUser(String username, RecoveryQuestion recoveryQuestion);
    void removeRecoveryQuestionFromUser(String username, RecoveryQuestion recoveryQuestion);

    //Friendlist stuff
    void addUserToFriendsList(User user, User newFriend);
    void removeUserFromFriendsList(User user, User oldFriend);

    //TODO probably remove these...
    List<User> getUsers();
    void deleteAll();
}
