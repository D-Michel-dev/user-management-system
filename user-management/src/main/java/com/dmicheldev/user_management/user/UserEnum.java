package com.dmicheldev.user_management.user;

public enum UserEnum {
    USER("user"),
    ADMIN("admin");

    private String role;

    UserEnum(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
