package com.emazon.users.domain.repository;

import com.emazon.users.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByDocumentId(String documentId);
    boolean existsByEmail(String email);


}
