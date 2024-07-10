package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        // Save the user to the database
        return userRepository.save(user);

    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        User userDetail = userRepository.findByUserId(userId);
        return userDetail;
    }

    public String getEmailByCustomer(Long customerId) {
        User userDetail = userRepository.findByCustomer_CustomerId(customerId);
        String email = userDetail.getEmail();
        return email;
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            } else if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            } else if (updatedUser.getPassword() != null) {
                user.setPassword(updatedUser.getPassword());
            }
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User loadUserByEmail(String email, String password) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with email: " + email);
//        }
//        return user;

//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword())
//            );
//        } catch (BadCredentialsException e) {
//            throw new Exception("Incorrect email or password", e);
//        }
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {

            return user;
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public String sendSimpleMail(String email, String text, String subject) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(email);
            mailMessage.setText(text);
            mailMessage.setSubject(subject);

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }
        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

}
