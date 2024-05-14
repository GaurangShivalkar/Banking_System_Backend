package com.backendapp.bankingsystem.services;

import java.util.Random;

public class Generators {


    public static String generateOTP() {
        final String NUMBERS = "0123456789";
        final int OTP_LENGTH = 6;
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        Random random = new Random();
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }
        return sb.toString();
    }

    public static String generateUpiId(String username) {
        // Remove any leading or trailing whitespace from the username
        username = username.trim();

        // Replace spaces with underscores (or remove them, depending on your preference)
        username = username.replace(" ", "");

        // Convert username to lowercase (UPI IDs are typically case-insensitive)
        username = username.toLowerCase();

        // Append a UPI domain to the username to form the UPI ID
        String upiDomain = "@okenpupi"; // Change this to the desired UPI domain
        String upiId = username + upiDomain;

        return upiId;
    }

}
