package com.mantaray.UserVerification;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailVerification
{
    public boolean checkEmail(String email)
    {
        Matcher emailMatcher = emailRegex.matcher(email);
        return emailMatcher.find();
    }

    private Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
