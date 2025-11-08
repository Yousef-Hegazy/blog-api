package com.yousef.blog.domain.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthorDto(
        UUID id,
        String name
) {
}
