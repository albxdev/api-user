package com.emazon.users.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.emazon.users.domain.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
