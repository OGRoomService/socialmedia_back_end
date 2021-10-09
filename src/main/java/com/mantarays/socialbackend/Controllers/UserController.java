package com.mantarays.socialbackend.Controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.RoleToUserForm;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Services.UserService;
import com.mantarays.socialbackend.Services.EmailVerification;
import com.mantarays.socialbackend.Services.PasswordVerification;
import com.mantarays.socialbackend.Services.UsernameVerification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController 
{
    private final UserService userService; 
    private final UsernameVerification usernameVerification;
    private final PasswordVerification passwordVerification;
    private final EmailVerification emailVerification;
  
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers()
    {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/users/create")
    public ResponseEntity<?> saveUser(@RequestParam Map<String, String> myMap)
    {
        UserFailureStrings upForm = new UserFailureStrings();
        boolean usernamePassed = usernameVerification.checkUsername(myMap.get("username"));
        boolean passwordPassed = passwordVerification.checkPassword(myMap.get("password"));
        boolean emailPassed = emailVerification.checkEmail(myMap.get("email"));
        ResponseEntity<?> errorResponse;


        if(!usernamePassed)
        {
            upForm.usernameFailureString = "Username failed preconditions";
        }
        if(!passwordPassed)
        {
            upForm.passwordFailureString = "Password failed preconditions";
        }
        if(!emailPassed)
        {
            upForm.emailFailureString = "Email failed preconditions";
        }

        if(!passwordPassed || !usernamePassed || !emailPassed)
        {
            errorResponse = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(upForm);
            return errorResponse;
        }

        
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/create").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(new User(0, myMap.get("username"), myMap.get("password"), myMap.get("email"), false, new ArrayList<Role>() )));
    }

    @PostMapping("/users/save")
    public ResponseEntity<User> saveUser(@RequestBody User user)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form)
    {
        userService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/deleteAll")
    public ResponseEntity<?> deleteAll()
    {
        userService.deleteAll();
        return ResponseEntity.ok().body("Deleted all user accounts...");
    }

    public class UserFailureStrings
    {
        String usernameFailureString;
        String passwordFailureString;
        String emailFailureString;
    }
}