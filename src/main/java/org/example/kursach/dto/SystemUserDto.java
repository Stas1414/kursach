package org.example.kursach.dto;

import org.example.kursach.domain.UserRole;

import java.time.Instant;
import java.util.Set;

public record SystemUserDto(
        Long id,
        String username,
        String fullName,
        String email,
        Set<UserRole> roles,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {}

