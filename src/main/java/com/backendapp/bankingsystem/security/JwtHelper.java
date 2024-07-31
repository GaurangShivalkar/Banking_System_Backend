package com.backendapp.bankingsystem.security;

import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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


    private final UserRepository userRepository;
    //requirement :
    @Value("${jwt.token.validity}")
    public long JWT_TOKEN_VALIDITY;

    @Value("${jwt.secret.key}")
    private String secret_key;

    public JwtHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //retrieve username from jwt token
    public User getUserFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        User user = new User();
        user.setUserId((Integer) claims.get("userId"));
        user.setUsername((String) claims.get("username"));
        user.setEmail((String) claims.get("email"));
        user.setRole((String) claims.get("role"));

        return user;
    }


    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret_key).build().parseSignedClaims(token).getBody();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            System.err.println("Error parsing or validating token: " + e.getMessage());
            return true; // Token considered expired on any exception
        }
    }

//    //generate token for user
//    public String generateToken(Authentication authentication) {
//        String email = authentication.getName();
//        User user = userRepository.findByEmail(email);
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + JWT_TOKEN_VALIDITY);
//        Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
//        return Jwts.builder()
//                .claim("userId", user.getUserId())
//                .claim("username", user.getUsername())
//                .claim("email", user.getEmail())
//                .claim("role", user.getRole())
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }

    //generate token new method
    public String generateToken(String email) {

        User user = userRepository.findByEmail(email);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_TOKEN_VALIDITY);
        Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
        return Jwts.builder()
                .claim("userId", user.getUserId())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
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

//    public String refreshToken(Authentication authentication) {
//        final String refreshToken = generateToken(authentication);
//        return refreshToken;
//    }

}
