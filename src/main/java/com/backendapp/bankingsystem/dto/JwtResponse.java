package com.backendapp.bankingsystem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    //private String username;
    private String refreshToken;
}
