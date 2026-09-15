package com.neoblishange.portfolio.repository;

import com.neoblishange.portfolio.entity.user.Role;
import com.neoblishange.portfolio.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByRole(Role role);
}