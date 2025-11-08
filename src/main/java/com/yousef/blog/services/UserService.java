package com.yousef.blog.services;

import com.yousef.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
