package com.csye6225HW1.Exceptions.ProductException;

public class ImageNotExistException extends RuntimeException {
    public ImageNotExistException(String message){
        super(message);
    }
}
