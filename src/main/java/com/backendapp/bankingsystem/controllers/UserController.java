package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.repositories.UserRepository;
import com.backendapp.bankingsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> saveUser(@RequestBody User user) {

        User registeredUser = userService.saveUser(user);
        return new ResponseEntity<>("User registered successfully with ID: " + registeredUser.getUserId(), HttpStatus.CREATED);
    }

    @GetMapping("/loggedInUser")
    public String loggedInUser(Principal principal) {
        return principal.getName();
    }
    @GetMapping("/showAllUsers")
    public List<User> getAllUser() {
        List<User> allUser = userService.getAllUsers();
        return allUser;
    }

    @GetMapping("/showUser/{id}")
    public Optional<User> getUserById(@PathVariable long id) {
        Optional<User> userData = userService.getUserById(id);
        return userData;
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable long id, @RequestBody User user) {

        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>("the user is data is updated successfully" + updatedUser.getUsername(), HttpStatus.OK);
    }
}
