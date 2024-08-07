package com.ensas.librarymanagementsystem.mapper;

import com.ensas.librarymanagementsystem.Model.security.User;
import com.ensas.librarymanagementsystem.dto.request.UserCreationRequest;
import com.ensas.librarymanagementsystem.dto.request.UserUpdateRequest;
import com.ensas.librarymanagementsystem.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
