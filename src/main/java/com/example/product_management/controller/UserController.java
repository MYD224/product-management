package com.example.product_management.controller;

import com.example.product_management.auth.AuthenticationRequest;
import com.example.product_management.auth.AuthenticationResponse;
import com.example.product_management.auth.RegisterRequest;
import com.example.product_management.dto.ProductDTO;
import com.example.product_management.model.product.Product;
import com.example.product_management.model.user.User;
import com.example.product_management.service.ProductService;
import com.example.product_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/test/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

   @GetMapping("/{id}/product")
    public ResponseEntity<List<ProductDTO>> getProducts(@PathVariable Long id){
       List<ProductDTO> products = productService.getProductsByUserId(id);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> addUser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.save(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
