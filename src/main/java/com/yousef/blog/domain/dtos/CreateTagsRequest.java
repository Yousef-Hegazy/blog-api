package com.yousef.blog.domain.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record CreateTagsRequest(
        @NotEmpty(message = "At least one tag name must be provided")
        @Size(max = 10, message = "Maximum of {max} tag names can be provided at once")
        Set<
                @Size(min = 2, max = 30, message = "Tag name must be between {min} and {max} characters")
                @Pattern(regexp = "^[\\w\\s-]+$", message = "Tag name can only contain letters, numbers, spaces, underscores, and hyphens")
                        String
                > names
) {
}
