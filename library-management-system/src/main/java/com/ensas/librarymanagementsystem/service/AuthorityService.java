package com.ensas.librarymanagementsystem.service;

import com.ensas.librarymanagementsystem.dto.request.AuthorityRequest;
import com.ensas.librarymanagementsystem.dto.response.AuthorityResponse;

import java.util.List;
import java.util.UUID;

public interface AuthorityService {

    AuthorityResponse create(AuthorityRequest request);

    List<AuthorityResponse> getAll();

    void delete(UUID authorityId);

    AuthorityResponse getAuthorityById(UUID id);

    AuthorityResponse getAuthorityByName(String name);

    AuthorityResponse updateAuthority(UUID AuthorityId, AuthorityRequest request);
}
