package com.dmicheldev.user_management.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users & Authentication", description = "Endpoints for user management and authentication.")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    // ========================= REGISTER =========================

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user account. Validates input data, ensures email uniqueness, and returns non-sensitive user data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing fields"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserData> registerUser(@Valid @RequestBody CreateUserRequest request) {
        UserData newUser = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // ========================= LOGIN =========================

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user using email and password. Returns user data along with authentication information."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // ========================= CURRENT USER =========================

    @Operation(
            summary = "Get current user",
            description = "Returns the authenticated user's data based on the provided JWT token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token")
    })
    @GetMapping("/me")
    public ResponseEntity<UserData> getCurrentUser(Authentication authentication) {
        UserData userData = userService.getMe(authentication);
        return ResponseEntity.ok(userData);
    }

    // ========================= LIST USERS =========================

    @Operation(
            summary = "List users",
            description = "Returns a paginated list of users. Only users with ADMIN role can access this endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    @GetMapping
    public ResponseEntity<PagedResponse<UserData>> getUsers(Authentication authentication, Pageable pageable) {
        User user = userService.getAuthenticatedUser(authentication);
        Page<UserData> page = userService.getUsers(user, pageable);
        PagedResponse<UserData> response = userService.convertToPagedResponse(page);
        return ResponseEntity.ok(response);
    }

    // ========================= DELETE USER =========================

    @Operation(
            summary = "Delete user",
            description = "Deletes a user by ID. Only ADMIN users can delete other users. Admin users cannot be deleted."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<Void> deleteUserById(
            @PathVariable Long targetUserId,
            Authentication authentication
    ){
        User currentUser = userService.getAuthenticatedUser(authentication);
        userService.deleteUserById(targetUserId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // ========================= UPDATE USER =========================

    @Operation(
            summary = "Update user",
            description = "Updates user data. Users can update their own profile. Admins can update non-admin users. Admin profiles cannot be updated by others."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{targetUserId}")
    public ResponseEntity<UserData> updateUser(
            @PathVariable Long targetUserId,
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication
    ) {
        User currentUser = userService.getAuthenticatedUser(authentication);
        UserData updatedUser = userService.updateUser(targetUserId, request, currentUser);
        return ResponseEntity.ok(updatedUser);
    }
}