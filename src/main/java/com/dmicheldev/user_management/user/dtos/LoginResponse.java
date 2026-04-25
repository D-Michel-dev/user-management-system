package com.dmicheldev.user_management.user.dtos;

import com.dmicheldev.user_management.user.UserEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response returned after successful authentication")
public class LoginResponse {

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "User name", example = "John Doe")
    private String name;

    @Schema(description = "User email", example = "john.doe@email.com")
    private String email;

    @Schema(description = "User role", example = "USER")
    private UserEnum role;

    @Schema(description = "JWT token used for authenticated requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
