package org.example.kursach.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.kursach.domain.UserRole;

import java.util.Set;

public record CreateUserRequest(
        @NotBlank @Size(min = 3, max = 64) String username,
        @NotBlank @Size(min = 6, max = 120) String password,
        @NotBlank @Size(max = 120) String fullName,
        @NotBlank @Email String email,
        Set<UserRole> roles
) {}

