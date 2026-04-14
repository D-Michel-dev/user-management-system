package com.dmicheldev.user_management.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dmicheldev.user_management.user.dtos.CreateUserRequest;
import com.dmicheldev.user_management.user.dtos.LoginRequest;
import com.dmicheldev.user_management.user.dtos.LoginResponse;
import com.dmicheldev.user_management.user.dtos.UpdateUserRequest;
import com.dmicheldev.user_management.user.dtos.UserData;
import com.dmicheldev.user_management.user.exceptions.EmailAlreadyExistsException;
import com.dmicheldev.user_management.user.exceptions.ForbiddenException;
import com.dmicheldev.user_management.user.exceptions.InvalidCredentialsException;
import com.dmicheldev.user_management.user.exceptions.UserNotFoundException;
import com.dmicheldev.user_management.user.validators.UserValidator;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;


    public UserService(UserRepository userRepository, UserValidator userValidator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(CreateUserRequest request) {

        userValidator.validateCreateUser(request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }


        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(UserEnum.USER);

        return userRepository.save(newUser);
    }

    public LoginResponse login(LoginRequest request){

        User user = getUserByEmail(request.getEmail());

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        LoginResponse response = new LoginResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );

        return response;
        
    }

    public List<UserData> getUsers(User user){

        if(!isAdmin(user) ){
            throw new ForbiddenException("Access denied.");
        }
        return userRepository.getUsers();
    }

    public void deleteUserById(Long targetUserId, User currentUser){

        User targetUser = getUserById(targetUserId);

        if(!canDelete(targetUser, currentUser)){
            throw new ForbiddenException("Access denied.");
        }

        userRepository.deleteById(targetUserId);
    }

    public UserData updateUser(Long targetUserId, UpdateUserRequest request, User currentUser){

        
        User targetUser = getUserById(targetUserId);
        
        if(!canUpdate(targetUser, currentUser)){
            throw new ForbiddenException("Access denied.");
        }
        
        userValidator.validateName(request.getName());

        targetUser.setName(request.getName());
        User savedUser = userRepository.save(targetUser);
        return convertToUserData(savedUser);

    }

    // ------------------------------------------------------------------------
    // ---------------------------- HELPER METHODS ----------------------------
    // ------------------------------------------------------------------------



    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return user;
    }
    
    private User getUserById(Long userId){
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    private User getUserByEmail(String email){
        return userRepository.findByEmail(email)
            .orElseThrow(()-> new UserNotFoundException("User not found.")); 
    }

    private boolean isAdmin(User user){
        return user.getRole() == UserEnum.ADMIN;   
    }

    private boolean canDelete(User targetUser, User currentUser){
        if(isAdmin(targetUser)){
            return false;
        }
        return currentUser.getRole() == UserEnum.ADMIN;
    }

    private boolean canUpdate(User targetUser, User currentUser){

        boolean isSelf = targetUser.getId().equals(currentUser.getId());
        boolean targetIsAdmin = isAdmin(targetUser);
        boolean currentUserIsAdmin = isAdmin(currentUser);

        if(isSelf){
            return true;
        }
        if(targetIsAdmin){
            return false;
        }
        return currentUserIsAdmin;
        
    }

    private UserData convertToUserData(User user){
        UserData userData = new UserData(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
        return userData;
    }
}
