package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User saveUser(User user) {

        // Save the user to the database
        return userRepository.save(user);

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
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




}
