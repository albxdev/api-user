package com.emazon.users.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Setter
    @Column(name = "dateofbirth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "documentid", nullable = false, unique = true, length = 20)
    private String documentId;

    @Column(name = "phone", length = 13)
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Setter
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_rol", nullable = false)
    private Role role;

    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(String name, String lastName, String email, String password, String documentId, String phone, LocalDate dateOfBirth, Role role) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        setPassword(password);
        this.documentId = documentId;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    // Getters y Setters

    public void setName(String name) {
        if (name.length() > 50) {
            throw new IllegalArgumentException("Name length exceeds 50 characters.");
        }
        this.name = name;
    }

    public void setLastName(String lastName) {
        if (lastName.length() > 50) {
            throw new IllegalArgumentException("Last name length exceeds 50 characters.");
        }
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        if (email.length() > 100) {
            throw new IllegalArgumentException("Email length exceeds 100 characters.");
        }
        this.email = email;
    }

    public void setDocumentId(String documentId) {
        if (documentId.length() > 20) {
            throw new IllegalArgumentException("Document ID exceeds 20 characters.");
        }
        this.documentId = documentId;
    }

    public void setPhone(String phone) {
        if (phone.length() > 13) {
            throw new IllegalArgumentException("Phone number length exceeds 13 characters.");
        }
        this.phone = phone;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }
}
