package com.greentrack.backend.dto;

import com.greentrack.backend.entity.User;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String role;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();
    }
}
