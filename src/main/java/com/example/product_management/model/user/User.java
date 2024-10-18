package com.example.product_management.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email must be a valid one")
    private String email;
    @NotNull(message = "User profile cannot be null.(USER or ADMIN)")
    private Role profile;
    @NotNull(message = "Password cannot be null")
    private String password;
}
