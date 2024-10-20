package com.example.product_management.dto;

import com.example.product_management.model.enums.Category;
import com.example.product_management.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductDTO {
    private Long id;

    private String label;
    private String description;
    private String manufacturingDate;
    private String expiryDate;
    private Category category;
    private UserDTO user;


    public static ProductDTO getProductDTOFromProduct(Product product, UserDTO userDTO) {
        return new ProductDTO(
                product.getId(),
                product.getLabel(),
                product.getDescription(),
                product.getManufacturingDate(),
                product.getExpiryDate(),
                product.getCategory(),userDTO
        );
    }
}
