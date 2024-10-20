package com.example.product_management.controller;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.model.product.Product;
import com.example.product_management.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/test/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public List<ProductDTO> getProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO findProduct(@PathVariable Long id){
        return productService.findProduct(id);
    }

    @PostMapping("")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product){
        ProductDTO createdProduct = productService.add(product);
        return ResponseEntity.ok(createdProduct);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product product){
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

}