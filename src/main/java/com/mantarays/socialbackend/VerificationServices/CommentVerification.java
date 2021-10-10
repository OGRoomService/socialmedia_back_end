package com.mantarays.socialbackend.VerificationServices;

public class CommentVerification 
{
    /**
     * This verifies that a comment's text can only
     * be between lengths 1-254 so that our database
     * can handle them.
     * @param comment_text
     * @return
     */
    public boolean checkCommentText(String comment_text)
    {
        if(comment_text.length() > 0 && comment_text.length() < 255)
        {
            return true;
        }
        return false;
    }
}
