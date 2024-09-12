package com.emazon.users.application.service;

import com.emazon.users.application.dto.AuthRequest;
import com.emazon.users.application.dto.AuthResponse;
import com.emazon.users.domain.exception.InvalidCredentialsException;
import com.emazon.users.domain.model.User;
import com.emazon.users.domain.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        Optional<User> userOptional = userRepository.findByEmail(authRequest.getEmail());
        User user = userOptional.orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            String token = generateToken(user);
            return new AuthResponse(token);
        } else {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
