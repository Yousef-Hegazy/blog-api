package com.yousef.blog.controllers;

import com.yousef.blog.domain.dtos.CreateTagsRequest;
import com.yousef.blog.domain.dtos.TagDto;
import com.yousef.blog.mappers.TagMapper;
import com.yousef.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        return ResponseEntity.ok(tagService.listTags().stream().map(tagMapper::toDto).toList());
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@RequestBody @Valid CreateTagsRequest createTagsRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        tagService
                                .createTags(createTagsRequest.names())
                                .stream()
                                .map(tagMapper::toDto)
                                .toList()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
