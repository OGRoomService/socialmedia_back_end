package com.mantarays.socialbackend.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;

public class EmailUtility
{
    @Autowired
    private JavaMailSender emailSender;

    private String email = "RowanSpaceSocial@gmail.com";
    private String name = "Rowanspace Support";

    public void sendEmail(String subject, String content, String toEmail) throws MessagingException, UnsupportedEncodingException
    {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(email, name);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        FileSystemResource file = new FileSystemResource(new File(System.getProperty("user.dir") + "\\user-photos\\sadge.png"));
        helper.addInline("sadge_picture", file);

        emailSender.send(message);
    }
}
