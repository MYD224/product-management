package com.example.product_management.service;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.dto.UserDTO;
import com.example.product_management.model.entity.Product;
import com.example.product_management.model.entity.User;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final   ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductDTO add(Product product){
        var username = getUserUsername();
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new NoSuchElementException("User with username: " + username + " not found"));
        product.setUser(user);
        Product product_ = productRepository.save(product);
        UserDTO userDTO = UserDTO.getUserDTOFromUser(user);

        return ProductDTO.getProductDTOFromProduct(product_, userDTO);
    }

    public List<ProductDTO> getAllProducts(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Product> products = user.getRole().equals("ADMIN") ?
                productRepository.findAll() :
                productRepository.findByUserId(user.getId());

        return products.stream().map(product -> {
            UserDTO userDTO = UserDTO.getUserDTOFromUser(user);
            return ProductDTO.getProductDTOFromProduct(product, userDTO);
        }).collect(Collectors.toList());
    }

    public ProductDTO findProduct(Long id){

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id "+ id + " not found"));
         UserDTO userDTO = UserDTO.getUserDTOFromUser(product.getUser());
        return ProductDTO.getProductDTOFromProduct(product,userDTO);
    }

    public ProductDTO updateProduct(Long id, Product product){
        return productRepository.findById(id).map(
                p->{
                    p.setCategory(product.getCategory());
                    p.setLabel(product.getLabel());
                    p.setDescription(product.getDescription());
                    p.setManufacturingDate(product.getManufacturingDate());
                    p.setExpiryDate(product.getExpiryDate());
                    Product savedProduct = productRepository.save(p);
                    UserDTO userDTO = UserDTO.getUserDTOFromUser(savedProduct.getUser());
                    return ProductDTO.getProductDTOFromProduct(savedProduct, userDTO);
                }
        ).orElseThrow(()->new EntityNotFoundException(("Product with id "+ id +" not found")));
    }

    public String deleteProduct(Long id){
        return productRepository.findById(id).map(p->{
            productRepository.deleteById(id);
            return "Product deleted successfully";
        }).orElseThrow(()-> new EntityNotFoundException(("Product with id "+ id +" not found")));
    }

    public List<ProductDTO> getProductsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " not found"));

        List<Product> savedProducts = productRepository.findByUserId(userId);
        UserDTO userDTO = UserDTO.getUserDTOFromUser(user);
        return savedProducts.stream().map(product -> {
            return ProductDTO.getProductDTOFromProduct(product, userDTO);
        }).collect(Collectors.toList());
    }

    private String getUserUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return username;
    }

}
