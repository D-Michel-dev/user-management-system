package com.dmicheldev.user_management.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dmicheldev.user_management.user.exceptions.BlankNameException;
import com.dmicheldev.user_management.user.exceptions.EmailAlreadyExistsException;
import com.dmicheldev.user_management.user.exceptions.InvalidEmailException;
import com.dmicheldev.user_management.user.exceptions.InvalidPasswordException;

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

    // ==== HELPER METHODS ====

    private void validateEmail(String email){

        if(email.isBlank() || email.isEmpty()){
            throw new InvalidEmailException("Email can't be empty.");
        }
        if(!email.matches(EMAIL_PATTERN)){
            throw new InvalidEmailException("Invalid email pattern.");
        }

    }

    private void validatePassword(String password){

        if(password.isBlank() || password.isEmpty()){
            throw new InvalidPasswordException("Password can't be empty.");
        }

        boolean validLength = password.length() >= 8;
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");

        if(!validLength || !hasLetter || !hasNumber){
            throw new InvalidPasswordException("Password must contain at leat 8 caracters, 1 number and 1 letter.");
        }
    }

    private void validateName(String name){
        if(name.isBlank() || name.isEmpty()){
            throw new BlankNameException("Name can't be empty.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
