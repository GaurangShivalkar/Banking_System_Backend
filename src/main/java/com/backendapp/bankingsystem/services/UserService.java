package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.ResetToken;
import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.repositories.ResetTokenRepository;
import com.backendapp.bankingsystem.repositories.UserRepository;
import com.backendapp.bankingsystem.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResetTokenRepository resetTokenRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private JwtHelper jwtHelper;
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
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User loadUserByEmail(String email, String password) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not exists by Username or Email");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        GrantedAuthority UserAuthority = new SimpleGrantedAuthority(user.getRole());
        authorities.add(UserAuthority);

        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                authorities
        );
    }

    @Transactional
    public void forgetPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            try {
                String resetPasswordToken = jwtHelper.generateToken(email);

                Date expiryDate = jwtHelper.getExpirationDateFromToken(resetPasswordToken);
                System.out.println(expiryDate);

                ResetToken resetToken = ResetToken.builder()
                        .resetPasswordToken(resetPasswordToken)
                        .expiryDate(expiryDate)
                        .build();
                resetTokenRepository.save(resetToken);

                // Prepare and send the email
                String subject = "Password Reset Request";
                String resetLink = "http://localhost:5173/resetPassword?token=" + resetPasswordToken;
                String message = "Please click the following link to reset your password:\n" + resetLink;
                sendSimpleMail(email, message, subject);
            } catch (Exception e) {
                throw new RuntimeException("Failed to process password reset request", e);
            }
        } else {
            throw new RuntimeException("User email does not exist");
        }
    }

    @Transactional
    public void deleteTokenFromDb(String token) {
        Optional<ResetToken> resetToken = resetTokenRepository.findByResetPasswordToken(token);
        if (resetToken.isPresent() && jwtHelper.validateToken(token)) {
            resetTokenRepository.deleteByResetPasswordToken(token);

        }

    }

    @Transactional
    public void resetPassword(String token, User password) {
        Optional<ResetToken> resetToken = resetTokenRepository.findByResetPasswordToken(token);
        if (jwtHelper.validateToken(token) && resetToken.isPresent()) {
            User userData = jwtHelper.getUserFromToken(token);
            User userUpdate = updateUser(userData.getUserId(), password);
            if (userUpdate != null) {
                deleteTokenFromDb(token);
            }
        }
    }
}
