package com.backendapp.bankingsystem.security;

import com.backendapp.bankingsystem.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtHelper {

    //requirement :
    @Value("${jwt.token.validity}")
    public long JWT_TOKEN_VALIDITY;

    @Value("${jwt.secret.key}")
    private String secret_key;

    //retrieve username from jwt token
    public User getUserFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        User user = new User();
        user.setUserId((Integer) claims.get("userId"));
        user.setUsername((String) claims.get("username"));
        user.setEmail((String) claims.get("email"));
        System.out.println("user: " + user);
        return user;
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser().setSigningKey(secret_key).build().parseClaimsJws(token).getBody();
        return Jwts.parser().setSigningKey(secret_key).build().parseSignedClaims(token).getPayload();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_TOKEN_VALIDITY);
        Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
        return Jwts.builder()
                .claim("userId", user.getUserId())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }



    //validate token
    public Boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
