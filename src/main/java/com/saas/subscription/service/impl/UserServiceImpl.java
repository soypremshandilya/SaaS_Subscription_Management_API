package com.saas.subscription.service.impl;

import com.saas.subscription.dto.UserLoginRequest;
import com.saas.subscription.dto.UserRegistrationRequest;
import com.saas.subscription.dto.UserResponse;
import com.saas.subscription.exception.BadRequestException;
import com.saas.subscription.exception.ResourceNotFoundException;
import com.saas.subscription.model.User;
import com.saas.subscription.repository.UserRepository;
import com.saas.subscription.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        // Fast plain text pass as per 'basic authentication, no JWT required initially' requirement
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword()) 
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .message("User registered successfully")
                .build();
    }

    @Override
    public UserResponse loginUser(UserLoginRequest request) {
        String input = request.getUsernameOrEmail();
        
        Optional<User> userOptional = userRepository.findByUsername(input);
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByEmail(input);
        }

        User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("User not found with provided username or email"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .message("Login successful")
                .build();
    }
}
