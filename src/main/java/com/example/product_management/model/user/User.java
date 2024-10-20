package com.example.product_management.model.user;

import com.example.product_management.model.enums.Role;
import com.example.product_management.model.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Username cannot be null")
    @Column(nullable = false, unique = true)
    private String username;
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email must be a valid one")
    private String email;
    @NotNull(message = "User profile cannot be null.(USER or ADMIN)")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @NotNull(message = "Password cannot be null")
    @Column(nullable = false)
    private String password;

//    @OneToMany(mappedBy = "user")
//    private List<Product> products;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
