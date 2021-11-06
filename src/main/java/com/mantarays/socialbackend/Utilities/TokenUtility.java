package com.mantarays.socialbackend.Utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mantarays.socialbackend.Models.Role;
import com.mantarays.socialbackend.Models.User;
import com.mantarays.socialbackend.Repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TokenUtility
{
    private final Algorithm algorithm;
    private final long DAY;
    private final long WEEK;
    private final long MONTH;
    private final long YEAR;
    private static JWTVerifier verifier;

    public TokenUtility()
    {
        this.algorithm = Algorithm.HMAC256("TotallySecretLoginToken".getBytes());

        this.DAY = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
        this.WEEK = this.DAY * 7;
        this.MONTH = this.WEEK * 4;
        this.YEAR = this.MONTH * 12;

        verifier = JWT.require(algorithm).build();
    }

    public Map<String, String> generateNewAccessTokenFromRefreshToken(HttpServletRequest request, UserRepository userRepo)
    {
        String authorizationHeader = request.getHeader("Authorization");
        String refreshToken = authorizationHeader.substring("Bearer ".length());
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        User user = userRepo.findByUsername(username);

        String accessToken = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(this.WEEK))
            .withIssuer(request.getRequestURL().toString())
            .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
            .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public Map<String, String> generateNewUserTokens(HttpServletRequest request, Authentication authentication)
    {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String accessToken = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(this.WEEK))
            .withIssuer(request.getRequestURL().toString())
            .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .sign(algorithm);

        String refreshToken = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(this.YEAR))
            .withIssuer(request.getRequestURL().toString())
            .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public String getUsernameFromToken(String token)
    {
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    public Collection<SimpleGrantedAuthority> getAuthoritiesFromToken(String token)
    {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        DecodedJWT decodedJWT = verifier.verify(token);
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Stream<String> stream = Arrays.stream(roles);

        stream.forEach(role ->
        {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        return authorities;
    }
}
