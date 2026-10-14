package com.substring.querydocs.service;

import com.substring.querydocs.dto.RegisterUserRequest;
import com.substring.querydocs.dto.UserDto;
import com.substring.querydocs.entity.Role;
import com.substring.querydocs.entity.User;
import com.substring.querydocs.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerUser(@Valid RegisterUserRequest registerUserRequest) {

        //validation
        if(registerUserRequest.username().isBlank()){
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if(userRepository.existsByUsername(registerUserRequest.username())){
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerUserRequest.username());
        user.setEmail(registerUserRequest.email());
        user.setPassword(passwordEncoder.encode(registerUserRequest.password()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        return new UserDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole());
    }
}
