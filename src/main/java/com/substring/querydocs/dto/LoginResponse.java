package com.substring.querydocs.dto;

public record LoginResponse(
        String accessToken,
        UserDto user
) {
}
