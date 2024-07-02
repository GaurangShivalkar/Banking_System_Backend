package com.backendapp.bankingsystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String password;
    private String role;

    @ManyToOne(optional = true)
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
    private Customer customer;


}
