package com.mantarays.socialbackend.VerificationServices;

public class RecoveryQuestionVerification
{
    public boolean verifyRecoveryQuestionAnswer(String answer)
    {
        return answer.length() > 0 && answer.length() < 255;
    }
}
