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
    private final RecoveryQuestionVerification recoveryQuestionVerification;

    public UserVerification(final UserService userService,
                            final UsernameVerification usernameVerification,
                            final PasswordVerification passwordVerification,
                            final EmailVerification emailVerification,
                            final RecoveryQuestionVerification recoveryQuestionVerification)
    {
        this.userService = userService;
        this.usernameVerification = usernameVerification;
        this.passwordVerification = passwordVerification;
        this.emailVerification = emailVerification;
        this.recoveryQuestionVerification = recoveryQuestionVerification;
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
        if(!recoveryQuestionVerification.verifyRecoveryQuestionAnswer(inAccount.get("recovery_answer_1")))
        {
            failureStrings.recoveryQuestion1FailureString = "Recovery question 1 failed verification.";
            allPass = false;
        }
        if(!recoveryQuestionVerification.verifyRecoveryQuestionAnswer(inAccount.get("recovery_answer_2")))
        {
            failureStrings.recoveryQuestion2FailureString = "Recovery question 2 failed verification.";
            allPass = false;
        }
        if(!recoveryQuestionVerification.verifyRecoveryQuestionAnswer(inAccount.get("recovery_answer_3")))
        {
            failureStrings.recoveryQuestion3FailureString = "Recovery question 3 failed verification.";
            allPass = false;
        }

        return allPass;
    }
}
