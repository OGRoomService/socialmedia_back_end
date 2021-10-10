package com.mantarays.socialbackend.VerificationTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mantarays.socialbackend.VerificationServices.UsernameVerification;

public class UsernameVerificationTest
{
    UsernameVerification myUsernameVerifier = new UsernameVerification();

    @Test
    void givenAUsernameLessThan6Characters_WhenCheckUsernameCalled_ThenReturnFalse() 
    {
        assertEquals(false, myUsernameVerifier.checkUsername("short"));
    }

    @Test
    void givenAUsernameEqualTo6Characters_WhenCheckUsernameCalled_ThenReturnFalse() 
    {
        assertEquals(false, myUsernameVerifier.checkUsername("short"));
    }

    @Test
    void givenAUsernameEqualTo6CharactersWithASymbol_WhenCheckUsernameCalled_ThenReturnFalse() 
    {
        assertEquals(false, myUsernameVerifier.checkUsername("short!@#"));
    }

    @Test
    void givenAUsernameGreater6CharactersWithASymbol_WhenCheckUsernameCalled_ThenReturnFalse() 
    {
        assertEquals(false, myUsernameVerifier.checkUsername("Username111!"));
    }

    @Test
    void givenAUsernameEqualTo6CharactersWithADigit_WhenCheckUsernameCalled_ThenReturnTrue() 
    {
        assertEquals(true, myUsernameVerifier.checkUsername("Username1"));
    }
}
