package com.dmicheldev.user_management.user.validators;

import org.springframework.stereotype.Component;

import com.dmicheldev.user_management.user.dtos.CreateUserRequest;
import com.dmicheldev.user_management.user.exceptions.BlankNameException;
import com.dmicheldev.user_management.user.exceptions.InvalidEmailException;
import com.dmicheldev.user_management.user.exceptions.InvalidPasswordException;

@Component
public class UserValidator {

    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public void validateCreateUser(CreateUserRequest request){
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());
        validateName(request.getName());
    }
        public void validateEmail(String email){

        if(email == null || email.isBlank()){
            throw new InvalidEmailException("Email can't be empty.");
        }
        if(!email.matches(EMAIL_PATTERN)){
            throw new InvalidEmailException("Invalid email pattern.");
        }

    }

    public void validatePassword(String password){

        if(password == null || password.isBlank()){
            throw new InvalidPasswordException("Password can't be empty.");
        }

        boolean validLength = password.length() >= 8;
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");

        if(!validLength || !hasLetter || !hasNumber){
            throw new InvalidPasswordException("Password must contain at least 8 characters, 1 number and 1 letter.");
        }
    }

    public void validateName(String name){

        if(name == null || name.isBlank()){
            throw new BlankNameException("Name can't be empty.");
        }
    }
}

