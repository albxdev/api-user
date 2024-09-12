package com.emazon.users.application.service;

import com.emazon.users.application.dto.UserDTO;
import com.emazon.users.domain.exception.UserAlreadyExistsException;
import com.emazon.users.domain.model.User;
import com.emazon.users.domain.model.Role;
import com.emazon.users.domain.repository.UserRepository;
import com.emazon.users.application.mapper.UserMapper;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public abstract class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserDTO createUser(UserDTO userDTO) {
        // Validar el usuario
        validateUser(userDTO);

        // Verificar si el usuario ya existe por correo electrónico o documento de identidad
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("The user already exists with this email.");
        }

        if (userRepository.findByDocumentId(userDTO.getDocumentId()).isPresent()) {
            throw new UserAlreadyExistsException("The user already exists with this document ID.");
        }

        // Mapear el DTO a la entidad User
        User user = userMapper.toEntity(userDTO);

        // Encriptar la contraseña
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Guardar el usuario en la base de datos
        user = userRepository.save(user);

        // Mapear la entidad User a DTO
        return userMapper.toDTO(user);
    }

    public UserDTO createAuxBodegaUser(UserDTO userDTO) {
        // Validar que el usuario sea mayor de 18 años
        if (isOfLegalAge(userDTO.getBirthDate())) {
            throw new IllegalArgumentException("User must be at least 18 years old.");
        }

        // Crear el rol "Aux_Bodega"
        Role role = new Role("Aux_Bodega", "Description of the role");

        // Mapear el DTO a la entidad User
        User user = userMapper.toEntity(userDTO);
        user.setRole(role);

        // Encriptar la contraseña
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Guardar el usuario
        User savedUser = userRepository.save(user);

        // Mapear la entidad guardada a DTO y devolverla
        return userMapper.toDTO(savedUser);
    }

    public boolean authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    private void validateUser(UserDTO userDTO) {
        if (!isValidEmail(userDTO.getEmail())) {
            throw new ValidationException("Invalid email format.");
        }

        if (!isValidPhone(userDTO.getPhone())) {
            throw new ValidationException("Invalid phone number format.");
        }

        if (!isValidIdentityDocument(userDTO.getDocumentId())) {
            throw new ValidationException("Identity document must be numeric.");
        }

        if (isOfLegalAge(userDTO.getBirthDate())) {
            throw new ValidationException("User must be at least 18 years old.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^\\+?[0-9]{10,13}$");
    }

    private boolean isValidIdentityDocument(String document) {
        return document.matches("\\d+");
    }

    private boolean isOfLegalAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears() < 18;
    }

    protected abstract UserRepository getUserRepository();

    protected abstract BCryptPasswordEncoder getPasswordEncoder();
}
