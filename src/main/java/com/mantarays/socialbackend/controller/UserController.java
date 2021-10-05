package com.mantarays.socialbackend.controller;

import java.util.List;

import com.mantarays.socialbackend.data.User;
import com.mantarays.socialbackend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Iterable<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping User create(@RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping("/friend")
    public User addFriend(@RequestParam Long uid1, @RequestParam Long uid2) {
        userService.addFriendToUser(uid1, uid2);
    }

    @RequestMapping("/many")
    public List<User> create(@RequestBody List<User> listToDo) {
        listToDo.forEach(x -> userService.save(x));
        return listToDo;
    }

    @PutMapping("/{id}")
    public User update(@RequestBody User toDo) {
        return userService.save(toDo);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
