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
    public User createUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public void saveUser(User user)
    {
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
        User user = userRepo.findByUsername(username);
        return user;
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
        user.getRoles().forEach(role ->
        {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public boolean doesEmailExist(String email)
    {
        try
        {
            User user = loadUserByEmail(email);
            return true;
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

    public boolean doesRoleExist(String inrole)
    {
        Role role = roleRepo.findByName(inrole);
        if(role != null)
            return true;
        return false;
    }

    public User loadUserByEmail(String email) throws UsernameNotFoundException
    {
        User user = userRepo.findByEmail(email);
        if(user == null)
        {
            String error = "User not found in database.";
            log.error(error);
            throw new UsernameNotFoundException(error);
        }
        return user;
    }

    @Override
    public void deleteAll()
    {
        userRepo.deleteAll();
    }

    @Override
    public void updateEmail(User user, String email)
    {
        user.setEmail(email);
        userRepo.save(user);
    }

    @Override
    public void updateProfilePicture(User user, String linkToProfilePicture)
    {
        user.setProfilePictureLink(linkToProfilePicture);
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
        user.getFriends().add(newFriend);
    }

    @Override
    public void removeUserFromFriendsList(User user, User oldFriend)
    {
        user.getFriends().remove(oldFriend);
    }
}
