package com.mantarays.socialbackend.Services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameVerification 
{
    /**
     * This verifies that a username is no shorter than
     * 6 characters and also does not contain any
     * special symbols.
     * @param username
     * @return
     */
    public boolean checkUsername(String username)
    {
        Matcher hasSymbol = symbol.matcher(username);
        return(username.length() >= 6 && !(hasSymbol.find()));
    }
    
    private Pattern symbol = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
}
