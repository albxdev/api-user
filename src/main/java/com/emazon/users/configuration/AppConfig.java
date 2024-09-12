package com.emazon.users.configuration;

import com.emazon.users.application.dto.UserDTO;
import com.emazon.users.application.service.UserService;
import com.emazon.users.domain.model.Role;
import com.emazon.users.domain.model.User;
import com.emazon.users.domain.repository.RoleRepository;
import com.emazon.users.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.Optional;

@Configuration
public class AppConfig {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AppConfig(UserService userService, RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUser() {
        // Verificar si el rol "admin" existe
        Optional<Role> roleOptional = roleRepository.findByName("Administrator");
        Role role = roleOptional.orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName("Administrator");
            newRole.setDescription("Role with all permissions.");
            return roleRepository.save(newRole);
        });

        // Verificar si el usuario "admin" ya existe
        Optional<User> userOptional = userRepository.findByName("admin");
        if (userOptional.isEmpty()) {
            // Crear y guardar el usuario admin
            User adminUser = new User();
            adminUser.setName("admin");
            adminUser.setLastName("AdminLastName");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("password")); // Asegúrate de que la contraseña esté codificada correctamente
            adminUser.setDocumentId("documentId123");
            adminUser.setPhone("1234567890");
            adminUser.setDateOfBirth(LocalDate.of(2000, 1, 1));
            adminUser.setRole(role);

            UserDTO userDTO = createUserDTOFromUser(adminUser);
            userService.createUser(userDTO);
        }
    }

    private UserDTO createUserDTOFromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword()); // Asegúrate de que esta contraseña esté codificada correctamente
        userDTO.setDocumentId(user.getDocumentId());
        userDTO.setPhone(user.getPhone());
        userDTO.setBirthDate(user.getDateOfBirth());
        userDTO.setRoleId(user.getRole().getId());
        return userDTO;
    }
}
