package com.emazon.users.adapter.inbound.rest;

import com.emazon.users.domain.model.Role;
import com.emazon.users.application.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{name}")
    public Role getRoleByName(@PathVariable String name) {
        return roleService.findRoleByName(name);
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @GetMapping("/id/{id}")
    public Optional<Role> getRoleById(@PathVariable Long id) {
        return roleService.findRoleById(id);
    }

    @DeleteMapping("/id/{id}")
    public void deleteRoleById(@PathVariable Long id) {
        roleService.deleteRoleById(id);
    }
}
