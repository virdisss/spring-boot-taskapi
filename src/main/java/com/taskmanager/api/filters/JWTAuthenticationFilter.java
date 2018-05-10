package com.taskmanager.api.filters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.api.domains.Role;
import com.taskmanager.api.domains.User;
import com.taskmanager.api.interfaces.GrantedAuth;
import com.taskmanager.api.repositories.UserRepo;
import com.taskmanager.api.utils.Constants;
import com.taskmanager.api.utils.TokenManager;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author vantenor
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserRepo userRepo;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepo userRepo) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            Role role = userRepo.findByEmail(user.getEmail()).getRole();
            System.out.println("Role: " + role.getName());
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), TokenManager.getAuthorities(role)));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        UserDetails user = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal());

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Constants.TIME))
                .signWith(SignatureAlgorithm.HS512, Constants.SECRET_WORD.getBytes())
                .compact();
        TokenManager.addToken(user.getUsername(), token);
        response.addHeader("Authorization", "Bearer " + token);
    }
}
