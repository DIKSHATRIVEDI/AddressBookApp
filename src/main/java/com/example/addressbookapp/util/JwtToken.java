package com.example.addressbookapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@Component
public class JwtToken{
    @Autowired
    static String TOKEN_SECRET = "Lock";
    @Autowired
    static Map<Long, String> activeTokens = new HashMap<>();

    public String createToken(Long id)   {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

            String token = JWT.create()
                    .withClaim("user_id", id)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 1000)) // Expires after 5 minute
                    .sign(algorithm);
            activeTokens.put(id, token);
            return token;

        } catch (JWTCreationException exception) {
            exception.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Long decodeToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET))
                    .acceptExpiresAt(0) // Accept token within the expiration time
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("user_id").asLong();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid or expired token.");
        }
    }

    public boolean isUserLoggedIn(Long userId, String token) {
        try {
            if (activeTokens.containsKey(userId) && activeTokens.get(userId).equals(token)) {
                decodeToken(token); // Verifies if the token is still valid
                return true;
            }
        } catch (Exception e) {
            // If token is expired, remove it from the active tokens
            activeTokens.remove(userId);
        }
        return false;
        // return activeTokens.containsKey(userId) && activeTokens.get(userId).equals(token);
    }

    public void logoutUser(Long userId) {
        activeTokens.remove(userId);
    }

    // New methods to get the current user ID and token
    public Long getCurrentUserId(String token) {
        return decodeToken(token); // Extracts the user ID from the token
    }

    public String getCurrentToken(Long userId) {
        return activeTokens.get(userId); // Retrieves the token for a logged-in user
    }

}
