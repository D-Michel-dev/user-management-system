package com.dmicheldev.user_management.user.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request payload to create a new user account")
public class CreateUserRequest {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d).+$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Schema(description = "User full name", example = "John Doe")
    @NotBlank(message = "Name can't be empty.")
    private String name;

    @Schema(description = "User email address", example = "john.doe@email.com")
    @NotBlank(message="Email can't be empty.")
    @Email(
            regexp=EMAIL_PATTERN,
            message="Invalid email format.")
    private String email;

    @Schema(description = "User password (minimum 8 characters, at least one letter and one number)", example = "Password123")
    @NotBlank(message="Password can't be empty.")
    @Size(
            min=8,
            message="Password must have at least 8 characters.")
    @Pattern(
            regexp=PASSWORD_PATTERN,
            message="Password must include at least 1 number and 1 letter")
    private String password;
}