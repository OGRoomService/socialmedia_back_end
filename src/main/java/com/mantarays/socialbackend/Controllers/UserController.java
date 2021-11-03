package com.mantarays.socialbackend.Controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mantarays.socialbackend.Forms.RoleToUserForm;
import com.mantarays.socialbackend.Forms.StandardReturnForm;
import com.mantarays.socialbackend.Forms.UserFailureStringsForm;
import com.mantarays.socialbackend.Models.RecoveryQuestion;
import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Repositories.RoleRepository;
import com.mantarays.socialbackend.Services.RecoveryQuestionService;
import com.mantarays.socialbackend.Services.UserService;
import com.mantarays.socialbackend.VerificationServices.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    private final UsernameVerification usernameVerification;
    private final PasswordVerification passwordVerification;
    private final EmailVerification emailVerification;
    private final RecoveryQuestionVerification recoveryQuestionVerification;
    private UserVerification userVerification;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers()
    {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/create")
    public ResponseEntity<?> saveUser(@RequestBody Map<String, String> myMap)
    {
        UserFailureStringsForm failureStrings = new UserFailureStringsForm();
        StandardReturnForm returnForm = new StandardReturnForm();
        userVerification = new UserVerification(userService, usernameVerification, passwordVerification, emailVerification, recoveryQuestionVerification);

        if(!userVerification.doesAccountPassPreconditions(myMap, failureStrings))
        {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(failureStrings);
        }
        else
        {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/create").toUriString());
            User user = new User(0,
                                    myMap.get("username"),
                                    myMap.get("password"),
                                    myMap.get("email"),
                                    false,
                                    new ArrayList<Post>(),
                                    new ArrayList<Role>(),
                                    new ArrayList<User>(),
                                    new ArrayList<RecoveryQuestion>());


            userService.createUser(user);

            userService.createRecoveryQuestion(user, myMap.get("recovery_question_1"), myMap.get("recovery_answer_1"));
            userService.createRecoveryQuestion(user, myMap.get("recovery_question_2"), myMap.get("recovery_answer_2"));
            userService.createRecoveryQuestion(user, myMap.get("recovery_question_3"), myMap.get("recovery_answer_3"));

            userService.addRoleToUser(user.getUsername(), "ROLE_USER");

            returnForm.message = "New user added to database.";
            return ResponseEntity.created(uri).body(returnForm);
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/save")
    public ResponseEntity<?> saveUser(@RequestBody User user)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body("Saved user account");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/role/save")
    public ResponseEntity<?> saveRole(@RequestBody Role role)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        StandardReturnForm returnForm = new StandardReturnForm();
        if(userService.doesRoleExist(role.getName()))
        {
            returnForm.message = "Role already exists";
            return ResponseEntity.badRequest().body(returnForm);
        }
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/users/deleteAll")
    public ResponseEntity<?> deleteAll()
    {
        userService.deleteAll();
        return ResponseEntity.ok().body("Deleted all user accounts...");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form)
    {
        userService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }
}
