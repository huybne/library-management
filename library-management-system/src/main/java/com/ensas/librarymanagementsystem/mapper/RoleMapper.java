package com.ensas.librarymanagementsystem.mapper;

import com.ensas.librarymanagementsystem.Model.security.Role;
import com.ensas.librarymanagementsystem.dto.request.RoleRequest;
import com.ensas.librarymanagementsystem.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "authorities", ignore = true)
    Role toRole(RoleRequest request);

    @Mapping(target = "authorities", source = "authorities")
    RoleResponse toRoleResponse(Role request );
}