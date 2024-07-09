package com.example.erp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.example.erp.entity.admin.AdminLogin;
import com.example.erp.entity.clientDetails.ClientProfile;

public class JwtUtils {
    private static final String SECRET = "TE7koozHpSsNbW2P3COvvWC7umIzVqOIO/6RNLmWVl4=";
    private static final long EXPIRATION_TIME = 864_000_000; 

    public static String generateToken(AdminLogin admin) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, admin.getEmail());
    }
    
    public static String generateClientToken(ClientProfile profile) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, profile.getEmail());
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static boolean validateToken(String token, AdminLogin admin) {
        final String email = extractUsername(token);
        return (email.equals(admin.getEmail()) && !isTokenExpired(token));
    }
    public static boolean validateClientToken(String token, ClientProfile profile) {
        final String email = extractUsername(token);
        return (email.equals(profile.getEmail()) && !isTokenExpired(token));
    }

    private static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    private static boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }
}
