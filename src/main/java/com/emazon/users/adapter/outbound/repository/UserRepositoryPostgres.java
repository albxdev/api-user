package com.emazon.users.adapter.outbound.repository;

import com.emazon.users.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPostgres extends JpaRepository<User, Integer> {
    List<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByDocumentId(String documentId);
}
