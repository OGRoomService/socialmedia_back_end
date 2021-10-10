package com.mantarays.socialbackend.VerificationTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mantarays.socialbackend.VerificationServices.PasswordVerification;

public class PasswordVerificationTest
{
    PasswordVerification myPasswordVerifier = new PasswordVerification();

    @Test
    void givenAPasswordOfLengthLessThanEight_WhenCheckPasswordCalled_ThenReturnFalse() 
    {
        assertEquals(false, myPasswordVerifier.checkPassword("short"));
    }

    @Test
    void givenAPasswordOfLengthEqualToEight_WhenCheckPasswordCalled_ThenReturnFalse() 
    {
        assertEquals(false, myPasswordVerifier.checkPassword("password"));
    }

    @Test
    void givenAPasswordOfLengthEqualToEightWithNumbers_WhenCheckPasswordCalled_ThenReturnFalse() 
    {
        assertEquals(false, myPasswordVerifier.checkPassword("password1"));
    }

    @Test
    void givenAPasswordOfLengthEqualToEightWithSymbols_WhenCheckPasswordCalled_ThenReturnFalse() 
    {
        assertEquals(false, myPasswordVerifier.checkPassword("password!"));
    }

    @Test
    void givenAPasswordOfLengthEqualToEightWithSymbolsAndNumbers_WhenCheckPasswordCalled_ThenReturnTrue() 
    {
        assertEquals(true, myPasswordVerifier.checkPassword("password1!"));
    }
}
