package org.example.ch3schedulerdevelopprojectexplanation.user.repository;

import org.example.ch3schedulerdevelopprojectexplanation.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
