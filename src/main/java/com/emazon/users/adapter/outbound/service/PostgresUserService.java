package com.emazon.users.adapter.outbound.service;

import com.emazon.users.domain.model.User;
import com.emazon.users.adapter.outbound.repository.UserRepositoryPostgres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostgresUserService {

    private final UserRepositoryPostgres userRepositoryPostgres;

    @Autowired
    public PostgresUserService(UserRepositoryPostgres userRepositoryPostgres) {
        this.userRepositoryPostgres = userRepositoryPostgres;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepositoryPostgres.findByEmail(email);
    }

    public Optional<User> getUserByDocumentId(String documentId) {
        return userRepositoryPostgres.findByDocumentId(documentId);
    }
}
