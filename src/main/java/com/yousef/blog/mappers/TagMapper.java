package com.yousef.blog.mappers;

import com.yousef.blog.domain.PostStatus;
import com.yousef.blog.domain.dtos.TagDto;
import com.yousef.blog.domain.entities.Post;
import com.yousef.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagDto toDto(Tag tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts) {
        if (null == posts) return 0;

        return (int) posts
                .stream()
                .filter(p -> PostStatus.PUBLISHED.equals(p.getStatus()))
                .count();
    }
}
