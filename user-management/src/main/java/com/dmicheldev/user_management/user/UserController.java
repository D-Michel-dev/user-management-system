package com.dmicheldev.user_management.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmicheldev.user_management.user.dtos.CreateUserRequest;
import com.dmicheldev.user_management.user.dtos.CreateUserResponse;
import com.dmicheldev.user_management.user.dtos.LoginRequest;
import com.dmicheldev.user_management.user.dtos.LoginResponse;
import com.dmicheldev.user_management.user.dtos.PagedResponse;
import com.dmicheldev.user_management.user.dtos.UpdateUserRequest;
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
    public ResponseEntity<PagedResponse<UserData>> getUsers(Authentication authentication, Pageable pageable) {

        User user = (User) authentication.getPrincipal();

        Page<UserData> page = userService.getUsers(user, pageable);

        PagedResponse<UserData> response = new PagedResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long targetUserId, Authentication authentication){

        User currentUser = (User) authentication.getPrincipal();
        userService.deleteUserById(targetUserId, currentUser);

        return ResponseEntity.ok().body("User deleted.");

    }

    @PatchMapping("/{targetUserId}")
    public ResponseEntity<UserData> updateUser(
        @PathVariable Long targetUserId, 
        @RequestBody UpdateUserRequest request, 
        Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();

        UserData updatedUser = userService.updateUser(targetUserId, request, currentUser);

        return ResponseEntity.ok().body(updatedUser);
    }
    
    
}
