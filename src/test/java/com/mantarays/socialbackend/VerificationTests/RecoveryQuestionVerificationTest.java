package com.mantarays.socialbackend.VerificationTests;

import com.mantarays.socialbackend.VerificationServices.RecoveryQuestionVerification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecoveryQuestionVerificationTest
{
    RecoveryQuestionVerification recoveryQuestionVerification = new RecoveryQuestionVerification();

    @Test
    void givenARecoveryQuestionAnswerThatIsEmpty_WhenVerifyRecoveryQuestionAnswerCalled_ThenReturnFalse()
    {
        assertEquals(false, recoveryQuestionVerification.verifyRecoveryQuestionAnswer(""));
    }

    @Test
    void givenARecoveryQuestionAnswerThatsLengthIsGreaterThanZeroButLessThan255_WhenVerifyRecoveryQuestionAnswerCalled_ThenReturnTrue()
    {
        assertEquals(true, recoveryQuestionVerification.verifyRecoveryQuestionAnswer("Cat"));
    }

    @Test
    void givenARecoveryQuestionAnswerThatsLengthIsGreaterThan255_WhenVerifyRecoveryQuestionAnswerCalled_ThenReturnFalse()
    {
        assertEquals(false, recoveryQuestionVerification.verifyRecoveryQuestionAnswer("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                                                                                            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                                                                                            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
    }

}
