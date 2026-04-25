package com.dmicheldev.user_management.user;

import com.dmicheldev.user_management.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully created",
                    content = @Content(schema = @Schema(implementation = UserData.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid or missing fields",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserData> registerUser(@Valid @RequestBody CreateUserRequest request) {
        UserData newUser = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // ========================= LOGIN =========================

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user using email and password and returns a JWT token along with user data."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid email or password",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
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
            @ApiResponse(
                    responseCode = "200",
                    description = "User data retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserData.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/me")
    public ResponseEntity<UserData> getCurrentUser(Authentication authentication) {
        UserData userData = userService.getMe(authentication);
        return ResponseEntity.ok(userData);
    }

    // ========================= LIST USERS =========================

    @Operation(
            summary = "List users",
            description = "Returns a paginated list of users. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
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
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
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
            description = "Updates user data. Users can update their own profile. Admins can update non-admin users."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserData.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
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