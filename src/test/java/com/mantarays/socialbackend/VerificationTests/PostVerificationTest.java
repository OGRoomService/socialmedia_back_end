package com.mantarays.socialbackend.VerificationTests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mantarays.socialbackend.VerificationServices.PostTextVerification;

public class PostVerificationTest 
{
    PostTextVerification postTextVerification = new PostTextVerification();

    @Test
    void givenAPostTextLengthLessThanOne_WhenCheckPostTextCalled_ThenReturnFalse()
    {
        assertEquals(false, postTextVerification.checkPostText(""));
    }

    @Test
    void givenAPostTextLengthGreaterThan255_WhenCheckPostTextCalled_ThenReturnFalse()
    {
        //Each line of 'A' is 100 'A's, therefore there are 300 'A's
        assertEquals(false, postTextVerification.checkPostText("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                                                             + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                                                             + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
    }

    @Test
    void givenAPostTextLengthMoreThanOneButLessThan255_WhenCheckPostTextCalled_ThenReturnTrue()
    {
        assertEquals(true, postTextVerification.checkPostText("A normal post!"));
    }
}
