package com.yousef.blog.domain.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryDto(
        UUID id,
        String name,
        Long postCount
) {
}
