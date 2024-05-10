package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.dto.JwtRequest;
import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.security.JwtHelper;
import com.backendapp.bankingsystem.services.Generators;
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

    private static String storedOtp;
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

    @PostMapping("/sendMail/{email}")
    public String sendMail(@PathVariable String email) {
        String otp = Generators.generateOTP();
        String status = userService.sendSimpleMail(email, otp);
        this.storedOtp = otp; // Store OTP temporarily in memory
        return "OTP sent successfully to " + email;
    }

    @PostMapping("/verify")
    public boolean verifyEmail(@RequestBody String otp) {

        if (otp.equals(storedOtp)) {
            // OTP verification successful
            return true;
        } else {
            // Incorrect OTP
            System.out.println("OTP " + otp + "doesn't match with " + storedOtp);
            return false;
        }
    }
}
