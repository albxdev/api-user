package com.emazon.users.application.service;

import com.emazon.users.domain.exception.InvalidInputException;
import com.emazon.users.domain.repository.RoleRepository;
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
import org.mockito.Mockito;
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

    @Mock
    private RoleRepository roleRepository; // Agregado para RoleRepository

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inicializa BCryptPasswordEncoder en el contexto del test
        bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        // Aquí se inicializa el servicio con el mock y el encoder
        userService = new UserServiceImpl(userRepository, bCryptPasswordEncoder, userMapper, roleRepository);
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
        role.setId(30L);
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
        when(roleRepository.findById(any())).thenReturn(Optional.of(role)); // Mock RoleRepository

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
        userDTO.setBirthDate(LocalDate.now().minusYears(20)); // Establece una fecha de nacimiento válida

        // Simula un usuario existente con el mismo email
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));

        // Verifica que la excepción correcta sea lanzada
        UserAlreadyExistsException thrownException = assertThrows(UserAlreadyExistsException.class, () ->
                userService.createAuxBodegaUser(userDTO)
        );

        // Verifica que el mensaje de la excepción es el esperado
        assertEquals("El usuario ya existe con este correo.", thrownException.getMessage());
    }




    @Test
    void testCreateUser_InvalidInput() {
        UserDTO userDTO = new UserDTO();
        userDTO.setBirthDate(LocalDate.now()); // Fecha no válida para la edad

        // Simula que el email no existe
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Verifica que se lanza la excepción de entrada inválida
        InvalidInputException thrownException = assertThrows(InvalidInputException.class, () ->
                userService.createUser(userDTO)
        );

        // Verifica que el mensaje de la excepción sea el esperado
        assertEquals("El usuario debe ser mayor de edad.", thrownException.getMessage());
    }


    @Test
    void testAuthenticate() {
        String email = "user@example.com";
        String password = "password123";
        String encodedPassword = "$2a$10$EIX9DPDOHb7G/yM8HhNGmOjETeBG/9Ir0xUJzZ4rC77chXtHb1GMK"; // Ejemplo de BCrypt password hash

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(encodedPassword);

        // Configuración de los mocks
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        Mockito.when(userRepository.findByEmail("wrong@example.com")).thenReturn(Optional.empty());
        Mockito.when(bCryptPasswordEncoder.matches(password, encodedPassword)).thenReturn(true);
        Mockito.when(bCryptPasswordEncoder.matches("wrongpassword", encodedPassword)).thenReturn(false);

        // Test para contraseña correcta
        assertTrue(userService.authenticate(email, password));

        // Test para contraseña incorrecta
        assertFalse(userService.authenticate(email, "wrongpassword"));

        // Test para email incorrecto
        assertFalse(userService.authenticate("wrong@example.com", password));
    }
}
