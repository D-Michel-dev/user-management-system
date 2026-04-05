package com.dmicheldev.user_management.user.exceptions;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException(String message){
        super(message);
    }
}
