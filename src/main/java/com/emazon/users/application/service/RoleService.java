package com.emazon.users.application.service;

import com.emazon.users.domain.model.Role;
import com.emazon.users.domain.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleService() {
    }

    public Role findRoleByName(String name) {
        Optional<Role> roleOptional = roleRepository.findByName(name);
        return roleOptional.orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public abstract Optional<Role> findRoleById(Long id);

    public abstract void deleteRoleById(Long id);

    // Other methods for managing roles
}
