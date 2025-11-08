package com.yousef.blog.services;

import com.yousef.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {

    List<Tag> listTags();

    Tag getTagById(UUID id);

    Set<Tag> getTagsByIds(Set<UUID> ids);

    List<Tag> createTags(Set<String> names);

    void deleteTag(UUID id);
}
