package com.emazon.users.application.service;

import com.emazon.users.application.dto.UserDTO;
import com.emazon.users.domain.exception.EmailAlreadyExistsException;
import com.emazon.users.domain.exception.InvalidInputException;
import com.emazon.users.domain.exception.UserAlreadyExistsException;
import com.emazon.users.domain.model.User;
import com.emazon.users.domain.model.Role;
import com.emazon.users.domain.repository.RoleRepository;
import com.emazon.users.domain.repository.UserRepository;
import com.emazon.users.application.mapper.UserMapper;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Primary
public class UserServiceImpl extends UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper, RoleRepository roleRepository) {
        super(userRepository, bCryptPasswordEncoder, userMapper);
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTO createAuxBodegaUser(UserDTO userDTO) {
        if (userDTO.getBirthDate() == null || userDTO.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new ValidationException("El usuario debe ser mayor de edad.");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario ya existe con este correo.");
        }

        // Crear rol "Aux_Bodega"
        Role role = new Role("Aux_Bodega", "Auxiliary Warehouse");

        User user = userMapper.toEntity(userDTO);
        user.setRole(role);
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("\"El usuario ya existe con este correo.");
        }

        if (userRepository.findByDocumentId(userDTO.getDocumentId()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario ya existe con este documento.");
        }

        if (userDTO.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
            throw new InvalidInputException("El usuario debe ser mayor de edad.");
        }

        validateUser(userDTO);

        // Aquí obtenemos el rol del usuario, asegurándonos de que no sea nulo
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new ValidationException("Rol no encontrado."));

        User user = userMapper.toEntity(userDTO);
        user.setRole(role);  // Asegúrate de asignar el rol al usuario
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }


    private void validateUser(UserDTO userDTO) {
        if (!isValidEmail(userDTO.getEmail())) {
            throw new ValidationException("Formato de correo inválido.");
        }

        if (!isValidPhone(userDTO.getPhone())) {
            throw new ValidationException("Formato de número de teléfono inválido.");
        }

        if (!isOfLegalAge(userDTO.getBirthDate())) {
            throw new ValidationException("El usuario debe tener al menos 18 años.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^\\+?[0-9]{10,13}$");
    }

    private boolean isOfLegalAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new ValidationException("La fecha de nacimiento no puede ser null.");
        }
        return LocalDate.now().minusYears(18).isAfter(birthDate);
    }

}
