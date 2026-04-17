package com.dmicheldev.user_management.user.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message){
        super(message);
    }
}
