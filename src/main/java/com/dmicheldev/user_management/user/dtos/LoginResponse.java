package com.dmicheldev.user_management.user.dtos;

import com.dmicheldev.user_management.user.UserEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private Long id;
    private String name;
    private String email;
    private UserEnum role;
    private String token;
}
