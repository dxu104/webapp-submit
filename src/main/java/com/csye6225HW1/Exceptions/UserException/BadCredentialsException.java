package com.csye6225HW1.Exceptions.UserException;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
