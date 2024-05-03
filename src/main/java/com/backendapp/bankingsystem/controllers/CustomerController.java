package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Customer;
import com.backendapp.bankingsystem.repositories.CustomerRepository;
import com.backendapp.bankingsystem.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/saveCustomer")
    public ResponseEntity<String> saveCustomer(@RequestBody Customer customer) {

        Customer savedCustomer = customerService.saveCustomer(customer);
        return new ResponseEntity<>("Customer successfully registered with ID: " + savedCustomer.getCustomerId(), HttpStatus.OK);
    }

    @GetMapping("/showAllCustomer")
    public List<Customer> getAllCustomer() {
        List<Customer> allCustomer = customerService.getAllCustomer();
        return allCustomer;
    }

    @GetMapping("/showCustomer/{id}")
    public Optional<Customer> getCustomerById(@PathVariable long id) {
        Optional<Customer> customerData = customerService.getCustomerById(id);
        return customerData;
    }

    @PutMapping("/updateCustomer/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable long id, @RequestBody Customer customer) {

        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return new ResponseEntity<>("the user is data is updated successfully" + updatedCustomer.getCustomerName(), HttpStatus.OK);
    }

}
