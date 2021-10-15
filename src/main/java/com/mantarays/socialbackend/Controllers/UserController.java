package com.mantarays.socialbackend.Controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mantarays.socialbackend.Forms.RoleToUserForm;
import com.mantarays.socialbackend.Forms.UserFailureStringsForm;
import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Repositories.RoleRepository;
import com.mantarays.socialbackend.Services.UserService;
import com.mantarays.socialbackend.VerificationServices.EmailVerification;
import com.mantarays.socialbackend.VerificationServices.PasswordVerification;
import com.mantarays.socialbackend.VerificationServices.UsernameVerification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final RoleRepository roleRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers()
    {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/users/create")
    public ResponseEntity<?> saveUser(@RequestParam Map<String, String> myMap)
    {
        boolean conditionalPassed;
        UserFailureStringsForm upForm;
        ResponseEntity<?> errorResponse;
        URI uri;

        upForm = new UserFailureStringsForm();
        conditionalPassed = true;

        try //This checks if user already exists
        {
            UserDetails user = userService.loadUserByUsername(myMap.get("username"));
            upForm.usernameFailureString = "Username already exists.";
            conditionalPassed = false;
        }
        catch(UsernameNotFoundException e)
        {
            //continue
        }

        if(!usernameVerification.checkUsername(myMap.get("username")))
        {
            upForm.usernameFailureString = "Username failed preconditions";
            conditionalPassed = false;
        }
        if(!passwordVerification.checkPassword(myMap.get("password")))
        {
            upForm.passwordFailureString = "Password failed preconditions";
            conditionalPassed = false;
        }
        if(!emailVerification.checkEmail(myMap.get("email")))
        {
            upForm.emailFailureString = "Email failed preconditions";
            conditionalPassed = false;
        }

        if(!conditionalPassed)
        {
            errorResponse = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(upForm);
            return errorResponse;
        }

        uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/create").toUriString());
        User user = new User(0,
                                myMap.get("username"),
                                myMap.get("password"),
                                myMap.get("email"),
                        false,
                                new ArrayList<Post>(),
                                new ArrayList<Role>(),
                                new ArrayList<User>() );
        userService.createUser(user);
        userService.addRoleToUser(user.getUsername(), "ROLE_USER");
        return ResponseEntity.created(uri).body("OK");
    }

    @PostMapping("/users/save")
    public ResponseEntity<?> saveUser(@RequestBody User user)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body("Saved user account");
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @GetMapping("/users/deleteAll")
    public ResponseEntity<?> deleteAll()
    {
        userService.deleteAll();
        return ResponseEntity.ok().body("Deleted all user accounts...");
    }

    @PostMapping("users/update_email")
    public ResponseEntity<?> updateEmail(@RequestParam Map<String, String> myMap)
    {
        boolean validEmail = emailVerification.checkEmail(myMap.get("email"));
        if(!validEmail)
        {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Email doesnt meet preconditions");
        }
        userService.updateEmail(userService.getUser(myMap.get("username")), myMap.get("email"));
        return ResponseEntity.ok().body("Updated user email.");
    }

    @PostMapping("users/update_username")
    public ResponseEntity<?> updateUsername(@RequestParam Map<String, String> myMap)
    {
        boolean validNewUsername = usernameVerification.checkUsername(myMap.get("new_username"));
        if(!validNewUsername)
        {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Username doesnt meet preconditions");
        }
        userService.updateUsername(userService.getUser(myMap.get("username")), myMap.get("new_username"));
        return ResponseEntity.ok().body("Updated username.");
    }

    @PostMapping("users/update_password")
    public ResponseEntity<?> updatePassword(@RequestParam Map<String, String> myMap)
    {
        boolean validNewPassword = passwordVerification.checkPassword(myMap.get("password"));
        if(!validNewPassword)
        {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Password doesnt meet preconditions");
        }
        userService.updatePassword(userService.getUser(myMap.get("username")), myMap.get("password"));
        return ResponseEntity.ok().body("Updated password.");
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form)
    {
        userService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }
}
