package com.dmicheldev.user_management.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    
    @NotBlank(message="Name can't be empty.")
    private String name;
}
