package com.example.product_management.controller;

import com.example.product_management.auth.AuthenticationRequest;
import com.example.product_management.auth.AuthenticationResponse;
import com.example.product_management.auth.RegisterRequest;
import com.example.product_management.dto.ProductDTO;
import com.example.product_management.model.entity.User;
import com.example.product_management.service.ProductServiceImpl;
import com.example.product_management.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/test/users")
public class UserController {
    private UserServiceImpl userService;
    private ProductServiceImpl productService;

   @GetMapping("/{id}/product")
    public ResponseEntity<List<ProductDTO>> getProducts(@PathVariable Long id){
       List<ProductDTO> products = productService.getProductsByUserId(id);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> addUser(@RequestBody RegisterRequest request){
        return new ResponseEntity<>(userService.save(request), HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
