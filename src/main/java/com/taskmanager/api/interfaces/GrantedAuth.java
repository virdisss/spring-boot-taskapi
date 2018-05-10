package com.taskmanager.api.interfaces;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author vantenor
 */
public class GrantedAuth implements GrantedAuthority {

    private String authority;

    public GrantedAuth(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
