package com.yousef.blog.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotBlank
        @Email(message = "Invalid email")
        String email,

        @NotBlank
        @Size(min = 4, message = "Password must be at least 4 characters long")
        String password
) {
}
