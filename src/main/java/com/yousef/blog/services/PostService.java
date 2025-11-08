package com.yousef.blog.services;

import com.yousef.blog.domain.dtos.CreatePostRequestDto;
import com.yousef.blog.domain.dtos.UpdatePostRequest;
import com.yousef.blog.domain.entities.Post;
import com.yousef.blog.domain.entities.User;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> searchPosts(UUID categoryId, UUID tagId);

    List<Post> searchDraftPosts(User user);

    Post createPost(CreatePostRequestDto entity, User user);

    Post updatePost(@Valid UpdatePostRequest dto, User user);

    Post getPostById(UUID id);

    void deletePost(UUID id, User user);
}
