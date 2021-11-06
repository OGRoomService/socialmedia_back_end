package com.mantarays.socialbackend.VerificationServices;

import com.mantarays.socialbackend.Forms.UserFailureStringsForm;
import com.mantarays.socialbackend.Services.UserService;

import java.util.Map;

public class UserVerification
{
    private final UserService userService;
    private final UsernameVerification usernameVerification;
    private final PasswordVerification passwordVerification;
    private final EmailVerification emailVerification;

    public UserVerification(final UserService userService,
                            final UsernameVerification usernameVerification,
                            final PasswordVerification passwordVerification,
                            final EmailVerification emailVerification)
    {
        this.userService = userService;
        this.usernameVerification = usernameVerification;
        this.passwordVerification = passwordVerification;
        this.emailVerification = emailVerification;
    }

    public boolean doesAccountPassPreconditions(Map<String, String> inAccount, UserFailureStringsForm failureStrings)
    {
        boolean allPass = true;
        if(userService.doesEmailExist(inAccount.get("email")))
        {
            failureStrings.emailFailureString = "Email already exists";
            allPass = false;
        }
        if(userService.doesUsernameExist(inAccount.get("username")))
        {
            failureStrings.usernameFailureString = "Username already exists";
            allPass = false;
        }
        if(!usernameVerification.checkUsername(inAccount.get("username")))
        {
            failureStrings.usernameFailureString = "Username failed preconditions";
            allPass = false;
        }
        if(!passwordVerification.checkPassword(inAccount.get("password")))
        {
            failureStrings.passwordFailureString = "Password failed preconditions";
            allPass = false;
        }
        if(!emailVerification.checkEmail(inAccount.get("email")))
        {
            failureStrings.emailFailureString = "Email failed preconditions";
            allPass = false;
        }
        return allPass;
    }
}
