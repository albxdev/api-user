package com.emazon.users.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String documentId;
    private String phone;
    private String password;
    private Long roleId;

    // Getters y Setters

}
