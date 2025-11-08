package com.yousef.blog.services.impl;

import com.yousef.blog.domain.entities.Tag;
import com.yousef.blog.repositories.TagRepository;
import com.yousef.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> listTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id " + id));
    }

    @Override
    public Set<Tag> getTagsByIds(Set<UUID> ids) {
        var foundTags = new HashSet<>(tagRepository.findAllById(ids));

        if (foundTags.size() != ids.size()) {
            throw new EntityNotFoundException("One or more tags not found");
        }

        return foundTags;
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> names) {
        var foundTags = tagRepository
                .findByNameIn(names);

        var found = foundTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        var newTags = names
                .stream()
                .filter(name -> !found.contains(name))
                .map(name -> Tag.builder().name(name).build())
                .collect(Collectors.toSet());

        if (newTags.isEmpty()) throw new IllegalStateException("All tags already exist");

        var savedTags = tagRepository.saveAll(newTags);
        savedTags.addAll(foundTags);
        return savedTags;
    }

    @Override
    public void deleteTag(UUID id) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        if (!tag.getPosts().isEmpty()) {
            throw new IllegalStateException("Cannot delete tag associated with posts");
        }

        tagRepository.deleteById(id);
    }
}
