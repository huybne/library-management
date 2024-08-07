package com.ensas.librarymanagementsystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    String permission;
    Set<String> authorities;
}
