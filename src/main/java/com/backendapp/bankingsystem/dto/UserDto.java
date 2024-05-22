package com.backendapp.bankingsystem.dto;

import lombok.Data;

@Data
public class UserDto {

    private int userId;
    private String username;
    private String email;
    private String password;
    private String role;
    private Long customerId;

}
