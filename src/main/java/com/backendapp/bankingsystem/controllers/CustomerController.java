package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Customer;
import com.backendapp.bankingsystem.services.CustomerService;
import com.backendapp.bankingsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;


    @PostMapping("/saveCustomer")
    public Long saveCustomer(@RequestBody Customer customer) {

        Customer savedCustomer = customerService.saveCustomer(customer);
        String custMsg = "The user customer Id is: " + savedCustomer.getCustomerId() + "\nThe user email for registration is " + savedCustomer.getEmail();
        String custSubject = "Details for registration";
        userService.sendSimpleMail(savedCustomer.getEmail(), custMsg, custSubject);
        return savedCustomer.getCustomerId();
    }

    @GetMapping("/showAllCustomer")
    public List<Customer> getAllCustomer() {
        List<Customer> allCustomer = customerService.getAllCustomer();
        return allCustomer;
    }

    @GetMapping("/showCustomer/{id}")
    public Customer getCustomerById(@PathVariable long id) {
        Customer customerData = customerService.getCustomerById(id);
        return customerData;
    }

    @GetMapping("/getCustomerIdByPan")
    public Long getCustomerIdByPan(@RequestParam String panNumber) {
        Customer customerData = customerService.getCustomerByPanNumber(panNumber);
        return customerData.getCustomerId();
    }

    @PutMapping("/updateCustomer/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable long id, @RequestBody Customer customer) {

        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return new ResponseEntity<>("the user is data is updated successfully" + updatedCustomer.getCustomerName(), HttpStatus.OK);
    }

}
