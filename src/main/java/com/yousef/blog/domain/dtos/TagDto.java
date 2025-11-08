package com.yousef.blog.domain.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TagDto(
        UUID id,
        String name,
        Integer postCount
) {
}
