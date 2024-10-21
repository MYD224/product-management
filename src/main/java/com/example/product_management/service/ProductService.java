package com.example.product_management.service;

import com.example.product_management.dto.ProductDTO;
import com.example.product_management.model.entity.Product;

import java.util.List;

public interface ProductService {
    ProductDTO add(Product product);
    List<ProductDTO> getAllProducts();
    ProductDTO findProduct(Long id);
    ProductDTO updateProduct(Long id, Product product);
    String deleteProduct(Long id);
    List<ProductDTO> getProductsByUserId(Long userId);
}
