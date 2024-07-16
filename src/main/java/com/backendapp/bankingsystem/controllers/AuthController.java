package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.dto.JwtRequest;
import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.security.JwtHelper;
import com.backendapp.bankingsystem.services.OtpService;
import com.backendapp.bankingsystem.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private OtpService otpService;
    @Autowired
    private AuthenticationManager authenticationManager;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> saveUser(@RequestBody User user) {

        User registeredUser = userService.saveUser(user);
        return new ResponseEntity<>("User registered successfully with ID: " + registeredUser.getUserId(), HttpStatus.CREATED);
    }


    @GetMapping("/user")
    public ResponseEntity<User> getUserDetails(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        User user = jwtHelper.getUserFromToken(token);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public String createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        //final User userDetails = userService.loadUserByEmail(jwtRequest.getEmail(), jwtRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtHelper.generateToken(authentication);

        return token;
    }

    @GetMapping("/loggedInUser")
    public String loggedInUser(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/generateOtp")
    public String generateOtp(@RequestParam String key, @RequestParam String email) {
        int otp = otpService.generateOtp(key);
        otpService.sendOtp(email, otp);
        return "OTP sent successfully to " + email;
    }

    @GetMapping("/validateOtp")
    public boolean validateOtp(@RequestParam String key, @RequestParam int otp) {
        if (otpService.getOtp(key) == otp) {
            otpService.clearOtp(key);
            return true;
        } else {
            return false;
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        String msg = "Hi your password has been updated for user with email " + updatedUser.getEmail() + "! \nPlease contact the bank moderator if you havent change the password";
        String subject = "Update on Password change";
        String email = updatedUser.getEmail();
        userService.sendSimpleMail(email, msg, subject);
        return new ResponseEntity<>("the user is data is updated successfully" + updatedUser.getUsername(), HttpStatus.OK);
    }

    @GetMapping("/checkExpiry/{token}")
    public Boolean isTokenExpired(@PathVariable String token) {
        return jwtHelper.isTokenExpired(token);
    }
}