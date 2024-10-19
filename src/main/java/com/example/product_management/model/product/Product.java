package com.example.product_management.model.product;

import com.example.product_management.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private String manufacturingDate;
    @NotNull(message = "Expiry date cannot be null")
    @Column(nullable = false)
    private String expiryDate;
    @NotNull(message = "Product category cannot be null. possible values: Anesthésiants, Antibiotiques, Androgènes")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category Category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}
