package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    private String storedOtp;

    @GetMapping("/showAllUsers")
    public List<User> getAllUser() {
        List<User> allUser = userService.getAllUsers();
        return allUser;
    }

    @GetMapping("/showUser/{id}")
    public User getUserById(@PathVariable long id) {
        User userData = userService.getUserById(id);
        return userData;
    }

    @GetMapping("/getCustomerId/{email}")
    public Long getCustomerIdByEmail(@PathVariable String email) {
        User userData = userService.getUserByEmail(email);
        Long customerId = userData.getCustomer().getCustomerId();
        return customerId;
    }



}
