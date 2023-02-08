package com.csye6225HW1.Exceptions.ProductException;

public class CreateOrUpdateProductException extends RuntimeException {
    public CreateOrUpdateProductException(String message){
        super(message);
    }
}
