package org.example.kursach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 6, max = 120) String password
) {}

