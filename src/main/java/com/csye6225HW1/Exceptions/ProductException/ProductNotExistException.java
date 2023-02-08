package com.csye6225HW1.Exceptions.ProductException;

public class ProductNotExistException extends RuntimeException {
    public ProductNotExistException(String message){
        super(message);
    }
}
