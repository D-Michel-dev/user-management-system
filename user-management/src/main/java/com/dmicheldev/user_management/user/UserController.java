package com.dmicheldev.user_management.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    
}
