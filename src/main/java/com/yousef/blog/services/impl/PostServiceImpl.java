package com.yousef.blog.services.impl;

import com.yousef.blog.domain.PostStatus;
import com.yousef.blog.domain.dtos.CreatePostRequestDto;
import com.yousef.blog.domain.dtos.UpdatePostRequest;
import com.yousef.blog.domain.entities.Post;
import com.yousef.blog.domain.entities.Tag;
import com.yousef.blog.domain.entities.User;
import com.yousef.blog.repositories.PostRepository;
import com.yousef.blog.services.CategoryService;
import com.yousef.blog.services.PostService;
import com.yousef.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    private static final int WORDS_PER_MINUTE = 130;


    @Override
    @Transactional(readOnly = true)
    public List<Post> searchPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            var category = categoryService.getCategoryById(categoryId);
            var tag = tagService.getTagById(tagId);

            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );

        }

        if (categoryId != null) {
            var category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        if (tagId != null) {
            var tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> searchDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(CreatePostRequestDto request, User user) {
        var post = Post.builder()
                .title(request.title())
                .content(request.content())
                .author(user)
                .category(categoryService.getCategoryById(request.categoryId()))
                .tags(tagService.getTagsByIds(request.tagIds()))
                .status(request.status())
                .readingTime(calculateReadingTime(request.content()))
                .build();

        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post updatePost(UpdatePostRequest dto, User user) {
        var post = postRepository.findByIdAndAuthor(dto.id(), user)
                .orElseThrow(() -> new IllegalArgumentException("Post not found or you are not the author"));

        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setStatus(dto.status());
        post.setReadingTime(calculateReadingTime(dto.content()));

        if (!dto.categoryId().equals(post.getCategory().getId())) {
            post.setCategory(categoryService.getCategoryById(dto.categoryId()));
        }

        if (!dto.tagIds().equals(post.getTags().stream().map(Tag::getId).collect(Collectors.toSet()))) {
            post.setTags(tagService.getTagsByIds(dto.tagIds()));
        }


        return postRepository.save(post);
    }

    @Override
    public Post getPostById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    @Override
    public void deletePost(UUID id, User user) {
        var post = postRepository.findByIdAndAuthor(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Post not found or you are not the author"));

        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content) {
        var words = content.split("\\s+").length;
        return (int) Math.max(1, Math.ceil((double) words / WORDS_PER_MINUTE));
    }
}
