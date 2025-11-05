package com.yousef.blog.mappers;

import com.yousef.blog.domain.PostStatus;
import com.yousef.blog.domain.dtos.CategoryDto;
import com.yousef.blog.domain.entities.Category;
import com.yousef.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (null == posts) return 0;

        return posts
                .stream()
                .filter(p -> PostStatus.PUBLISHED.equals(p.getStatus()))
                .count();
    }
}
