package com.mantarays.socialbackend.UserVerificationTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mantarays.socialbackend.service.UsernameVerification;

public class UsernameVerificationTest
{
    UsernameVerification myUsernameVerifier = new UsernameVerification();

    @Test
    void givenAUsernameLessThan6Characters_WhenCheckUsernameCalled_ThenReturnFalse() 
    {
        assertEquals(myUsernameVerifier.checkUsername("short"), false);
    }

    @Test
    void givenAUsernameEqualTo6Characters_WhenCheckUsernameCalled_ThenReturnFalse() 
    {
        assertEquals(myUsernameVerifier.checkUsername("short"), false);
    }

    @Test
    void givenAUsernameEqualTo6CharactersWithASymbol_WhenCheckUsernameCalled_ThenReturnFalse() 
    {
        assertEquals(myUsernameVerifier.checkUsername("short!@#"), false);
    }

    @Test
    void givenAUsernameGreater6CharactersWithASymbol_WhenCheckUsernameCalled_ThenReturnFalse() 
    {
        assertEquals(myUsernameVerifier.checkUsername("Username111!"), false);
    }

    @Test
    void givenAUsernameEqualTo6CharactersWithADigit_WhenCheckUsernameCalled_ThenReturnTrue() 
    {
        assertEquals(myUsernameVerifier.checkUsername("Username1"), true);
    }
}
