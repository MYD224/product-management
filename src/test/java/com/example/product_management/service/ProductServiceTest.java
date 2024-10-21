package com.example.product_management.service;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.dto.UserDTO;
import com.example.product_management.model.entity.Product;
import com.example.product_management.model.entity.User;
import com.example.product_management.model.enums.Category;
import com.example.product_management.model.enums.Role;
import com.example.product_management.repository.ProductRepository;
import com.example.product_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    private User user;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        user = new User(1L, "testUser", "test@domain.com", Role.USER, "password");

        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);

        product = new Product(1L, "Product 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(1), Category.Anesthésiants, user);
        productDTO = new ProductDTO(1L, "Product 1", "Description 1", LocalDate.now(), LocalDate.now().plusDays(1), Category.Androgènes, UserDTO.getUserDTOFromUser(user));
    }

    @Test
    void shouldAddProductSuccessfully() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO addedProduct = productService.add(product);

        assertNotNull(addedProduct);
        assertEquals(productDTO.getLabel(), addedProduct.getLabel());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingProductForNonExistentUser() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.add(product));
    }

    @Test
    void shouldReturnAllProductsForUser() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(productRepository.findByUserId(user.getId())).thenReturn(List.of(product));

        List<ProductDTO> products = productService.getAllProducts();

        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    void shouldFindProductByIdSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO foundProduct = productService.findProduct(1L);

        assertNotNull(foundProduct);
        assertEquals(productDTO.getLabel(), foundProduct.getLabel());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistentProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.findProduct(1L));
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        Product updatedProduct = new Product(1L, "Updated Label", "Updated Description", LocalDate.now(), LocalDate.now().plusDays(2), Category.Antibiotiques, user);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductDTO updatedProductDTO = productService.updateProduct(1L, updatedProduct);

        assertEquals("Updated Label", updatedProductDTO.getLabel());
        assertEquals(Category.Antibiotiques, updatedProductDTO.getCategory());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(1L, product));
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        String result = productService.deleteProduct(1L);

        assertEquals("Product deleted successfully", result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(1L));
    }
}

