package com.dmicheldev.user_management.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dmicheldev.user_management.user.dtos.CreateUserRequest;
import com.dmicheldev.user_management.user.dtos.LoginRequest;
import com.dmicheldev.user_management.user.dtos.LoginResponse;
import com.dmicheldev.user_management.user.dtos.UserData;
import com.dmicheldev.user_management.user.exceptions.BlankNameException;
import com.dmicheldev.user_management.user.exceptions.EmailAlreadyExistsException;
import com.dmicheldev.user_management.user.exceptions.ForbiddenException;
import com.dmicheldev.user_management.user.exceptions.InvalidCredentialsException;
import com.dmicheldev.user_management.user.exceptions.InvalidEmailException;
import com.dmicheldev.user_management.user.exceptions.InvalidPasswordException;
import com.dmicheldev.user_management.user.exceptions.UserNotFoundException;

@Service
public class UserService implements UserDetailsService {

    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(CreateUserRequest request) {

        validateEmail(request.getEmail());
        validatePassword(request.getPassword());
        validateName(request.getName());

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

    // ------------------------------------------------------------------------
    // ---------------------------- HELPER METHODS ----------------------------
    // ------------------------------------------------------------------------

    private void validateEmail(String email){

        if(email == null){
            throw new InvalidEmailException("Email can't be empty.");
        }

        if(email.isBlank() || email.isEmpty()){
            throw new InvalidEmailException("Email can't be empty.");
        }
        if(!email.matches(EMAIL_PATTERN)){
            throw new InvalidEmailException("Invalid email pattern.");
        }

    }

    private void validatePassword(String password){

        if(password == null){
            throw new InvalidPasswordException("Password can't be empty");
        }

        if(password.isBlank() || password.isEmpty()){
            throw new InvalidPasswordException("Password can't be empty.");
        }

        boolean validLength = password.length() >= 8;
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");

        if(!validLength || !hasLetter || !hasNumber){
            throw new InvalidPasswordException("Password must contain at least 8 characters, 1 number and 1 letter.");
        }
    }

    private void validateName(String name){
        if(name == null){
            throw new BlankNameException("Name can't be empty");
        }
        if(name.isBlank() || name.isEmpty()){
            throw new BlankNameException("Name can't be empty.");
        }
    }

    private boolean isAdmin(User user){
        return user.getRole() == UserEnum.ADMIN;   
    }

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

    private boolean canDelete(User targetUser, User currentUser){
        if(isAdmin(targetUser)){
            return false;
        }
        return currentUser.getRole() == UserEnum.ADMIN;
    }
}
