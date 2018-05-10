package com.taskmanager.api.utils;

import com.taskmanager.api.domains.Role;
import com.taskmanager.api.interfaces.GrantedAuth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vantenor
 */
public class TokenManager {

    private static Map<String, String> enabledTokens = new HashMap<>();

    public static void addToken(String email, String token) {
        enabledTokens.put(email, token);
    }

    public static boolean isBlackListed(String email) {
        return enabledTokens.get(email) == null;
    }

    public static void revokeToken(String email) {
        enabledTokens.remove(email);
    }

    public static List<GrantedAuth> getAuthorities(Role role) {
        List<GrantedAuth> authorities = new ArrayList<>();
        authorities.add(new GrantedAuth(role.getName()));
        return authorities;
    }

}
