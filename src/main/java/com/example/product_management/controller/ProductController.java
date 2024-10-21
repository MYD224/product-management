package com.example.product_management.controller;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.model.entity.Product;
import com.example.product_management.service.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/test/products")
public class ProductController {
    private final ProductServiceImpl productService;

    @GetMapping("")
    public List<ProductDTO> getProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO findProduct(@PathVariable Long id){
        return productService.findProduct(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product){
        ProductDTO createdProduct = productService.add(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
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