package com.mantarays.socialbackend.VerificationTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mantarays.socialbackend.VerificationServices.EmailVerification;

public class EmailVerificationTest
{
    EmailVerification myEmailVerifier = new EmailVerification();

    @Test
    void givenJustAUsernameString_WhenCheckEmailCalleD_ThenReturnFalse()
    {
        assertEquals(true, myEmailVerifier.checkEmail("email"));
    }

    @Test
    void givenAUsernameStringWithAnAtSymbol_WhenCheckEmailCalleD_ThenReturnFalse()
    {
        assertEquals(false, myEmailVerifier.checkEmail("email@"));
    }

    @Test
    void givenAUsernameStringWithAnAtSymbolAndADomain_WhenCheckEmailCalleD_ThenReturnFalse()
    {
        assertEquals(false, myEmailVerifier.checkEmail("email@test"));
    }

    @Test
    void givenACorrectEmail_WhenCheckEmailCalleD_ThenReturnTrue()
    {
        assertEquals(true, myEmailVerifier.checkEmail("email@test.com"));
    }

}
