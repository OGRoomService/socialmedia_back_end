package com.mantarays.socialbackend.VerificationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.mantarays.socialbackend.VerificationServices.CommentVerification;


public class CommentVerificationTest 
{
    CommentVerification commentVerification = new CommentVerification();

    @Test
    void givenACommentTextLengthLessThanOne_WhenCheckPostTextCalled_ThenReturnFalse()
    {
        assertEquals(false, commentVerification.checkCommentText(""));
    }

    @Test
    void givenACommentTextLengthGreaterThan255_WhenCheckPostTextCalled_ThenReturnFalse()
    {
        //Each line of 'A' is 100 'A's, therefore there are 300 'A's
        assertEquals(false, commentVerification.checkCommentText("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                                                             + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                                                             + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
    }

    @Test
    void givenACommentTextLengthMoreThanOneButLessThan255_WhenCheckPostTextCalled_ThenReturnTrue()
    {
        assertEquals(true, commentVerification.checkCommentText("A normal post!"));
    }
}
