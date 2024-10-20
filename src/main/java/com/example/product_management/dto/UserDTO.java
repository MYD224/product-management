package com.example.product_management.dto;

import com.example.product_management.model.enums.Role;
import com.example.product_management.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private String username;
    private String email;
    private Role role;
    public static  UserDTO getUserDTOFromUser(User user) {
        return new UserDTO(user.getUsername(), user.getEmail(), user.getRole());
    }
}
