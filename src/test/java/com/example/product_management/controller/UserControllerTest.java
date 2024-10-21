package com.example.product_management.controller;

import com.example.product_management.auth.AuthenticationRequest;
import com.example.product_management.auth.AuthenticationResponse;
import com.example.product_management.auth.RegisterRequest;
import com.example.product_management.config.JwtService;
import com.example.product_management.dto.ProductDTO;
import com.example.product_management.model.entity.User;
import com.example.product_management.model.enums.Category;
import com.example.product_management.model.enums.Role;
import com.example.product_management.service.ProductServiceImpl;
import com.example.product_management.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private ProductServiceImpl productService;
    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("testUser", "test@example.com", Role.USER, "password123");
        authRequest = new AuthenticationRequest("testUser", "password123");
    }

    @Test

    void shouldRegisterUserSuccessfully() throws Exception {
        User user = new User(1L, "testUser", "test@example.com", Role.USER, "password123");

        when(userService.save(any(RegisterRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/test/users/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));


        verify(userService, times(1)).save(any(RegisterRequest.class));
    }

    @Test
    void shouldLoginUserSuccessfully() throws Exception {
        AuthenticationResponse authResponse = new AuthenticationResponse("jwt_token_example");

        when(userService.authenticate(any(AuthenticationRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/test/users/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_token_example"));

        verify(userService, times(1)).authenticate(any(AuthenticationRequest.class));
    }

    @Test
    void shouldReturnProductsForUser() throws Exception {
        List<ProductDTO> productList = Arrays.asList(
                new ProductDTO(1L, "Product1", "Description1", LocalDate.now(), LocalDate.now().plusDays(10), Category.Androgènes, null),
                new ProductDTO(2L, "Product2", "Description2", LocalDate.now(), LocalDate.now().plusDays(20), Category.Anesthésiants, null)
        );

        when(productService.getProductsByUserId(1L)).thenReturn(productList);

        mockMvc.perform(get("/api/v1/test/users/1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("Product1"))
                .andExpect(jsonPath("$[1].label").value("Product2"));

        verify(productService, times(1)).getProductsByUserId(1L);
    }

}

