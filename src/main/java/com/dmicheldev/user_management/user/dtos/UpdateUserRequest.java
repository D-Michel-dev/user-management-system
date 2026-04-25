package com.dmicheldev.user_management.user.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request payload to update user information")
public class UpdateUserRequest {

    @Schema(description = "Updated user name", example = "John Updated")
    @NotBlank(message="Name can't be empty.")
    private String name;
}
