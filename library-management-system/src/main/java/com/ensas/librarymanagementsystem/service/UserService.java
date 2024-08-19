package com.ensas.librarymanagementsystem.service;

import com.ensas.librarymanagementsystem.Model.security.Role;
import com.ensas.librarymanagementsystem.Model.security.User;
import com.ensas.librarymanagementsystem.dto.request.ChangePasswordRequest;
import com.ensas.librarymanagementsystem.dto.request.UserCreationRequest;
import com.ensas.librarymanagementsystem.dto.request.UserUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;


public interface UserService {
    void deleteUser(UUID userid);
    UserResponse getUser(UUID userId);
    List<UserResponse> getUsers();
    UserResponse createUser(UserCreationRequest request);
    User saveUser(User user);

    UserResponse getMyInfo();

    UserResponse UpdateUser(UUID userId, UserUpdateRequest request);

    void changePassword(UUID userId, ChangePasswordRequest changePasswordRequest);

    void addRolesToUser(UUID uuid, List<Role> roles);
    User loadUserByUserName(String username);
}
