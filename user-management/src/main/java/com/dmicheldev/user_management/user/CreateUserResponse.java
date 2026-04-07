package com.dmicheldev.user_management.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {

    private Long id;
    private String name;
    private String email;
    private UserEnum role;
}
