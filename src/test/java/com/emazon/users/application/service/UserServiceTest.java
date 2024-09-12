package com.emazon.users.application.service;

import com.emazon.users.application.dto.UserDTO;
import com.emazon.users.domain.exception.UserAlreadyExistsException;
import com.emazon.users.domain.model.Role;
import com.emazon.users.domain.model.User;
import com.emazon.users.domain.repository.UserRepository;
import com.emazon.users.application.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Test Name");
        userDTO.setLastName("Test LastName");
        userDTO.setEmail("test@example.com");
        userDTO.setDocumentId("123456789");
        userDTO.setPhone("1234567890");
        userDTO.setPassword("password");
        userDTO.setRoleId(2L); // Ensure to use Long here

        // Set a valid birth date
        userDTO.setBirthDate(LocalDate.now().minusYears(20)); // Ensure the date is at least 18 years ago

        User user = new User();
        user.setEmail("test@example.com");
        Role role = new Role();
        role.setId(2L);
        role.setName("Aux_Bodega");
        role.setDescription("Auxiliary Warehouse");
        user.setRole(role);

        // Mock configurations
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByDocumentId(any())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Execute method
        UserDTO createdUser = userService.createUser(userDTO);

        // Verifications
        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("Test Name", createdUser.getName());
    }


    @Test
    void testCreateUserFailure_UserAlreadyExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setDocumentId("123456789");

        // Simulate existing user with the same email
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));

        // Verify the correct exception is thrown
        UserAlreadyExistsException thrownException = assertThrows(UserAlreadyExistsException.class, () ->
                userService.createUser(userDTO)
        );

        // Verify the exception message is as expected
        assertEquals("El usuario ya existe con este correo.", thrownException.getMessage());
    }
}
