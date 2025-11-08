package com.yousef.blog.domain.dtos;

import com.yousef.blog.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record CreatePostRequestDto(
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 200, message = "Title must be between {min} and {max} characters")
        String title,

        @NotBlank(message = "Content is required")
        @Size(min = 10, max = 50000, message = "Content must be between {min} and {max} characters")
        String content,

        @NotNull(message = "Category is required")
        UUID categoryId,

        @Size(min = 1, max = 10, message = "A post can have between {min} and {max} tags")
        Set<UUID> tagIds,

        @NotNull(message = "Post status is required")
        PostStatus status
) {

}
