package com.dmicheldev.user_management.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    
    UserDetails findByEmail(String email);

    List<User> findByName(String name);

    Boolean existsByEmail(String email);
}
