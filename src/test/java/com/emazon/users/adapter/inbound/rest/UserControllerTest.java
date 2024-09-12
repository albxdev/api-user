package com.emazon.users.adapter.inbound.rest;

import com.emazon.users.application.dto.UserDTO;
import com.emazon.users.application.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; // Only mock UserService

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "testUser")
    public void testCreateAuxBodegaUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("John");
        userDTO.setLastName("Doe");
        userDTO.setDocumentId("123456789");
        userDTO.setPhone("+573005698325");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("password");
        userDTO.setBirthDate(LocalDate.of(1990, 1, 1));

        // Mocking UserDTO to be returned by the service
        Mockito.when(userService.createAuxBodegaUser(Mockito.any(UserDTO.class)))
                .thenReturn(userDTO);

        mockMvc.perform(post("/api/users/create-aux-bodega")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }
}
