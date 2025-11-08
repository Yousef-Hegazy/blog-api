package com.yousef.blog.controllers;

import com.yousef.blog.domain.dtos.CreatePostRequestDto;
import com.yousef.blog.domain.dtos.PostDto;
import com.yousef.blog.domain.dtos.UpdatePostRequest;
import com.yousef.blog.mappers.PostMapper;
import com.yousef.blog.services.PostService;
import com.yousef.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> searchPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId
    ) {
        return ResponseEntity.ok(postService.searchPosts(categoryId, tagId).stream()
                .map(postMapper::toDto)
                .toList());
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<PostDto>> searchDraftPosts(@RequestAttribute UUID userId) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        var user = userService.getUserById(userId);

        return ResponseEntity.ok(
                postService
                        .searchDraftPosts(user)
                        .stream()
                        .map(postMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable UUID id) {
        return ResponseEntity.ok(postMapper.toDto(postService.getPostById(id)));
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto dto,
            @RequestAttribute UUID userId
    ) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        var user = userService.getUserById(userId);

        return ResponseEntity.ok(
                postMapper.toDto(
                        postService.createPost(dto, user)
                )
        );
    }

    @PutMapping
    public ResponseEntity<PostDto> updatePost(
            @RequestAttribute UUID userId,
            @Valid @RequestBody UpdatePostRequest dto
    ) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        var user = userService.getUserById(userId);

        return ResponseEntity.ok(
                postMapper.toDto(
                        postService.updatePost(dto, user)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @RequestAttribute UUID userId,
            @PathVariable UUID id
    ) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        var user = userService.getUserById(userId);
        postService.deletePost(id, user);
        return ResponseEntity.noContent().build();
    }
}
