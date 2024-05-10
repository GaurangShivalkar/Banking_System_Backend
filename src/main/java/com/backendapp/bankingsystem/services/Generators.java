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
}
