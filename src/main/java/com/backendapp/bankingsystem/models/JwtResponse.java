package com.backendapp.bankingsystem.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String jwtToken;
    private String username;
}
