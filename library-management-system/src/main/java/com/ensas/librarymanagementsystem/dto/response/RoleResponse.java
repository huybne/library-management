package com.ensas.librarymanagementsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class RoleResponse {
    UUID id;
    String name;
    String permission;
    Set<AuthorityResponse> authorities;
}
