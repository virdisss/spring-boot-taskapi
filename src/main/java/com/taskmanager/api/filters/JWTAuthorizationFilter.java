package com.taskmanager.api.filters;

import com.taskmanager.api.domains.Role;
import com.taskmanager.api.repositories.UserRepo;
import com.taskmanager.api.utils.Constants;
import com.taskmanager.api.utils.TokenManager;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author vantenor
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepo userRepo;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserRepo userRepo) {
        super(authenticationManager);
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {

        String username = Jwts.parser().setSigningKey(Constants.SECRET_WORD.getBytes()).parseClaimsJws(token.replace("Bearer ", "")).getBody().getSubject();
        if (username != null && !TokenManager.isBlackListed(username)) {
            Role role = userRepo.findByEmail(username).getRole();
            return new UsernamePasswordAuthenticationToken(username, null, TokenManager.getAuthorities(role));
        }
        return null;
    }
}
