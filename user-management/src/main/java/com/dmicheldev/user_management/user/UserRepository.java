package com.dmicheldev.user_management.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dmicheldev.user_management.user.dtos.UserData;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

    List<User> findByName(String name);

    Boolean existsByEmail(String email);

    @Query("SELECT new com.dmicheldev.user_management.user.dtos.UserData(u.id, u.name, u.email, u.role) FROM User u ORDER BY u.id")
    Page<UserData> findAllUserData(Pageable pageable);
}
