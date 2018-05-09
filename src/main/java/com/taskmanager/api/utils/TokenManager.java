package com.taskmanager.api.utils;

import java.util.HashMap;
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

}
