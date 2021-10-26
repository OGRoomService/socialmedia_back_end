package com.mantarays.socialbackend;

import com.mantarays.socialbackend.VerificationServices.EmailVerification;
import com.mantarays.socialbackend.VerificationServices.PasswordVerification;
import com.mantarays.socialbackend.VerificationServices.PostTextVerification;
import com.mantarays.socialbackend.VerificationServices.UsernameVerification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MainServer
{
    public static void main(String[] args)
	{
		SpringApplication.run(MainServer.class, args);
	}

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
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

	@Bean
	EmailVerification emailVerification()
	{
		return new EmailVerification();
	}

	@Bean
	PostTextVerification postTextVerification()
	{
		return new PostTextVerification();
	}

}
