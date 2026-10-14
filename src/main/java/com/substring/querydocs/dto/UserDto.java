package com.substring.querydocs.dto;

import com.substring.querydocs.entity.Role;

public record UserDto(
        Long id,
        String username,
        String email,
        Role role
) {
}
