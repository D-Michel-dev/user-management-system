package com.dmicheldev.user_management.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d).+$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @NotBlank(message = "Name can't be empty.")
    private String name;

    @NotBlank(message="Email can't be empty.")
    @Email(
        regexp=EMAIL_PATTERN,
        message="Invalid email format.")
    private String email;

    @NotBlank(message="Password can't be empty.")
    @Size(
        min=8, 
        message="Password must have at leasts 8 characters.")
    @Pattern(
        regexp=PASSWORD_PATTERN, 
        message="Password must include at least 1 number and 1 letter")
    private String password;
}
