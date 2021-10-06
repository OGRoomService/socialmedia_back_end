package com.mantarays.socialbackend.service;

import com.mantarays.socialbackend.data.User;
import com.mantarays.socialbackend.exception.EntityNotFoundException;
import com.mantarays.socialbackend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void addFriendToUser(Long uid1, Long uid2) {
        User user1 = findById(uid1);
        User user2 = findById(uid2);
        
        user1.addToFriendsList(user2);
        user2.addToFriendsList(user1);
        save(user1);
        save(user2);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
