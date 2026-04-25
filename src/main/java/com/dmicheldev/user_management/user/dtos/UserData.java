package com.dmicheldev.user_management.user.dtos;

import com.dmicheldev.user_management.user.UserEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User data returned by the API")
public class UserData {

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "User name", example = "John Doe")
    private String name;

    @Schema(description = "User email", example = "john.doe@email.com")
    private String email;

    @Schema(description = "User role", example = "USER")
    private UserEnum role;
}