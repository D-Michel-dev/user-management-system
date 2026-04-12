package com.dmicheldev.user_management.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmicheldev.user_management.user.dtos.CreateUserRequest;
import com.dmicheldev.user_management.user.dtos.CreateUserResponse;
import com.dmicheldev.user_management.user.dtos.LoginRequest;
import com.dmicheldev.user_management.user.dtos.LoginResponse;
import com.dmicheldev.user_management.user.dtos.UserData;





@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> registerUser(@RequestBody CreateUserRequest request) {
        User user = userService.registerUser(request);

        CreateUserResponse response = new CreateUserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserData> getCurrentUser(Authentication authentication) {
            User user = (User) authentication.getPrincipal();

            UserData userData = new UserData(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
            );

        return ResponseEntity.ok(userData);
    }

    @GetMapping("")
    public ResponseEntity<List<UserData>> getUsers(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<UserData> users = userService.getUsers(user);

        return ResponseEntity.ok(users);
    }
    
    
}
