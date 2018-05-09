package com.taskmanager.api.services;

import com.taskmanager.api.dao.UserDAO;
import com.taskmanager.api.domains.User;
import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author vantenor
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDAO userService;

    public UserDetailsServiceImpl(UserDAO service) {
        this.userService = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
