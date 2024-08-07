package com.ensas.librarymanagementsystem.service;


import com.ensas.librarymanagementsystem.dto.request.AddAuthoritiesToRole;
import com.ensas.librarymanagementsystem.dto.request.RoleRequest;
import com.ensas.librarymanagementsystem.dto.response.RoleResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RoleService {

    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();


    void delete(UUID roleId);

    Map<String, Object> addAuthorityToRole(AddAuthoritiesToRole request);

    RoleResponse getRoleById(UUID roleId);

    RoleResponse getRoleByName(String name);

    RoleResponse updateRole(UUID roleId, RoleRequest request);
}
