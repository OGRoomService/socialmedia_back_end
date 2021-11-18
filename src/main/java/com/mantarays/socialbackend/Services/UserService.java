package com.mantarays.socialbackend.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Repositories.RoleRepository;
import com.mantarays.socialbackend.Repositories.UserRepository;
import com.mantarays.socialbackend.ServiceInterfaces.UserServiceIntf;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserServiceIntf, UserDetailsService
{
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role)
    {
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName)
    {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);

        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username)
    {
        return userRepo.findByUsername(username);
    }

    @Override
    public User getUserFromID(String id)
    {
        if(userRepo.findById(Long.valueOf(id)).isPresent())
            return userRepo.findById(Long.valueOf(id)).get();
        else
            return null;
    }

    @Override
    public User getUserFromEmail(String email)
    {
        return userRepo.findByEmail(email);
    }

    @Override
    public User getUserFromUsername(String username)
    {
        return userRepo.findByUsername(username);
    }

    @Override
    public User getUserFromPasswordResetToken(String passwordResetToken)
    {
        return userRepo.findByPasswordResetToken(passwordResetToken);
    }

    @Override
    public List<User> getUsers()
    {
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepo.findByUsername(username);
        if(user == null)
        {
            String error = "User not found in database.";
            log.error(error);
            throw new UsernameNotFoundException(error);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public boolean doesEmailExist(String email)
    {
        try
        {
            User user = getUserFromEmail(email);
            return user != null;
        }
        catch(UsernameNotFoundException e)
        {
            return false;
        }
    }

    public boolean doesUsernameExist(String username)
    {
        try
        {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) loadUserByUsername(username);
            return true;
        }
        catch(UsernameNotFoundException e)
        {
            return false;
        }
    }

    @Override
    public void updateEmail(User user, String email)
    {
        user.setEmail(email);
        userRepo.save(user);
    }

    @Override
    public void updatePasswordResetToken(User user, String passwordResetToken)
    {
        user.setPasswordResetToken(passwordResetToken);
        userRepo.save(user);
    }

    @Override
    public void updateProfilePicture(User user, String linkToProfilePicture)
    {
        user.setProfilePictureLink(linkToProfilePicture);
        userRepo.save(user);
    }

    @Override
    public void updateLoggedIn(User user, boolean loggedIn)
    {
        user.setLogged_in(loggedIn);
        userRepo.save(user);
    }

    @Override
    public void updateUsername(User user, String username)
    {
        user.setUsername(username);
        userRepo.save(user);
    }

    @Override
    public void updatePassword(User user, String password)
    {
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
    }

    @Override
    public void addUserToFriendsList(User user, User newFriend)
    {
        if(user.getId() != newFriend.getId() && !(user.getFriends().contains(newFriend)))
        {
            user.getPotentialFriends().add(newFriend);

            if(newFriend.getPotentialFriends().contains(user))
            {
                newFriend.getPotentialFriends().remove(user);
                user.getPotentialFriends().remove(newFriend);
                newFriend.getFriends().add(user);
                user.getFriends().add(newFriend);
            }
        }
    }

    @Override
    public void removeUserFromFriendsList(User user, User oldFriend)
    {
        user.getFriends().remove(oldFriend);
        oldFriend.getFriends().remove(user);
    }

    @Override
    public List<User> getPotentialFriendsThatContain(User user)
    {
        return userRepo.findAllByPotentialFriendsContains(user);
    }

    @Override
    public void removeFromPotentialFriends(User otherUser, User user)
    {
        otherUser.getPotentialFriends().remove(user);
        userRepo.save(user);
    }
}
