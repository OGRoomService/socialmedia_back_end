package com.mantarays.socialbackend.Filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantarays.socialbackend.Services.UserService;

import com.mantarays.socialbackend.Utilities.TokenUtility;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenUtility tokenUtility;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService)
    {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenUtility = new TokenUtility(userService);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");


        if(email != null)
        {
            com.mantarays.socialbackend.Models.User user = userService.loadUserByEmail(email);
            username = user.getUsername();
        }

        log.info("Trying to login with username: {} , password: {}, email: {}", username, password, email);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException
    {
        Map<String, String> tokens = tokenUtility.generateNewUserTokens(request, authentication);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
