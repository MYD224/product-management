package com.example.product_management.controller;

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
    public ResponseEntity<List<Product>> getProducts(@PathVariable Long id){
       List<Product> products = productService.getProductsByUserId(id);
        return ResponseEntity.ok(products);
    }

    @PostMapping("")
    public ResponseEntity<User> addUser(@RequestBody User user){
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/login")
    public void login(@RequestBody User user){

    }
}
