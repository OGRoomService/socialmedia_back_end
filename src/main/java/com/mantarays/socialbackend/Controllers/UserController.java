package com.mantarays.socialbackend.Controllers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantarays.socialbackend.Forms.StandardReturnForm;
import com.mantarays.socialbackend.Forms.UserFailureStringsForm;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Services.UserService;
import com.mantarays.socialbackend.Utilities.EmailUtility;
import com.mantarays.socialbackend.Utilities.PictureUploadingUtility;
import com.mantarays.socialbackend.Utilities.TokenUtility;
import com.mantarays.socialbackend.VerificationServices.*;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
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
    private final AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtility tokenUtility;

    @Autowired
    private EmailUtility emailUtility;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers()
    {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/users/get_self")
    public ResponseEntity<?> getOwnUser(@RequestHeader("Authorization") String tokenHeader)
    {
        String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
        return ResponseEntity.ok().body(userService.getUserFromUsername(tokenUtility.getUsernameFromToken(accessToken)));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/forgot_password")
    public ResponseEntity<?> forgotPassword(HttpServletRequest request, @RequestBody Map<String, String> myMap) throws UnsupportedEncodingException, MessagingException
    {
        User user = userService.getUserFromEmail(myMap.get("email"));
        String passwordToken = RandomString.make(45);
        userService.updatePasswordResetToken(user, passwordToken);

        emailUtility.sendEmail("Password Reset Link", request.getRequestURL().toString() + "/reset_password_token?token=" + passwordToken, "lehquack@gmail.com");

        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/reset_password")
    public ResponseEntity<?> resetPassword(@RequestParam(value = "password_reset_token") String token)
    {
        User user = userService.getUserFromPasswordResetToken(token);
        userService.updatePassword(user, "Password1234!");
        userService.updatePasswordResetToken(user, null);

        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String tokenHeader)
    {
        String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
        User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(accessToken));
        userService.updateLoggedIn(user, false);
        return ResponseEntity.ok().body("Successfully logged out.");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/create")
    public ResponseEntity<?> saveUser(@RequestBody Map<String, String> myMap)
    {
        UserFailureStringsForm failureStrings = new UserFailureStringsForm();
        StandardReturnForm returnForm = new StandardReturnForm();
        UserVerification userVerification = new UserVerification(userService, usernameVerification, passwordVerification, emailVerification);

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
    @PostMapping("users/update_profile_picture")
    public ResponseEntity<?> updateProfilePicture(@RequestHeader("Authorization") String tokenHeader,
                                                  @RequestParam("image") MultipartFile multipartFile) throws IOException
    {
        String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
        User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(accessToken));

        if(multipartFile.getOriginalFilename() != null)
        {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "user-photos/" + user.getId() + "/profile-pictures";
            PictureUploadingUtility.savePicture(uploadDir, fileName, multipartFile);

            userService.updateProfilePicture(user, "/user-photos/" + user.getId() + "/profile-pictures/" + fileName);

            return ResponseEntity.ok().body("Updated profile picture.");
        }
        return ResponseEntity.ok().body("Failed to update profile picture.");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "users/get_profile_picture", produces= MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> getProfilePicture(@RequestHeader("Authorization") String tokenHeader) throws IOException
    {
        String token = tokenUtility.getTokenFromHeader(tokenHeader);
        User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(token));
        if(user.getProfilePictureLink() != null)
        {
            String formattedDir = user.getProfilePictureLink().replace("/", "\\");
            File img = new File(System.getProperty("user.dir") + formattedDir);
            return ResponseEntity.ok()
                .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img)))
                .body(Files.readAllBytes(img.toPath()));
        }
        else
        {
            return ResponseEntity.badRequest().body("Failed to get user profile picture");
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/add_friend")
    public ResponseEntity<?> addFriend(@RequestHeader("Authorization") String tokenHeader,
                                       @RequestBody Map<String, String> myMap)
    {
        String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
        User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(accessToken));
        if(myMap.containsKey("user_id"))
        {
            User otherUser = userService.getUserFromID(myMap.get("user_id"));
            userService.addUserToFriendsList(user, otherUser);
            userService.addUserToFriendsList(otherUser, user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/delete_friend")
    public ResponseEntity<?> deleteFriend(@RequestHeader("Authorization") String tokenHeader,
                                       @RequestBody Map<String, String> myMap)
    {
        String accessToken = tokenUtility.getTokenFromHeader(tokenHeader);
        User user = userService.getUserFromUsername(tokenUtility.getUsernameFromToken(accessToken));
        if(myMap.containsKey("user_id"))
        {
            User otherUser = userService.getUserFromID(myMap.get("user_id"));
            userService.removeUserFromFriendsList(user, otherUser);
            userService.removeUserFromFriendsList(otherUser, user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/users/login")
    public ResponseEntity<?> overloadedLogin(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        try
        {
            @SuppressWarnings("unchecked")
            Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);

            User user = null;

            String username = requestMap.get("username");
            String password = requestMap.get("password");

            if(username.contains("@"))
            {
                user = userService.getUserFromEmail(username);
                username = user.getUsername();
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            TokenUtility tokenUtility = new TokenUtility();
            try
            {
                Map<String, String> tokens = tokenUtility.generateNewUserTokens(request, authenticationManager.authenticate(authenticationToken));
                if(user != null)
                {
                    userService.updateLoggedIn(user, true);
                }
                return ResponseEntity.ok().body(tokens);

            }
            catch(BadCredentialsException e)
            {
                return ResponseEntity.badRequest().body("Unable to login with the given credentials.");
            }
        }
        catch(IOException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
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
                Map<String, String> tokens = tokenUtility.generateNewAccessTokenFromRefreshToken(request, userService);

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
