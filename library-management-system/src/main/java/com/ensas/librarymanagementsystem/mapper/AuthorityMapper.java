package com.ensas.librarymanagementsystem.mapper;

import com.ensas.librarymanagementsystem.Model.security.Authority;
import com.ensas.librarymanagementsystem.dto.request.AuthorityRequest;
import com.ensas.librarymanagementsystem.dto.response.AuthorityResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    Authority toAuthority(AuthorityRequest request);
    AuthorityResponse toAuthorityResponse(Authority request );
}
