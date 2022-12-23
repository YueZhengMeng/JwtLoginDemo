package com.shou.jwtlogindemo.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shou.jwtlogindemo.security.JwtUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Calendar;


@Component
public class JwtUtil {
    private static final String salt = "1951215&1951231&1959234";

    public static String generateToken(Authentication authentication)
    {
        JwtUserDetail jwtUserDetail = (JwtUserDetail) (authentication.getPrincipal());
        int userID = jwtUserDetail.getUserid();
        String username = jwtUserDetail.getUsername();
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("userID", userID);
        builder.withClaim("username",username);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, 24);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(salt));
    }

    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(salt)).build().verify(token);
    }

    public static String getUserNameFromToken(String token) {
        DecodedJWT decodedJWT = verify(token);
        return decodedJWT.getClaims().get("username").asString();
    }

    public static int getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = verify(token);
        return decodedJWT.getClaims().get("userID").asInt();
    }
}
