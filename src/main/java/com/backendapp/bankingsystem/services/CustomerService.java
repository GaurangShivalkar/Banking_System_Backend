package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Customer;
import com.backendapp.bankingsystem.repositories.CustomerRepository;
import com.backendapp.bankingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private UserRepository userRepository;

    public Customer saveCustomer(Customer customer) {
        long customerId = Long.parseLong(Generators.generateCustomerId());
        customer.setCustomerId(customerId);
        customer.setStatus("pending");
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    public Customer updateCustomer(Long customerId, Customer updatedCustomer) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            if (updatedCustomer.getAddress() != null) {
                customer.setAddress(updatedCustomer.getAddress());
            } else if (updatedCustomer.getPhoneNumber() != null) {
                customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
            } else if (updatedCustomer.getZipcode() != null) {
                customer.setZipcode(updatedCustomer.getZipcode());
            } else if (updatedCustomer.getStatus() != null) {
                customer.setStatus(updatedCustomer.getStatus());
            }
            return customerRepository.save(customer);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Customer getCustomerByPanNumber(String panNumber) {
        return customerRepository.findByPanNumber(panNumber);
    }
}
