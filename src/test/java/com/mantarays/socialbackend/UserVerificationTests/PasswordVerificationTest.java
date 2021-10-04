package com.mantarays.socialbackend.UserVerificationTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mantarays.socialbackend.service.PasswordVerification;

public class PasswordVerificationTest
{
    PasswordVerification myPasswordVerifier = new PasswordVerification();

    @Test
    void givenAPasswordOfLengthLessThanEight_WhenCheckPasswordCalled_ThenReturnFalse() 
    {
        assertEquals(myPasswordVerifier.checkPassword("short"), false);
    }

    @Test
    void givenAPasswordOfLengthEqualToEight_WhenCheckPasswordCalled_ThenReturnFalse() 
    {
        assertEquals(myPasswordVerifier.checkPassword("password"), false);
    }

    @Test
    void givenAPasswordOfLengthEqualToEightWithNumbers_WhenCheckPasswordCalled_ThenReturnFalse() 
    {
        assertEquals(myPasswordVerifier.checkPassword("password1"), false);
    }

    @Test
    void givenAPasswordOfLengthEqualToEightWithSymbols_WhenCheckPasswordCalled_ThenReturnFalse() 
    {
        assertEquals(myPasswordVerifier.checkPassword("password!"), false);
    }

    @Test
    void givenAPasswordOfLengthEqualToEightWithSymbolsAndNumbers_WhenCheckPasswordCalled_ThenReturnTrue() 
    {
        assertEquals(myPasswordVerifier.checkPassword("password1!"), true);
    }
}
