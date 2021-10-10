package com.mantarays.socialbackend.VerificationServices;

public class PostTextVerification 
{
    /**
     * This verifies that a post's text can only
     * be between lengths 1-254 so that our database
     * can handle them.
     * @param text
     * @return
     */
    public boolean checkPostText(String text)
    {
        if(text.length() > 0 && text.length() < 255)
        {
            return true;
        }
        return false;
    }
}
