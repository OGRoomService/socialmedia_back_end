package com.mantarays.socialbackend.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantarays.socialbackend.Forms.RoleToUserForm;
import com.mantarays.socialbackend.Forms.StandardReturnForm;
import com.mantarays.socialbackend.Forms.UserFailureStringsForm;
import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Models.Post;
import com.mantarays.socialbackend.Repositories.UserRepository;
import com.mantarays.socialbackend.Services.UserService;
import com.mantarays.socialbackend.Utilities.TokenUtility;
import com.mantarays.socialbackend.VerificationServices.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepo;
    private UserVerification userVerification;

    @Autowired
    private TokenUtility tokenUtility;

    @Autowired
    private JavaMailSender emailSender;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers()
    {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/users/getself")
    public ResponseEntity<?> getOwnUser(@RequestHeader("Authorization") String tokenHeader)
    {
        String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
        return ResponseEntity.ok().body(userRepo.findByUsername(tokenUtility.getUsernameFromToken(accessToken)));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token)
    {
        String accessToken = token.substring("Bearer ".length());
        User user = userRepo.findByUsername(tokenUtility.getUsernameFromToken(accessToken));
        user.setLogged_in(false);
        userRepo.save(user);
        return ResponseEntity.ok().body("Successfully logged out.");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/create")
    public ResponseEntity<?> saveUser(@RequestBody Map<String, String> myMap)
    {
        UserFailureStringsForm failureStrings = new UserFailureStringsForm();
        StandardReturnForm returnForm = new StandardReturnForm();
        userVerification = new UserVerification(userService, usernameVerification, passwordVerification, emailVerification);

        if(!userVerification.doesAccountPassPreconditions(myMap, failureStrings))
        {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(failureStrings);
        }
        else
        {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/create").toUriString());
            User user = new User(   myMap.get("username"),
                                    myMap.get("password"),
                                    myMap.get("email"));


            userService.createUser(user);

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

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/sendemail")
    public ResponseEntity<?> sendEmail() throws UnsupportedEncodingException, MessagingException
    {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("RowanSpaceSocial@gmail.com", "Rowanspace Support");
        helper.setTo("Lehquack@gmail.com");

        String subject = "Password reset link.";
        String content = "<p> LETS GOOOOOOO </p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        emailSender.send(message);

        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/login")
    public ResponseEntity<?> overloadedLogin(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        try
        {
            Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);

            User user = null;

            String username = requestMap.get("username");
            String password = requestMap.get("password");
            String email = requestMap.get("email");

            if(email != null)
            {
                user = userService.loadUserByEmail(email);
                username = user.getUsername();
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            TokenUtility tokenUtility = new TokenUtility();
            Map<String, String> tokens = tokenUtility.generateNewUserTokens(request, authenticationManager.authenticate(authenticationToken));

            if(user != null)
            {
                user.setLogged_in(true);
                userRepo.save(user);
            }

            return ResponseEntity.ok().body(tokens);
        }
        catch(IOException e)
        {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/token/refresh")
    public ResponseEntity<?> getNewToken(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            try
            {
                TokenUtility tokenUtility = new TokenUtility();
                Map<String, String> tokens = tokenUtility.generateNewAccessTokenFromRefreshToken(request, userRepo);

                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
            catch(Exception e)
            {
                response.setHeader("error", e.getMessage());
                response.setStatus(403);

                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        else
        {
            throw new RuntimeException("Refresh token is missing");
        }

        return ResponseEntity.ok().build();
    }
}
