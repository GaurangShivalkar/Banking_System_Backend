package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.JwtRequest;
import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.security.JwtHelper;
import com.backendapp.bankingsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> saveUser(@RequestBody User user) {

        User registeredUser = userService.saveUser(user);
        return new ResponseEntity<>("User registered successfully with ID: " + registeredUser.getUserId(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword())
//            );
//        } catch (BadCredentialsException e) {
//            throw new Exception("Incorrect email or password", e);
//        }

        final User userDetails = userService.loadUserByEmail(jwtRequest.getEmail(), jwtRequest.getPassword());
        final String token = jwtHelper.generateToken(userDetails);
        return token;
    }

    @GetMapping("/loggedInUser")
    public String loggedInUser(Principal principal) {
        return principal.getName();
    }
}
