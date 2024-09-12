package com.emazon.users.adapter.inbound.rest;

import com.emazon.users.application.dto.AuthRequest;
import com.emazon.users.application.dto.AuthResponse;
import com.emazon.users.application.service.AuthService;
import com.emazon.users.configuration.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class) // Reemplaza con tu configuración de seguridad
@ActiveProfiles("test") // Usa un perfil específico para pruebas si tienes configuraciones separadas
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    public void testLogin() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("john.doe@example.com");
        authRequest.setPassword("password");

        AuthResponse authResponse = new AuthResponse("dummy-token");

        Mockito.when(authService.authenticate(Mockito.any(AuthRequest.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-token"));
    }
}
