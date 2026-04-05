package com.dmicheldev.user_management.user.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message){
        super(message);
    }
    
}
