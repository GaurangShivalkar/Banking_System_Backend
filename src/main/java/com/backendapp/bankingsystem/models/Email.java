package com.backendapp.bankingsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class Email {

    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
