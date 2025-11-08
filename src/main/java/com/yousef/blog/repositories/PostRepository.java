package com.yousef.blog.repositories;

import com.yousef.blog.domain.PostStatus;
import com.yousef.blog.domain.entities.Category;
import com.yousef.blog.domain.entities.Post;
import com.yousef.blog.domain.entities.Tag;
import com.yousef.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(
            PostStatus status,
            Category category,
            Tag tag
    );

    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);

    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);

    List<Post> findAllByStatus(PostStatus status);

    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);

    Optional<Post> findByIdAndAuthor(UUID id, User user);
}
