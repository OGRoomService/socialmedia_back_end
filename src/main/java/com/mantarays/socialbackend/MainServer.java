package com.mantarays.socialbackend;

import com.mantarays.socialbackend.Services.PasswordVerification;
import com.mantarays.socialbackend.Services.UsernameVerification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MainServer 
{
    public static void main(String[] args) 
	{
		SpringApplication.run(MainServer.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	UsernameVerification usernameVerification()
	{
		return new UsernameVerification();
	}

	@Bean
	PasswordVerification passwordVerification()
	{
		return new PasswordVerification();
	}

}
