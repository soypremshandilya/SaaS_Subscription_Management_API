package com.saas.subscription.service;

import com.saas.subscription.dto.UserLoginRequest;
import com.saas.subscription.dto.UserRegistrationRequest;
import com.saas.subscription.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest request);
    UserResponse loginUser(UserLoginRequest request);
}
