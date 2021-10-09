package com.mantarays.socialbackend.Services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailVerification
{
    /**
     * This determines whether or not an email follows
     * the regex set in place. This does not actually
     * tell you whether or not the email exists.
     * @param email
     * @return
     */
    public boolean checkEmail(String email)
    {
        Matcher emailMatcher = emailRegex.matcher(email);
        return emailMatcher.find();
    }

    private Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
