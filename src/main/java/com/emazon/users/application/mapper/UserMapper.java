package com.emazon.users.application.mapper;

import com.emazon.users.domain.model.User;
import com.emazon.users.application.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setDocumentId(dto.getDocumentId());
        user.setPhone(dto.getPhone());
        user.setDateOfBirth(dto.getBirthDate());
        user.setPassword(dto.getPassword()); // La contraseña será cifrada en la entidad
        return user;
    }

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setDocumentId(user.getDocumentId());
        dto.setPhone(user.getPhone());
        dto.setBirthDate(user.getDateOfBirth());
        dto.setPassword(user.getPassword()); // Aunque la contraseña no se debe mostrar, puedes omitirla si es necesario
        dto.setRoleId(user.getRole().getId()); // Directly set the roleId
        return dto;
    }
}
