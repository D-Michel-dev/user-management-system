package com.dmicheldev.user_management.user.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request payload for user authentication")
public class LoginRequest {

    @Schema(description = "User email", example = "john.doe@email.com")
    private String email;

    @Schema(description = "User password", example = "Password123")
    private String password;
}