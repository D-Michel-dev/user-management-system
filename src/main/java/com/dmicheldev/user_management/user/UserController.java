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
import com.dmicheldev.user_management.user.dtos.LoginRequest;
import com.dmicheldev.user_management.user.dtos.LoginResponse;
import com.dmicheldev.user_management.user.dtos.PagedResponse;
import com.dmicheldev.user_management.user.dtos.UpdateUserRequest;
import com.dmicheldev.user_management.user.dtos.UserData;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserData> registerUser(@Valid @RequestBody CreateUserRequest request) {

        UserData newUser = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserData> getCurrentUser(Authentication authentication) {

        UserData userData = userService.getMe(authentication);

        return ResponseEntity.ok(userData);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<UserData>> getUsers(Authentication authentication, Pageable pageable) {

        User user = userService.getAuthenticatedUser(authentication);

        Page<UserData> page = userService.getUsers(user, pageable);
        PagedResponse<UserData> response = userService.convertToPagedResponse(page);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long targetUserId, Authentication authentication){

        User currentUser = userService.getAuthenticatedUser(authentication);
        userService.deleteUserById(targetUserId, currentUser);

        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{targetUserId}")
    public ResponseEntity<UserData> updateUser(
        @PathVariable Long targetUserId,
        @Valid @RequestBody UpdateUserRequest request, 
        Authentication authentication) {
        
        User currentUser = userService.getAuthenticatedUser(authentication);
        UserData updatedUser = userService.updateUser(targetUserId, request, currentUser);
        return ResponseEntity.ok().body(updatedUser);
    }
    
    
}
