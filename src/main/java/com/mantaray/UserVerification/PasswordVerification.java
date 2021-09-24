package com.mantaray.UserVerification;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordVerification
{
    /**
     * This verifies that a password is longer than
     * 8 characters long and contains at least a 
     * symbol and at least a digit.
     * @param password
     * @return
     */
    public boolean checkPassword(String password)
    {
        if(password.length() >= 8)
        {
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSymbol = symbol.matcher(password);

            return (hasDigit.find() && hasSymbol.find());
        }
        return false;
    }

    private Pattern digit = Pattern.compile("[0-9]");
    private Pattern symbol = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
}
