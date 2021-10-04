package com.mantarays.socialbackend.UserVerificationTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mantarays.socialbackend.service.EmailVerification;

public class EmailVerificationTest
{
    EmailVerification myEmailVerifier = new EmailVerification();

    @Test
    void givenJustAUsernameString_WhenCheckEmailCalleD_ThenReturnFalse() 
    {
        assertEquals(myEmailVerifier.checkEmail("email"), false);
    }

    @Test
    void givenAUsernameStringWithAnAtSymbol_WhenCheckEmailCalleD_ThenReturnFalse() 
    {
        assertEquals(myEmailVerifier.checkEmail("email@"), false);
    }

    @Test
    void givenAUsernameStringWithAnAtSymbolAndADomain_WhenCheckEmailCalleD_ThenReturnFalse() 
    {
        assertEquals(myEmailVerifier.checkEmail("email@test"), false);
    }

    @Test
    void givenACorrectEmail_WhenCheckEmailCalleD_ThenReturnTrue() 
    {
        assertEquals(myEmailVerifier.checkEmail("email@test.com"), true);
    }

}
