package com.mantarays.socialbackend;

import com.mantarays.socialbackend.Repositories.UserRepository;
import com.mantarays.socialbackend.Utilities.EmailUtility;
import com.mantarays.socialbackend.Utilities.TokenUtility;
import com.mantarays.socialbackend.VerificationServices.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
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
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("*")
                    .allowedHeaders("*");
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

	@Bean
    RecoveryQuestionVerification recoveryQuestionVerification()
    {
        return new RecoveryQuestionVerification();
    }

    @Bean
    TokenUtility tokenUtility()
    {
        return new TokenUtility();
    }

    @Bean EmailUtility emailUtility()
    {
        return new EmailUtility();
    }
}
