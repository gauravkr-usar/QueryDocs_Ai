package com.substring.querydocs.dto;

import com.substring.querydocs.entity.Role;

public record RegisterUserRequest(
        String username,
        String email,
        String password
) {
}
