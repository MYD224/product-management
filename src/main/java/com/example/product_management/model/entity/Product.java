package com.example.product_management.model.entity;

import com.example.product_management.model.data_patterns.DataFormat;
import com.example.product_management.model.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product label cannot be null")
    @Column(nullable = false)
    private String label;
    private String description;
    @NotNull(message = "Manufacturing date cannot be null")
    @Column(nullable = false)
    @JsonFormat(pattern = DataFormat.DATE_FORMAT)
    private LocalDate manufacturingDate;
    @NotNull(message = "Expiry date cannot be null")
    @Column(nullable = false)
    @JsonFormat(pattern = DataFormat.DATE_FORMAT)
    private LocalDate expiryDate;
    @NotNull(message = "Product category cannot be null. possible values: Anesthésiants, Antibiotiques, Androgènes")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}
