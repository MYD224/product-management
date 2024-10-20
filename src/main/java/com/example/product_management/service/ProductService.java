package com.example.product_management.service;

import com.example.product_management.model.product.Product;
import com.example.product_management.model.user.User;
import com.example.product_management.repository.ProductRepository;
import com.example.product_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final   ProductRepository productRepository;
    private final UserRepository userRepository;

    public Product add(Product product){
        var username = getUserUsername();
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new NoSuchElementException("User with username: " + username + " not found"));
        product.setUser(user);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts(){
        var username = getUserUsername();
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new EntityNotFoundException("User with username: " + username + " not found"));
        return user.getRole().equals("ADMIN") ?
                productRepository.findAll() :
                productRepository.findByUserId(user.getId());
    }

    public Product findProduct(Long id){

        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id "+ id + " not found"));
    }

    public Product updateProduct(Long id, Product product){
        return productRepository.findById(id).map(
                p->{
                    p.setCategory(product.getCategory());
                    p.setLabel(product.getLabel());
                    p.setDescription(product.getDescription());
                    p.setManufacturingDate(product.getManufacturingDate());
                    p.setExpiryDate(product.getExpiryDate());
                    return productRepository.save(p);
                }
        ).orElseThrow(()->new EntityNotFoundException(("Product with id "+ id +" not found")));
    }

    public String deleteProduct(Long id){
        return productRepository.findById(id).map(p->{
            productRepository.deleteById(id);
            return "Product deleted successfully";
        }).orElseThrow(()-> new EntityNotFoundException(("Product with id "+ id +" not found")));
    }

    public List<Product> getProductsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " not found"));

        return productRepository.findByUserId(userId);
    }

    private String getUserUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return username;
    }
}
