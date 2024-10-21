package com.example.product_management.controller;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.model.entity.Product;
import com.example.product_management.model.enums.Category;
import com.example.product_management.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        product = new Product(1L, "Product 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(1), Category.Anesthésiants, null);
        productDTO = new ProductDTO(1L, "Product 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(1), Category.Anesthésiants, null);
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/api/v1/test/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].label").value("Product 1"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void shouldFindProductById() throws Exception {
        when(productService.findProduct(anyLong())).thenReturn(productDTO);

        mockMvc.perform(get("/api/v1/test/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.label").value("Product 1"));

        verify(productService, times(1)).findProduct(anyLong());
    }

    @Test
    void shouldCreateProduct() throws Exception {
        when(productService.add(any(Product.class))).thenReturn(productDTO);

        mockMvc.perform(post("/api/v1/test/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"label\":\"Product 1\",\"description\":\"Description 1\",\"manufacturingDate\":\"2024-10-21\",\"expiryDate\":\"2024-10-22\",\"category\":\"Anesthésiants\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.label").value("Product 1"));

        verify(productService, times(1)).add(any(Product.class));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/v1/test/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"label\":\"Updated Product\",\"description\":\"Updated Description\",\"manufacturingDate\":\"2024-10-21\",\"expiryDate\":\"2024-10-22\",\"category\":\"Antibiotiques\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.label").value("Product 1"));

        verify(productService, times(1)).updateProduct(anyLong(), any(Product.class));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        when(productService.deleteProduct(anyLong())).thenReturn("Product deleted successfully");

        mockMvc.perform(delete("/api/v1/test/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));

        verify(productService, times(1)).deleteProduct(anyLong());
    }
}

