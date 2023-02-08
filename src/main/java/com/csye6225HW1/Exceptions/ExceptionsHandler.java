package com.csye6225HW1.Exceptions;

import com.csye6225HW1.Exceptions.UserException.BadCredentialsException;
import com.csye6225HW1.Exceptions.UserException.ChangeOthersInfoException;
import com.csye6225HW1.Exceptions.UserException.UsernameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {



    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity UsernameNotFoundException(UsernameNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleBadCredentialsException(UsernameNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(ChangeOthersInfoException.class)
    public ResponseEntity handleBadCredentialsException(ChangeOthersInfoException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleException(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}

