package com.mantarays.socialbackend.Security;

import com.mantarays.socialbackend.Filter.CustomAuthorizationFilter;
import com.mantarays.socialbackend.Repositories.RoleRepository;
import com.mantarays.socialbackend.Repositories.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer
{
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/token/refresh/").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/create/").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/login/").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/logout").permitAll();


        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/posts/create").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/posts/getposts").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/posts/likepost").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/posts/dislikepost").hasAnyAuthority("ROLE_USER");
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("*");
        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("HEAD");
        allowedMethods.add("GET");
        allowedMethods.add("POST");
        allowedMethods.add("PUT");
        allowedMethods.add("DELETE");
        allowedMethods.add("PATCH");
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("Authorization");
        allowedHeaders.add("Cache-Control");
        allowedHeaders.add("Content-Type");
        configuration.setAllowedOriginPatterns(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        //configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(allowedHeaders);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**").allowedMethods("*");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }
}
