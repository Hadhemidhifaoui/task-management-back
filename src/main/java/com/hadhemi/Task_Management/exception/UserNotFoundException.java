package com.hadhemi.Task_Management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("User not found");
    }


    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

